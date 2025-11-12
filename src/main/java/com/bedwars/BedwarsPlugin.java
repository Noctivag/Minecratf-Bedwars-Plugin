package com.bedwars;

import com.bedwars.commands.BedwarsCommand;
import com.bedwars.commands.BedwarsAdminCommand;
import com.bedwars.database.DatabaseManager;
import com.bedwars.items.SpecialItemHandler;
import com.bedwars.listeners.GameListener;
import com.bedwars.listeners.PlayerListener;
import com.bedwars.listeners.GUIListener;
import com.bedwars.listeners.UpgradeVillagerListener;
import com.bedwars.managers.ArenaManager;
import com.bedwars.managers.GameManager;
import com.bedwars.managers.ScoreboardManager;
import com.bedwars.managers.StatsManager;
import com.bedwars.trackers.CompassTracker;
import com.bedwars.upgrades.GeneratorUpgradeManager;
import com.bedwars.achievements.AchievementManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BedwarsPlugin extends JavaPlugin {

    private static BedwarsPlugin instance;
    private DatabaseManager databaseManager;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;
    private StatsManager statsManager;
    private SpecialItemHandler specialItemHandler;
    private CompassTracker compassTracker;
    private GeneratorUpgradeManager generatorUpgradeManager;
    private AchievementManager achievementManager;
    private GUIListener guiListener;
    
    @Override
    public void onEnable() {
        instance = this;

        // Save default config
        saveDefaultConfig();

        // Initialize database
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();

        // Initialize managers
        statsManager = new StatsManager(this, databaseManager);
        achievementManager = new AchievementManager(this);
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
        getServer().getPluginManager().registerEvents(new UpgradeVillagerListener(this), this);

        // Register special item handler
        specialItemHandler = new SpecialItemHandler(this);
        getServer().getPluginManager().registerEvents(specialItemHandler, this);

        // Register compass tracker
        compassTracker = new CompassTracker(this);
        getServer().getPluginManager().registerEvents(compassTracker, this);

        // Register generator upgrade manager
        generatorUpgradeManager = new GeneratorUpgradeManager(this);
        getServer().getPluginManager().registerEvents(generatorUpgradeManager, this);

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

        // Save all player stats
        if (statsManager != null) {
            statsManager.saveAllStats();
        }

        // Cleanup special items
        if (specialItemHandler != null) {
            specialItemHandler.cleanup();
        }

        // Shutdown compass tracker
        if (compassTracker != null) {
            compassTracker.shutdown();
        }

        // Close database connection
        if (databaseManager != null) {
            databaseManager.close();
        }

        getLogger().info("Bedwars Plugin has been disabled!");
    }
    
    public static BedwarsPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
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

    public CompassTracker getCompassTracker() {
        return compassTracker;
    }

    public GeneratorUpgradeManager getGeneratorUpgradeManager() {
        return generatorUpgradeManager;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }
}
