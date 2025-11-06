package com.bedwars.models;

import java.util.HashMap;
import java.util.Map;

public class TeamUpgrades {
    
    private int sharpnessLevel = 0;
    private int protectionLevel = 0;
    private int hasteLevel = 0;
    private boolean healPool = false;
    private boolean trap = false;
    
    // Track individual player upgrades
    private Map<String, PlayerUpgrades> playerUpgrades = new HashMap<>();
    
    public static class PlayerUpgrades {
        private boolean armor = false;
        
        public boolean hasArmor() { return armor; }
        public void setArmor(boolean armor) { this.armor = armor; }
    }
    
    public int getSharpnessLevel() { return sharpnessLevel; }
    public void setSharpnessLevel(int level) { this.sharpnessLevel = Math.min(level, 4); }
    public void upgradeSharpness() { sharpnessLevel = Math.min(sharpnessLevel + 1, 4); }
    
    public int getProtectionLevel() { return protectionLevel; }
    public void setProtectionLevel(int level) { this.protectionLevel = Math.min(level, 4); }
    public void upgradeProtection() { protectionLevel = Math.min(protectionLevel + 1, 4); }
    
    public int getHasteLevel() { return hasteLevel; }
    public void setHasteLevel(int level) { this.hasteLevel = Math.min(level, 2); }
    public void upgradeHaste() { hasteLevel = Math.min(hasteLevel + 1, 2); }
    
    public boolean hasHealPool() { return healPool; }
    public void setHealPool(boolean healPool) { this.healPool = healPool; }
    
    public boolean hasTrap() { return trap; }
    public void setTrap(boolean trap) { this.trap = trap; }
    
    public PlayerUpgrades getPlayerUpgrades(String playerName) {
        return playerUpgrades.computeIfAbsent(playerName, k -> new PlayerUpgrades());
    }
    
    public void reset() {
        sharpnessLevel = 0;
        protectionLevel = 0;
        hasteLevel = 0;
        healPool = false;
        trap = false;
        playerUpgrades.clear();
    }
}
