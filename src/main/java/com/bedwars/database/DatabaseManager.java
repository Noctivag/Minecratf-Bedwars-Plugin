package com.bedwars.database;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.PlayerStats;

import java.io.File;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class DatabaseManager {

    private final BedwarsPlugin plugin;
    private Connection connection;
    private final String databasePath;

    public DatabaseManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.databasePath = plugin.getDataFolder().getAbsolutePath() + File.separator + "bedwars.db";
    }

    /**
     * Initialize the database connection and create tables
     */
    public void initialize() {
        try {
            // Create plugin data folder if it doesn't exist
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);

            // Create tables
            createTables();

            plugin.getLogger().info("Database connected successfully!");

        } catch (ClassNotFoundException | SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database!", e);
        }
    }

    /**
     * Create all necessary database tables
     */
    private void createTables() throws SQLException {
        String playerStatsTable = """
            CREATE TABLE IF NOT EXISTS player_stats (
                uuid TEXT PRIMARY KEY,
                player_name TEXT NOT NULL,
                kills INTEGER DEFAULT 0,
                deaths INTEGER DEFAULT 0,
                final_kills INTEGER DEFAULT 0,
                final_deaths INTEGER DEFAULT 0,
                beds_destroyed INTEGER DEFAULT 0,
                wins INTEGER DEFAULT 0,
                losses INTEGER DEFAULT 0,
                games_played INTEGER DEFAULT 0,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(playerStatsTable);
        }

        plugin.getLogger().info("Database tables created successfully!");
    }

    /**
     * Load player statistics from database asynchronously
     */
    public CompletableFuture<PlayerStats> loadPlayerStats(UUID uuid, String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM player_stats WHERE uuid = ?";

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Player exists, load their stats
                    PlayerStats stats = new PlayerStats();
                    stats.setKills(rs.getInt("kills"));
                    stats.setDeaths(rs.getInt("deaths"));
                    stats.setFinalKills(rs.getInt("final_kills"));
                    stats.setFinalDeaths(rs.getInt("final_deaths"));
                    stats.setBedsDestroyed(rs.getInt("beds_destroyed"));
                    stats.setWins(rs.getInt("wins"));
                    stats.setLosses(rs.getInt("losses"));
                    stats.setGamesPlayed(rs.getInt("games_played"));
                    return stats;
                } else {
                    // New player, create entry
                    createPlayerEntry(uuid, playerName);
                    return new PlayerStats();
                }

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load player stats for " + uuid, e);
                return new PlayerStats();
            }
        });
    }

    /**
     * Save player statistics to database asynchronously
     */
    public CompletableFuture<Void> savePlayerStats(UUID uuid, String playerName, PlayerStats stats) {
        return CompletableFuture.runAsync(() -> {
            String query = """
                INSERT INTO player_stats (uuid, player_name, kills, deaths, final_kills, final_deaths,
                                         beds_destroyed, wins, losses, games_played, last_updated)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                ON CONFLICT(uuid) DO UPDATE SET
                    player_name = excluded.player_name,
                    kills = excluded.kills,
                    deaths = excluded.deaths,
                    final_kills = excluded.final_kills,
                    final_deaths = excluded.final_deaths,
                    beds_destroyed = excluded.beds_destroyed,
                    wins = excluded.wins,
                    losses = excluded.losses,
                    games_played = excluded.games_played,
                    last_updated = excluded.last_updated
            """;

            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, playerName);
                stmt.setInt(3, stats.getKills());
                stmt.setInt(4, stats.getDeaths());
                stmt.setInt(5, stats.getFinalKills());
                stmt.setInt(6, stats.getFinalDeaths());
                stmt.setInt(7, stats.getBedsDestroyed());
                stmt.setInt(8, stats.getWins());
                stmt.setInt(9, stats.getLosses());
                stmt.setInt(10, stats.getGamesPlayed());

                stmt.executeUpdate();

            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save player stats for " + uuid, e);
            }
        });
    }

    /**
     * Create a new player entry in the database
     */
    private void createPlayerEntry(UUID uuid, String playerName) throws SQLException {
        String query = "INSERT INTO player_stats (uuid, player_name) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, playerName);
            stmt.executeUpdate();
        }
    }

    /**
     * Get top players by a specific stat
     */
    public CompletableFuture<ResultSet> getTopPlayers(String statColumn, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT uuid, player_name, " + statColumn +
                          " FROM player_stats ORDER BY " + statColumn + " DESC LIMIT ?";

            try {
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, limit);
                return stmt.executeQuery();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to get top players", e);
                return null;
            }
        });
    }

    /**
     * Close the database connection
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database connection closed!");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close database connection!", e);
        }
    }

    /**
     * Check if database is connected
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
