package com.bedwars.commands;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.Arena;
import com.bedwars.models.PlayerStats;
import com.bedwars.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BedwarsCommand implements CommandExecutor {
    
    private final BedwarsPlugin plugin;
    
    public BedwarsCommand(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.color("&cOnly players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "play":
            case "join":
                if (args.length == 1) {
                    // Open game mode selector GUI
                    handlePlayGUI(player);
                } else {
                    // Direct join to arena
                    handleJoin(player, args[1]);
                }
                break;
                
            case "leave":
                handleLeave(player);
                break;
                
            case "stats":
                if (args.length > 1) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bw stats"));
                    return true;
                }
                handleStats(player);
                break;
                
            case "list":
                handleList(player);
                break;
                
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(Player player) {
        player.sendMessage(MessageUtil.color("&7&m                                    "));
        player.sendMessage(MessageUtil.color("&c&lBedwars Commands"));
        player.sendMessage(MessageUtil.color("&e/bw play &7- Open game mode selector"));
        player.sendMessage(MessageUtil.color("&e/bw join <arena> &7- Join a specific arena"));
        player.sendMessage(MessageUtil.color("&e/bw leave &7- Leave current game"));
        player.sendMessage(MessageUtil.color("&e/bw stats &7- View your statistics"));
        player.sendMessage(MessageUtil.color("&e/bw list &7- List available arenas"));
        player.sendMessage(MessageUtil.color("&7&m                                    "));
    }
    
    private void handlePlayGUI(Player player) {
        if (plugin.getGameManager().isInGame(player)) {
            player.sendMessage(MessageUtil.color("&cYou are already in a game!"));
            return;
        }
        
        // Open the game mode selector GUI
        plugin.getGUIListener().getSelectorGUI().openGameModeSelector(player);
    }
    
    private void handleJoin(Player player, String arenaName) {
        if (plugin.getGameManager().isInGame(player)) {
            player.sendMessage(MessageUtil.color("&cYou are already in a game!"));
            return;
        }
        
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(MessageUtil.color("&cArena not found!"));
            return;
        }
        
        if (!arena.isEnabled()) {
            player.sendMessage(MessageUtil.color("&cThis arena is currently disabled!"));
            return;
        }
        
        if (plugin.getGameManager().joinGame(player, arenaName)) {
            player.sendMessage(MessageUtil.color("&aYou joined the game!"));
        } else {
            player.sendMessage(MessageUtil.color("&cFailed to join the game! It may be full or already started."));
        }
    }
    
    private void handleLeave(Player player) {
        if (!plugin.getGameManager().isInGame(player)) {
            player.sendMessage(MessageUtil.color("&cYou are not in a game!"));
            return;
        }
        
        if (plugin.getGameManager().leaveGame(player)) {
            player.sendMessage(MessageUtil.color("&aYou left the game!"));
        } else {
            player.sendMessage(MessageUtil.color("&cFailed to leave the game!"));
        }
    }
    
    private void handleStats(Player player) {
        PlayerStats stats = plugin.getGameManager().getPlayerStats(player);
        
        player.sendMessage(MessageUtil.color("&7&m                                    "));
        player.sendMessage(MessageUtil.color("&c&lYour Bedwars Statistics"));
        player.sendMessage(MessageUtil.color("&eKills: &f" + stats.getKills()));
        player.sendMessage(MessageUtil.color("&eDeaths: &f" + stats.getDeaths()));
        player.sendMessage(MessageUtil.color("&eK/D Ratio: &f" + String.format("%.2f", stats.getKDRatio())));
        player.sendMessage(MessageUtil.color("&eFinal Kills: &f" + stats.getFinalKills()));
        player.sendMessage(MessageUtil.color("&eFinal Deaths: &f" + stats.getFinalDeaths()));
        player.sendMessage(MessageUtil.color("&eBeds Destroyed: &f" + stats.getBedsDestroyed()));
        player.sendMessage(MessageUtil.color("&eWins: &f" + stats.getWins()));
        player.sendMessage(MessageUtil.color("&eLosses: &f" + stats.getLosses()));
        player.sendMessage(MessageUtil.color("&eGames Played: &f" + stats.getGamesPlayed()));
        player.sendMessage(MessageUtil.color("&eWin Rate: &f" + String.format("%.1f%%", stats.getWinRate())));
        player.sendMessage(MessageUtil.color("&7&m                                    "));
    }
    
    private void handleList(Player player) {
        List<Arena> arenas = plugin.getArenaManager().getAvailableArenas();
        
        player.sendMessage(MessageUtil.color("&7&m                                    "));
        player.sendMessage(MessageUtil.color("&c&lAvailable Arenas"));
        
        if (arenas.isEmpty()) {
            player.sendMessage(MessageUtil.color("&7No arenas available"));
        } else {
            for (Arena arena : arenas) {
                BedwarsGame game = plugin.getGameManager().getGames().stream()
                    .filter(g -> g.getArena().getName().equals(arena.getName()))
                    .findFirst()
                    .orElse(null);
                
                String status;
                int players = 0;
                
                if (game == null) {
                    status = "&aWaiting";
                } else {
                    players = game.getTotalPlayers();
                    switch (game.getState()) {
                        case WAITING:
                            status = "&aWaiting";
                            break;
                        case STARTING:
                            status = "&eStarting";
                            break;
                        case ACTIVE:
                            status = "&cIn Progress";
                            break;
                        default:
                            status = "&7Ending";
                            break;
                    }
                }
                
                player.sendMessage(MessageUtil.color("&e" + arena.getDisplayName() + " &7- &6" + 
                    arena.getGameMode().getDisplayName() + " &7- " + 
                    status + " &7(" + players + "/" + arena.getMaxPlayers() + ")"));
            }
        }
        
        player.sendMessage(MessageUtil.color("&7&m                                    "));
    }
}
