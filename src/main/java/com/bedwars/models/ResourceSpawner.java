package com.bedwars.models;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ResourceSpawner implements ConfigurationSerializable {
    
    public enum ResourceType {
        IRON(Material.IRON_INGOT, 1),
        GOLD(Material.GOLD_INGOT, 8),
        DIAMOND(Material.DIAMOND, 30),
        EMERALD(Material.EMERALD, 60);
        
        private final Material material;
        private final int defaultSpawnRate;
        
        ResourceType(Material material, int defaultSpawnRate) {
            this.material = material;
            this.defaultSpawnRate = defaultSpawnRate;
        }
        
        public Material getMaterial() { return material; }
        public int getDefaultSpawnRate() { return defaultSpawnRate; }
    }
    
    private Location location;
    private ResourceType type;
    private int spawnRate;
    private TeamColor team; // null for shared spawners
    
    public ResourceSpawner(Location location, ResourceType type, TeamColor team) {
        this.location = location;
        this.type = type;
        this.spawnRate = type.getDefaultSpawnRate();
        this.team = team;
    }
    
    // Constructor for deserialization
    public ResourceSpawner(Map<String, Object> map) {
        this.location = (Location) map.get("location");
        this.type = ResourceType.valueOf((String) map.get("type"));
        this.spawnRate = (int) map.getOrDefault("spawnRate", type.getDefaultSpawnRate());
        String teamStr = (String) map.get("team");
        this.team = teamStr != null ? TeamColor.valueOf(teamStr) : null;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", location);
        map.put("type", type.name());
        map.put("spawnRate", spawnRate);
        map.put("team", team != null ? team.name() : null);
        return map;
    }
    
    // Getters and setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    
    public ResourceType getType() { return type; }
    public void setType(ResourceType type) { this.type = type; }
    
    public int getSpawnRate() { return spawnRate; }
    public void setSpawnRate(int spawnRate) { this.spawnRate = spawnRate; }
    
    public TeamColor getTeam() { return team; }
    public void setTeam(TeamColor team) { this.team = team; }
    
    public boolean isShared() { return team == null; }
}
