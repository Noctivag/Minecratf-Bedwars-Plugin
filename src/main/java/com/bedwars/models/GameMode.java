package com.bedwars.models;

/**
 * Represents different Bedwars game modes like Hypixel
 */
public enum GameMode {
    
    SOLO("Solo", 1, 8, 8),           // 1v1v1v1v1v1v1v1 (8 teams, 1 player each)
    DOUBLES("Doubles", 2, 8, 16),     // 2v2v2v2v2v2v2v2 (8 teams, 2 players each)
    THREES("3v3v3v3", 3, 4, 12),      // 3v3v3v3 (4 teams, 3 players each)
    FOURS("4v4v4v4", 4, 4, 16),       // 4v4v4v4 (4 teams, 4 players each)
    MEGA_DOUBLES("Mega Doubles", 2, 16, 32),  // 2v2v2v2... (16 teams, 2 players each)
    MEGA_FOURS("Mega 4v4", 4, 8, 32); // 4v4v4v4... (8 teams, 4 players each)
    
    private final String displayName;
    private final int teamSize;
    private final int maxTeams;
    private final int maxPlayers;
    
    GameMode(String displayName, int teamSize, int maxTeams, int maxPlayers) {
        this.displayName = displayName;
        this.teamSize = teamSize;
        this.maxTeams = maxTeams;
        this.maxPlayers = maxPlayers;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getTeamSize() {
        return teamSize;
    }
    
    public int getMaxTeams() {
        return maxTeams;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public int getMinPlayers() {
        // Minimum 50% of teams with at least 1 player each
        return Math.max(2, maxTeams / 2);
    }
    
    public String getDescription() {
        return String.format("%s (%d players per team, max %d teams, %d players total)",
            displayName, teamSize, maxTeams, maxPlayers);
    }
    
    public static GameMode fromString(String name) {
        for (GameMode mode : values()) {
            if (mode.name().equalsIgnoreCase(name) || 
                mode.displayName.equalsIgnoreCase(name)) {
                return mode;
            }
        }
        return null;
    }
}
