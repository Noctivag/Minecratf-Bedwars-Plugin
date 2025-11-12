package com.bedwars.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStats {
    
    private int kills = 0;
    private int deaths = 0;
    private int finalKills = 0;
    private int finalDeaths = 0;
    private int bedsDestroyed = 0;
    private int wins = 0;
    private int losses = 0;
    private int gamesPlayed = 0;
    
    // In-game stats (reset each game)
    private transient int gameKills = 0;
    private transient int gameFinalKills = 0;
    private transient int gameBedsDestroyed = 0;
    
    public void addKill() { kills++; gameKills++; }
    public void addDeath() { deaths++; }
    public void addFinalKill() { finalKills++; gameFinalKills++; }
    public void addFinalDeath() { finalDeaths++; }
    public void addBedDestroyed() { bedsDestroyed++; gameBedsDestroyed++; }
    public void addWin() { wins++; }
    public void addLoss() { losses++; }
    public void addGame() { gamesPlayed++; }
    
    public void resetGameStats() {
        gameKills = 0;
        gameFinalKills = 0;
        gameBedsDestroyed = 0;
    }
    
    // Getters
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public int getFinalKills() { return finalKills; }
    public int getFinalDeaths() { return finalDeaths; }
    public int getBedsDestroyed() { return bedsDestroyed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getGamesPlayed() { return gamesPlayed; }

    public int getGameKills() { return gameKills; }
    public int getGameFinalKills() { return gameFinalKills; }
    public int getGameBedsDestroyed() { return gameBedsDestroyed; }

    // Setters (for database loading)
    public void setKills(int kills) { this.kills = kills; }
    public void setDeaths(int deaths) { this.deaths = deaths; }
    public void setFinalKills(int finalKills) { this.finalKills = finalKills; }
    public void setFinalDeaths(int finalDeaths) { this.finalDeaths = finalDeaths; }
    public void setBedsDestroyed(int bedsDestroyed) { this.bedsDestroyed = bedsDestroyed; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    
    public double getKDRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }
    
    public double getWinRate() {
        return gamesPlayed == 0 ? 0 : (double) wins / gamesPlayed * 100;
    }
}
