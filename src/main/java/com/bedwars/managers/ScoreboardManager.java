package com.bedwars.managers;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import com.bedwars.models.TeamColor;
import com.bedwars.models.TeamData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardManager {
    
    private final BedwarsPlugin plugin;
    private final Map<Player, Scoreboard> playerScoreboards;
    
    public ScoreboardManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.playerScoreboards = new HashMap<>();
    }
    
    public void updateScoreboard(Player player, BedwarsGame game) {
        Scoreboard scoreboard = playerScoreboards.get(player);
        
        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            playerScoreboards.put(player, scoreboard);
            player.setScoreboard(scoreboard);
        }
        
        Objective objective = scoreboard.getObjective("bedwars");
        if (objective != null) {
            objective.unregister();
        }
        
        objective = scoreboard.registerNewObjective("bedwars", "dummy", 
            ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        if (game.getState() == GameState.WAITING || game.getState() == GameState.STARTING) {
            updateLobbyScoreboard(objective, game);
        } else if (game.getState() == GameState.ACTIVE) {
            updateGameScoreboard(objective, game, player);
        }
    }
    
    private void updateLobbyScoreboard(Objective objective, BedwarsGame game) {
        int line = 10;
        
        setScore(objective, ChatColor.GRAY + "Waiting for players...", line--);
        setScore(objective, "", line--);
        setScore(objective, ChatColor.WHITE + "Players: " + ChatColor.GREEN + 
            game.getTotalPlayers() + "/" + game.getArena().getMaxPlayers(), line--);
        setScore(objective, ChatColor.WHITE + "Map: " + ChatColor.GREEN + 
            game.getArena().getDisplayName(), line--);
        setScore(objective, "  ", line--);
        
        if (game.getState() == GameState.STARTING) {
            setScore(objective, ChatColor.YELLOW + "Starting in...", line--);
        } else {
            setScore(objective, ChatColor.YELLOW + "Need " + 
                (game.getArena().getMinPlayers() - game.getTotalPlayers()) + " more", line--);
        }
    }
    
    private void updateGameScoreboard(Objective objective, BedwarsGame game, Player player) {
        int line = 15;
        
        TeamColor playerTeam = game.getPlayerTeam(player);
        
        setScore(objective, ChatColor.GRAY + "minecraft.net", line--);
        setScore(objective, "", line--);
        
        // Show all teams
        for (TeamColor teamColor : game.getArena().getTeams().keySet()) {
            TeamData teamData = game.getArena().getTeam(teamColor);
            
            String bedStatus;
            if (teamData.isBedAlive()) {
                bedStatus = ChatColor.GREEN + "✓";
            } else {
                bedStatus = ChatColor.RED + "✗";
            }
            
            int teamPlayers = game.getTeamPlayers(teamColor).size();
            String playerCount = teamPlayers > 0 ? 
                ChatColor.GREEN + "" + teamPlayers : ChatColor.GRAY + "0";
            
            String teamIndicator = teamColor == playerTeam ? ChatColor.WHITE + "YOU" : "";
            
            setScore(objective, teamColor.getChatColor() + teamColor.getDisplayName().substring(0, 1) + " " +
                teamColor.getChatColor() + teamColor.getDisplayName() + " " + bedStatus + " " + 
                playerCount + "  " + teamIndicator, line--);
        }
        
        setScore(objective, "  ", line--);
        setScore(objective, ChatColor.YELLOW + "Kills: " + ChatColor.WHITE + 
            plugin.getGameManager().getPlayerStats(player).getGameKills(), line--);
        setScore(objective, ChatColor.YELLOW + "Final Kills: " + ChatColor.WHITE + 
            plugin.getGameManager().getPlayerStats(player).getGameFinalKills(), line--);
        setScore(objective, ChatColor.YELLOW + "Beds Broken: " + ChatColor.WHITE + 
            plugin.getGameManager().getPlayerStats(player).getGameBedsDestroyed(), line--);
    }
    
    private void setScore(Objective objective, String text, int score) {
        Score s = objective.getScore(text);
        s.setScore(score);
    }
    
    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
    
    public void clearAll() {
        for (Player player : playerScoreboards.keySet()) {
            removeScoreboard(player);
        }
        playerScoreboards.clear();
    }
}
