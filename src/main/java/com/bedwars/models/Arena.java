package com.bedwars.models;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class Arena implements ConfigurationSerializable {
    
    private String name;
    private String displayName;
    private Location lobbySpawn;
    private Map<TeamColor, TeamData> teams;
    private List<ResourceSpawner> resourceSpawners;
    private int minPlayers;
    private int maxPlayers;
    private boolean enabled;
    
    public Arena(String name) {
        this.name = name;
        this.displayName = name;
        this.teams = new HashMap<>();
        this.resourceSpawners = new ArrayList<>();
        this.minPlayers = 2;
        this.maxPlayers = 16;
        this.enabled = false;
    }
    
    // Constructor for deserialization
    @SuppressWarnings("unchecked")
    public Arena(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.displayName = (String) map.get("displayName");
        this.lobbySpawn = (Location) map.get("lobbySpawn");
        this.minPlayers = (int) map.get("minPlayers");
        this.maxPlayers = (int) map.get("maxPlayers");
        this.enabled = (boolean) map.get("enabled");
        
        this.teams = new HashMap<>();
        Map<String, Object> teamsMap = (Map<String, Object>) map.get("teams");
        if (teamsMap != null) {
            for (Map.Entry<String, Object> entry : teamsMap.entrySet()) {
                TeamColor color = TeamColor.valueOf(entry.getKey());
                TeamData data = new TeamData((Map<String, Object>) entry.getValue());
                this.teams.put(color, data);
            }
        }
        
        this.resourceSpawners = new ArrayList<>();
        List<Map<String, Object>> spawnersList = (List<Map<String, Object>>) map.get("resourceSpawners");
        if (spawnersList != null) {
            for (Map<String, Object> spawnerMap : spawnersList) {
                this.resourceSpawners.add(new ResourceSpawner(spawnerMap));
            }
        }
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("displayName", displayName);
        map.put("lobbySpawn", lobbySpawn);
        map.put("minPlayers", minPlayers);
        map.put("maxPlayers", maxPlayers);
        map.put("enabled", enabled);
        
        Map<String, Object> teamsMap = new HashMap<>();
        for (Map.Entry<TeamColor, TeamData> entry : teams.entrySet()) {
            teamsMap.put(entry.getKey().name(), entry.getValue().serialize());
        }
        map.put("teams", teamsMap);
        
        List<Map<String, Object>> spawnersList = new ArrayList<>();
        for (ResourceSpawner spawner : resourceSpawners) {
            spawnersList.add(spawner.serialize());
        }
        map.put("resourceSpawners", spawnersList);
        
        return map;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public Location getLobbySpawn() { return lobbySpawn; }
    public void setLobbySpawn(Location lobbySpawn) { this.lobbySpawn = lobbySpawn; }
    
    public Map<TeamColor, TeamData> getTeams() { return teams; }
    public void setTeams(Map<TeamColor, TeamData> teams) { this.teams = teams; }
    
    public TeamData getTeam(TeamColor color) { return teams.get(color); }
    public void addTeam(TeamColor color, TeamData data) { teams.put(color, data); }
    
    public List<ResourceSpawner> getResourceSpawners() { return resourceSpawners; }
    public void addResourceSpawner(ResourceSpawner spawner) { resourceSpawners.add(spawner); }
    
    public int getMinPlayers() { return minPlayers; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public boolean isComplete() {
        return lobbySpawn != null && !teams.isEmpty() && 
               teams.values().stream().allMatch(TeamData::isComplete);
    }
}
