package com.bedwars;

import com.bedwars.commands.BedwarsCommand;
import com.bedwars.commands.BedwarsAdminCommand;
import com.bedwars.listeners.GameListener;
import com.bedwars.listeners.PlayerListener;
import com.bedwars.listeners.GUIListener;
import com.bedwars.managers.ArenaManager;
import com.bedwars.managers.GameManager;
import com.bedwars.managers.ScoreboardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedwarsPlugin extends JavaPlugin {
    
    private static BedwarsPlugin instance;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;
    private GUIListener guiListener;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Initialize managers
        arenaManager = new ArenaManager(this);
        gameManager = new GameManager(this);
        scoreboardManager = new ScoreboardManager(this);
        
        // Register commands
        getCommand("bedwars").setExecutor(new BedwarsCommand(this));
        getCommand("bwadmin").setExecutor(new BedwarsAdminCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new com.bedwars.listeners.ShopListener(this), this);
        
        // Register GUI listener
        guiListener = new GUIListener(this);
        getServer().getPluginManager().registerEvents(guiListener, this);
        
        getLogger().info("Bedwars Plugin has been enabled!");
        getLogger().info("Game modes available: SOLO, DOUBLES, THREES, FOURS, MEGA_DOUBLES, MEGA_FOURS");
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
    
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    
    public GUIListener getGUIListener() {
        return guiListener;
    }
}
