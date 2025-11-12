package com.bedwars.managers;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.Arena;
import com.bedwars.models.PlayerStats;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private final BedwarsPlugin plugin;
    private final Map<String, BedwarsGame> games; // arena name -> game
    private final Map<UUID, BedwarsGame> playerGames; // player -> game

    public GameManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.games = new HashMap<>();
        this.playerGames = new HashMap<>();
    }
    
    public BedwarsGame createGame(Arena arena) {
        if (games.containsKey(arena.getName())) {
            return games.get(arena.getName());
        }
        
        BedwarsGame game = new BedwarsGame(plugin, arena);
        games.put(arena.getName(), game);
        return game;
    }
    
    public boolean joinGame(Player player, String arenaName) {
        // Check if player is already in a game
        if (playerGames.containsKey(player.getUniqueId())) {
            return false;
        }
        
        // Get or create game
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null || !arena.isEnabled()) {
            return false;
        }
        
        BedwarsGame game = games.get(arenaName);
        if (game == null) {
            game = createGame(arena);
        }
        
        if (game.addPlayer(player)) {
            playerGames.put(player.getUniqueId(), game);
            return true;
        }
        
        return false;
    }
    
    public boolean leaveGame(Player player) {
        BedwarsGame game = playerGames.remove(player.getUniqueId());
        if (game != null) {
            game.removePlayer(player);
            return true;
        }
        return false;
    }
    
    public BedwarsGame getPlayerGame(Player player) {
        return playerGames.get(player.getUniqueId());
    }
    
    public boolean isInGame(Player player) {
        return playerGames.containsKey(player.getUniqueId());
    }
    
    public boolean hasGame(String arenaName) {
        return games.containsKey(arenaName);
    }
    
    public BedwarsGame getGame(String arenaName) {
        return games.get(arenaName);
    }
    
    public void endGame(BedwarsGame game) {
        // Remove all players from tracking
        for (Player player : game.getAllPlayers()) {
            playerGames.remove(player.getUniqueId());
        }
        
        // Remove game
        games.remove(game.getArena().getName());
    }
    
    public void stopAllGames() {
        for (BedwarsGame game : new ArrayList<>(games.values())) {
            game.forceEnd();
        }
        games.clear();
        playerGames.clear();
    }
    
    public Collection<BedwarsGame> getGames() {
        return games.values();
    }
    
    public PlayerStats getPlayerStats(Player player) {
        return plugin.getStatsManager().getPlayerStats(player);
    }
}
