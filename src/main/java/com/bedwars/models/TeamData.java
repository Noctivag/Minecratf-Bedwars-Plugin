package com.bedwars.models;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class TeamData implements ConfigurationSerializable {
    
    private TeamColor color;
    private Location bedLocation;
    private Location spawnLocation;
    private Location shopLocation;
    private Location upgradesLocation;
    private boolean bedAlive;
    private int maxSize;
    
    public TeamData(TeamColor color) {
        this.color = color;
        this.bedAlive = true;
        this.maxSize = 4;
    }
    
    // Constructor for deserialization
    public TeamData(Map<String, Object> map) {
        this.color = TeamColor.valueOf((String) map.get("color"));
        this.bedLocation = (Location) map.get("bedLocation");
        this.spawnLocation = (Location) map.get("spawnLocation");
        this.shopLocation = (Location) map.get("shopLocation");
        this.upgradesLocation = (Location) map.get("upgradesLocation");
        this.bedAlive = (boolean) map.getOrDefault("bedAlive", true);
        this.maxSize = (int) map.getOrDefault("maxSize", 4);
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("color", color.name());
        map.put("bedLocation", bedLocation);
        map.put("spawnLocation", spawnLocation);
        map.put("shopLocation", shopLocation);
        map.put("upgradesLocation", upgradesLocation);
        map.put("bedAlive", bedAlive);
        map.put("maxSize", maxSize);
        return map;
    }
    
    public boolean isComplete() {
        return bedLocation != null && spawnLocation != null;
    }
    
    // Getters and setters
    public TeamColor getColor() { return color; }
    
    public Location getBedLocation() { return bedLocation; }
    public void setBedLocation(Location bedLocation) { this.bedLocation = bedLocation; }
    
    public Location getSpawnLocation() { return spawnLocation; }
    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }
    
    public Location getShopLocation() { return shopLocation; }
    public void setShopLocation(Location shopLocation) { this.shopLocation = shopLocation; }
    
    public Location getUpgradesLocation() { return upgradesLocation; }
    public void setUpgradesLocation(Location upgradesLocation) { this.upgradesLocation = upgradesLocation; }
    
    public boolean isBedAlive() { return bedAlive; }
    public void setBedAlive(boolean bedAlive) { this.bedAlive = bedAlive; }
    
    public int getMaxSize() { return maxSize; }
    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }
}
