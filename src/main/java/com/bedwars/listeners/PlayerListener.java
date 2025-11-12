package com.bedwars.listeners;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final BedwarsPlugin plugin;

    public PlayerListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player stats from database
        plugin.getStatsManager().loadPlayerStats(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove player from game if they're in one
        BedwarsGame game = plugin.getGameManager().getPlayerGame(event.getPlayer());
        if (game != null) {
            plugin.getGameManager().leaveGame(event.getPlayer());
        }

        // Save and unload player stats
        plugin.getStatsManager().unloadPlayerStats(event.getPlayer());
    }
}
