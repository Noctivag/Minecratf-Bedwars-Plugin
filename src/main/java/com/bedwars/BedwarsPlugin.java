package com.bedwars;

import com.bedwars.commands.BedwarsCommand;
import com.bedwars.commands.BedwarsAdminCommand;
import com.bedwars.listeners.GameListener;
import com.bedwars.listeners.PlayerListener;
import com.bedwars.managers.ArenaManager;
import com.bedwars.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedwarsPlugin extends JavaPlugin {
    
    private static BedwarsPlugin instance;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        arenaManager = new ArenaManager(this);
        gameManager = new GameManager(this);
        
        // Register commands
        getCommand("bedwars").setExecutor(new BedwarsCommand(this));
        getCommand("bwadmin").setExecutor(new BedwarsAdminCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        getLogger().info("Bedwars Plugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        // Stop all games
        if (gameManager != null) {
            gameManager.stopAllGames();
        }
        
        getLogger().info("Bedwars Plugin has been disabled!");
    }
    
    public static BedwarsPlugin getInstance() {
        return instance;
    }
    
    public ArenaManager getArenaManager() {
        return arenaManager;
    }
    
    public GameManager getGameManager() {
        return gameManager;
    }
}
