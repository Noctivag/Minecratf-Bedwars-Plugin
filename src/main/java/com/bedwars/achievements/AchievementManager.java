package com.bedwars.achievements;

import com.bedwars.BedwarsPlugin;
import com.bedwars.achievements.Achievement.AchievementCategory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class AchievementManager {

    private final BedwarsPlugin plugin;
    private final Map<String, Achievement> achievements;
    private final Map<UUID, Map<String, Integer>> playerProgress;

    public AchievementManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.achievements = new HashMap<>();
        this.playerProgress = new HashMap<>();
        registerAchievements();
        createTable();
    }

    /**
     * Register all achievements
     */
    private void registerAchievements() {
        // GENERAL
        register(new Achievement("first_game", "First Game", "Play your first game",
            Material.GRASS_BLOCK, AchievementCategory.GENERAL, 1, 10));

        register(new Achievement("games_10", "Dedicated Player", "Play 10 games",
            Material.IRON_INGOT, AchievementCategory.GENERAL, 10, 50));

        register(new Achievement("games_100", "Veteran", "Play 100 games",
            Material.DIAMOND, AchievementCategory.GENERAL, 100, 500));

        register(new Achievement("first_win", "First Victory", "Win your first game",
            Material.GOLD_INGOT, AchievementCategory.GENERAL, 1, 25));

        register(new Achievement("wins_10", "Winning Streak", "Win 10 games",
            Material.EMERALD, AchievementCategory.GENERAL, 10, 100));

        register(new Achievement("wins_100", "Champion", "Win 100 games",
            Material.BEACON, AchievementCategory.GENERAL, 100, 1000));

        // COMBAT
        register(new Achievement("first_kill", "First Blood", "Get your first kill",
            Material.WOODEN_SWORD, AchievementCategory.COMBAT, 1, 10));

        register(new Achievement("kills_100", "Slayer", "Get 100 kills",
            Material.IRON_SWORD, AchievementCategory.COMBAT, 100, 100));

        register(new Achievement("kills_1000", "Assassin", "Get 1000 kills",
            Material.DIAMOND_SWORD, AchievementCategory.COMBAT, 1000, 1000));

        register(new Achievement("final_kills_10", "Finisher", "Get 10 final kills",
            Material.GOLDEN_SWORD, AchievementCategory.COMBAT, 10, 75));

        register(new Achievement("final_kills_100", "Executioner", "Get 100 final kills",
            Material.NETHERITE_SWORD, AchievementCategory.COMBAT, 100, 750));

        register(new Achievement("beds_destroyed_1", "Bed Breaker", "Destroy your first bed",
            Material.RED_BED, AchievementCategory.TEAMWORK, 1, 20));

        register(new Achievement("beds_destroyed_10", "Demolition Expert", "Destroy 10 beds",
            Material.TNT, AchievementCategory.TEAMWORK, 10, 150));

        register(new Achievement("beds_destroyed_100", "Bed Destroyer", "Destroy 100 beds",
            Material.OBSIDIAN, AchievementCategory.TEAMWORK, 100, 1500));

        // MASTERY
        register(new Achievement("flawless_win", "Flawless Victory", "Win without dying",
            Material.TOTEM_OF_UNDYING, AchievementCategory.MASTERY, 1, 200));

        register(new Achievement("team_wipe", "Team Wipe", "Eliminate all enemy teams in one game",
            Material.WITHER_SKELETON_SKULL, AchievementCategory.MASTERY, 1, 250));
    }

    /**
     * Register an achievement
     */
    private void register(Achievement achievement) {
        achievements.put(achievement.getId(), achievement);
    }

    /**
     * Create achievements table in database
     */
    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_achievements (
                uuid TEXT NOT NULL,
                achievement_id TEXT NOT NULL,
                progress INTEGER DEFAULT 0,
                completed BOOLEAN DEFAULT 0,
                completed_at TIMESTAMP,
                PRIMARY KEY (uuid, achievement_id)
            )
        """;

        CompletableFuture.runAsync(() -> {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.execute();
                plugin.getLogger().info("Achievements table created successfully!");
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create achievements table!", e);
            }
        });
    }

    /**
     * Load player achievements from database
     */
    public CompletableFuture<Void> loadPlayerAchievements(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Integer> progress = new HashMap<>();

            String sql = "SELECT achievement_id, progress FROM player_achievements WHERE uuid = ?";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, uuid.toString());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    progress.put(rs.getString("achievement_id"), rs.getInt("progress"));
                }

                playerProgress.put(uuid, progress);
                plugin.getLogger().info("Loaded achievements for player: " + uuid);

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load achievements for " + uuid, e);
            }
        });
    }

    /**
     * Save player achievements to database
     */
    public CompletableFuture<Void> savePlayerAchievements(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Integer> progress = playerProgress.get(uuid);
            if (progress == null) {
                return;
            }

            String sql = """
                INSERT INTO player_achievements (uuid, achievement_id, progress, completed)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(uuid, achievement_id) DO UPDATE SET
                    progress = excluded.progress,
                    completed = excluded.completed
            """;

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                for (Map.Entry<String, Integer> entry : progress.entrySet()) {
                    Achievement achievement = achievements.get(entry.getKey());
                    if (achievement == null) continue;

                    stmt.setString(1, uuid.toString());
                    stmt.setString(2, entry.getKey());
                    stmt.setInt(3, entry.getValue());
                    stmt.setBoolean(4, entry.getValue() >= achievement.getTargetValue());
                    stmt.addBatch();
                }

                stmt.executeBatch();

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save achievements for " + uuid, e);
            }
        });
    }

    /**
     * Increment achievement progress
     */
    public void incrementProgress(Player player, String achievementId, int amount) {
        UUID uuid = player.getUniqueId();
        Achievement achievement = achievements.get(achievementId);

        if (achievement == null) {
            return;
        }

        Map<String, Integer> progress = playerProgress.computeIfAbsent(uuid, k -> new HashMap<>());
        int currentProgress = progress.getOrDefault(achievementId, 0);
        int newProgress = currentProgress + amount;

        progress.put(achievementId, newProgress);

        // Check if achievement completed
        if (currentProgress < achievement.getTargetValue() && newProgress >= achievement.getTargetValue()) {
            completeAchievement(player, achievement);
        }
    }

    /**
     * Complete an achievement
     */
    private void completeAchievement(Player player, Achievement achievement) {
        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        player.sendMessage("§e§lACHIEVEMENT UNLOCKED!");
        player.sendMessage("");
        player.sendMessage("§a§l" + achievement.getName());
        player.sendMessage("§7" + achievement.getDescription());
        player.sendMessage("§6§l+ " + achievement.getRewardCoins() + " Coins");
        player.sendMessage("§6§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Play sound and effects
        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

        // Save to database
        savePlayerAchievements(player.getUniqueId());
    }

    /**
     * Get player progress for an achievement
     */
    public int getProgress(UUID uuid, String achievementId) {
        return playerProgress
            .getOrDefault(uuid, new HashMap<>())
            .getOrDefault(achievementId, 0);
    }

    /**
     * Check if player has completed achievement
     */
    public boolean hasCompleted(UUID uuid, String achievementId) {
        Achievement achievement = achievements.get(achievementId);
        if (achievement == null) return false;

        int progress = getProgress(uuid, achievementId);
        return progress >= achievement.getTargetValue();
    }

    /**
     * Open achievements GUI for a player
     */
    public void openAchievementsGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§8§lAchievements");

        int slot = 0;
        for (Achievement achievement : achievements.values()) {
            if (slot >= 54) break;

            inv.setItem(slot++, createAchievementItem(player, achievement));
        }

        player.openInventory(inv);
    }

    /**
     * Create achievement item for GUI
     */
    private ItemStack createAchievementItem(Player player, Achievement achievement) {
        UUID uuid = player.getUniqueId();
        int progress = getProgress(uuid, achievement.getId());
        boolean completed = hasCompleted(uuid, achievement.getId());

        ItemStack item = new ItemStack(completed ? achievement.getIcon() : Material.GRAY_DYE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName((completed ? "§a§l" : "§7§l") + achievement.getName());

            List<String> lore = new ArrayList<>();
            lore.add("§7" + achievement.getDescription());
            lore.add("");
            lore.add("§7Progress: §e" + progress + "§7/§e" + achievement.getTargetValue());
            lore.add("§7Reward: §6" + achievement.getRewardCoins() + " Coins");
            lore.add("");

            if (completed) {
                lore.add("§a§lCOMPLETED!");
            } else {
                int percentage = (int) ((progress / (double) achievement.getTargetValue()) * 100);
                lore.add("§e" + percentage + "% Complete");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Get database connection
     */
    private Connection getConnection() throws SQLException {
        String databasePath = plugin.getDataFolder().getAbsolutePath() + "/bedwars.db";
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    /**
     * Get all achievements
     */
    public Collection<Achievement> getAllAchievements() {
        return achievements.values();
    }

    /**
     * Unload player achievements
     */
    public void unloadPlayer(UUID uuid) {
        savePlayerAchievements(uuid);
        playerProgress.remove(uuid);
    }
}
