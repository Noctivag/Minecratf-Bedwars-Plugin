package com.bedwars.commands;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.*;
import com.bedwars.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BedwarsAdminCommand implements CommandExecutor {
    
    private final BedwarsPlugin plugin;
    private final Map<String, String> setupMode; // player -> arena being set up
    
    public BedwarsAdminCommand(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.setupMode = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.color("&cOnly players can use this command!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bedwars.admin")) {
            player.sendMessage(MessageUtil.color("&cYou don't have permission to use this command!"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "setup":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin setup <arena>"));
                    return true;
                }
                handleSetup(player, args[1]);
                break;
                
            case "setlobby":
                handleSetLobby(player);
                break;
                
            case "setbed":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin setbed <team>"));
                    return true;
                }
                handleSetBed(player, args[1]);
                break;
                
            case "setspawn":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin setspawn <team>"));
                    return true;
                }
                handleSetSpawn(player, args[1]);
                break;
                
            case "setresource":
                if (args.length < 3) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin setresource <type> [team]"));
                    player.sendMessage(MessageUtil.color("&cTypes: IRON, GOLD, DIAMOND, EMERALD"));
                    return true;
                }
                handleSetResource(player, args);
                break;
                
            case "savearena":
                handleSaveArena(player);
                break;
                
            case "deletearena":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin deletearena <arena>"));
                    return true;
                }
                handleDeleteArena(player, args[1]);
                break;
                
            case "list":
                handleListArenas(player);
                break;
                
            case "enable":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin enable <arena>"));
                    return true;
                }
                handleEnableArena(player, args[1]);
                break;
                
            case "disable":
                if (args.length < 2) {
                    player.sendMessage(MessageUtil.color("&cUsage: /bwadmin disable <arena>"));
                    return true;
                }
                handleDisableArena(player, args[1]);
                break;
                
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    private void sendHelp(Player player) {
        player.sendMessage(MessageUtil.color("&7&m                                    "));
        player.sendMessage(MessageUtil.color("&c&lBedwars Admin Commands"));
        player.sendMessage(MessageUtil.color("&e/bwa setup <arena> &7- Start arena setup"));
        player.sendMessage(MessageUtil.color("&e/bwa setlobby &7- Set lobby spawn point"));
        player.sendMessage(MessageUtil.color("&e/bwa setbed <team> &7- Set team bed location"));
        player.sendMessage(MessageUtil.color("&e/bwa setspawn <team> &7- Set team spawn point"));
        player.sendMessage(MessageUtil.color("&e/bwa setresource <type> [team] &7- Set resource spawner"));
        player.sendMessage(MessageUtil.color("&e/bwa savearena &7- Save current arena"));
        player.sendMessage(MessageUtil.color("&e/bwa deletearena <arena> &7- Delete an arena"));
        player.sendMessage(MessageUtil.color("&e/bwa list &7- List all arenas"));
        player.sendMessage(MessageUtil.color("&e/bwa enable <arena> &7- Enable an arena"));
        player.sendMessage(MessageUtil.color("&e/bwa disable <arena> &7- Disable an arena"));
        player.sendMessage(MessageUtil.color("&7Teams: RED, BLUE, GREEN, YELLOW"));
        player.sendMessage(MessageUtil.color("&7&m                                    "));
    }
    
    private void handleSetup(Player player, String arenaName) {
        setupMode.put(player.getName(), arenaName);
        player.sendMessage(MessageUtil.color("&aStarted setup for arena: &e" + arenaName));
        player.sendMessage(MessageUtil.color("&7Now you can configure the arena using other commands."));
        player.sendMessage(MessageUtil.color("&7Use /bwa savearena when done to save the configuration."));
    }
    
    private void handleSetLobby(Player player) {
        String arenaName = setupMode.get(player.getName());
        if (arenaName == null) {
            player.sendMessage(MessageUtil.color("&cYou must start setup first! Use /bwa setup <arena>"));
            return;
        }
        
        Arena arena = plugin.getArenaManager().getSetupArena(arenaName);
        arena.setLobbySpawn(player.getLocation());
        
        player.sendMessage(MessageUtil.color("&aLobby spawn point set for arena &e" + arenaName));
    }
    
    private void handleSetBed(Player player, String teamStr) {
        String arenaName = setupMode.get(player.getName());
        if (arenaName == null) {
            player.sendMessage(MessageUtil.color("&cYou must start setup first! Use /bwa setup <arena>"));
            return;
        }
        
        TeamColor team;
        try {
            team = TeamColor.valueOf(teamStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtil.color("&cInvalid team! Use: RED, BLUE, GREEN, YELLOW"));
            return;
        }
        
        Arena arena = plugin.getArenaManager().getSetupArena(arenaName);
        TeamData teamData = arena.getTeam(team);
        
        if (teamData == null) {
            teamData = new TeamData(team);
            arena.addTeam(team, teamData);
        }
        
        teamData.setBedLocation(player.getLocation());
        player.sendMessage(MessageUtil.color("&aBed location set for team &e" + team.getColoredName()));
    }
    
    private void handleSetSpawn(Player player, String teamStr) {
        String arenaName = setupMode.get(player.getName());
        if (arenaName == null) {
            player.sendMessage(MessageUtil.color("&cYou must start setup first! Use /bwa setup <arena>"));
            return;
        }
        
        TeamColor team;
        try {
            team = TeamColor.valueOf(teamStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtil.color("&cInvalid team! Use: RED, BLUE, GREEN, YELLOW"));
            return;
        }
        
        Arena arena = plugin.getArenaManager().getSetupArena(arenaName);
        TeamData teamData = arena.getTeam(team);
        
        if (teamData == null) {
            teamData = new TeamData(team);
            arena.addTeam(team, teamData);
        }
        
        teamData.setSpawnLocation(player.getLocation());
        player.sendMessage(MessageUtil.color("&aSpawn point set for team &e" + team.getColoredName()));
    }
    
    private void handleSetResource(Player player, String[] args) {
        String arenaName = setupMode.get(player.getName());
        if (arenaName == null) {
            player.sendMessage(MessageUtil.color("&cYou must start setup first! Use /bwa setup <arena>"));
            return;
        }
        
        ResourceSpawner.ResourceType type;
        try {
            type = ResourceSpawner.ResourceType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(MessageUtil.color("&cInvalid resource type! Use: IRON, GOLD, DIAMOND, EMERALD"));
            return;
        }
        
        TeamColor team = null;
        if (args.length > 2) {
            try {
                team = TeamColor.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(MessageUtil.color("&cInvalid team! Use: RED, BLUE, GREEN, YELLOW"));
                return;
            }
        }
        
        Arena arena = plugin.getArenaManager().getSetupArena(arenaName);
        Location loc = player.getLocation();
        
        ResourceSpawner spawner = new ResourceSpawner(loc, type, team);
        arena.addResourceSpawner(spawner);
        
        String teamMsg = team != null ? " for team " + team.getColoredName() : " (shared)";
        player.sendMessage(MessageUtil.color("&a" + type.name() + " spawner added" + teamMsg));
    }
    
    private void handleSaveArena(Player player) {
        String arenaName = setupMode.get(player.getName());
        if (arenaName == null) {
            player.sendMessage(MessageUtil.color("&cYou must start setup first! Use /bwa setup <arena>"));
            return;
        }
        
        Arena arena = plugin.getArenaManager().getSetupArena(arenaName);
        
        if (!arena.isComplete()) {
            player.sendMessage(MessageUtil.color("&cArena is not complete! You must set:"));
            if (arena.getLobbySpawn() == null) {
                player.sendMessage(MessageUtil.color("&c- Lobby spawn point"));
            }
            if (arena.getTeams().isEmpty()) {
                player.sendMessage(MessageUtil.color("&c- At least one team with bed and spawn"));
            } else {
                for (Map.Entry<TeamColor, TeamData> entry : arena.getTeams().entrySet()) {
                    if (!entry.getValue().isComplete()) {
                        player.sendMessage(MessageUtil.color("&c- Complete setup for team " + 
                            entry.getKey().getColoredName()));
                    }
                }
            }
            return;
        }
        
        plugin.getArenaManager().finishSetup(arenaName);
        setupMode.remove(player.getName());
        
        player.sendMessage(MessageUtil.color("&aArena &e" + arenaName + " &asaved and enabled!"));
    }
    
    private void handleDeleteArena(Player player, String arenaName) {
        if (!plugin.getArenaManager().arenaExists(arenaName)) {
            player.sendMessage(MessageUtil.color("&cArena not found!"));
            return;
        }
        
        plugin.getArenaManager().deleteArena(arenaName);
        setupMode.remove(player.getName());
        
        player.sendMessage(MessageUtil.color("&aArena &e" + arenaName + " &adeleted!"));
    }
    
    private void handleListArenas(Player player) {
        player.sendMessage(MessageUtil.color("&7&m                                    "));
        player.sendMessage(MessageUtil.color("&c&lAll Arenas"));
        
        if (plugin.getArenaManager().getArenas().isEmpty()) {
            player.sendMessage(MessageUtil.color("&7No arenas configured"));
        } else {
            for (Arena arena : plugin.getArenaManager().getArenas()) {
                String status = arena.isEnabled() ? "&aEnabled" : "&cDisabled";
                String complete = arena.isComplete() ? "&aComplete" : "&eIncomplete";
                
                player.sendMessage(MessageUtil.color("&e" + arena.getName() + " &7- " + 
                    status + " &7- " + complete));
            }
        }
        
        player.sendMessage(MessageUtil.color("&7&m                                    "));
    }
    
    private void handleEnableArena(Player player, String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(MessageUtil.color("&cArena not found!"));
            return;
        }
        
        if (!arena.isComplete()) {
            player.sendMessage(MessageUtil.color("&cCannot enable incomplete arena!"));
            return;
        }
        
        arena.setEnabled(true);
        plugin.getArenaManager().saveArena(arena);
        
        player.sendMessage(MessageUtil.color("&aArena &e" + arenaName + " &aenabled!"));
    }
    
    private void handleDisableArena(Player player, String arenaName) {
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null) {
            player.sendMessage(MessageUtil.color("&cArena not found!"));
            return;
        }
        
        arena.setEnabled(false);
        plugin.getArenaManager().saveArena(arena);
        
        player.sendMessage(MessageUtil.color("&aArena &e" + arenaName + " &adisabled!"));
    }
}
