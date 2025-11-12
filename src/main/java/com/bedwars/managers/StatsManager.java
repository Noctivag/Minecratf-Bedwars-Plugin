package com.bedwars.managers;

import com.bedwars.BedwarsPlugin;
import com.bedwars.database.DatabaseManager;
import com.bedwars.models.PlayerStats;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsManager {

    private final BedwarsPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerStats> playerStatsCache;

    public StatsManager(BedwarsPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerStatsCache = new HashMap<>();
    }

    /**
     * Load player stats from database and cache them
     */
    public void loadPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        databaseManager.loadPlayerStats(uuid, name).thenAccept(stats -> {
            playerStatsCache.put(uuid, stats);
            plugin.getLogger().info("Loaded stats for player: " + name);
        }).exceptionally(ex -> {
            plugin.getLogger().severe("Failed to load stats for " + name + ": " + ex.getMessage());
            playerStatsCache.put(uuid, new PlayerStats());
            return null;
        });
    }

    /**
     * Save player stats to database
     */
    public void savePlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName();
        PlayerStats stats = playerStatsCache.get(uuid);

        if (stats != null) {
            databaseManager.savePlayerStats(uuid, name, stats).thenRun(() -> {
                plugin.getLogger().info("Saved stats for player: " + name);
            }).exceptionally(ex -> {
                plugin.getLogger().severe("Failed to save stats for " + name + ": " + ex.getMessage());
                return null;
            });
        }
    }

    /**
     * Save all cached player stats
     */
    public void saveAllStats() {
        for (Map.Entry<UUID, PlayerStats> entry : playerStatsCache.entrySet()) {
            UUID uuid = entry.getKey();
            PlayerStats stats = entry.getValue();
            Player player = plugin.getServer().getPlayer(uuid);

            if (player != null) {
                databaseManager.savePlayerStats(uuid, player.getName(), stats);
            }
        }
        plugin.getLogger().info("Saved all player stats to database!");
    }

    /**
     * Get player stats from cache
     */
    public PlayerStats getPlayerStats(UUID uuid) {
        return playerStatsCache.computeIfAbsent(uuid, k -> new PlayerStats());
    }

    /**
     * Get player stats from cache by Player object
     */
    public PlayerStats getPlayerStats(Player player) {
        return getPlayerStats(player.getUniqueId());
    }

    /**
     * Remove player stats from cache (call when player leaves)
     */
    public void unloadPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        savePlayerStats(player);
        playerStatsCache.remove(uuid);
    }

    /**
     * Clear all cached stats
     */
    public void clearCache() {
        playerStatsCache.clear();
    }
}
