package com.bedwars.managers;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.Arena;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaManager {
    
    private final BedwarsPlugin plugin;
    private final Map<String, Arena> arenas;
    private final Map<String, Arena> setupArenas; // Arenas being set up
    private final File arenasFolder;
    
    public ArenaManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.setupArenas = new HashMap<>();
        this.arenasFolder = new File(plugin.getDataFolder(), "arenas");
        
        if (!arenasFolder.exists()) {
            arenasFolder.mkdirs();
        }
        
        loadArenas();
    }
    
    public void loadArenas() {
        arenas.clear();
        
        if (!arenasFolder.exists()) {
            return;
        }
        
        File[] files = arenasFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            try {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                Map<String, Object> arenaMap = config.getValues(false);
                Arena arena = new Arena(arenaMap);
                arenas.put(arena.getName(), arena);
                plugin.getLogger().info("Loaded arena: " + arena.getName());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load arena from " + file.getName() + ": " + e.getMessage());
            }
        }
    }
    
    public void saveArena(Arena arena) {
        File arenaFile = new File(arenasFolder, arena.getName() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        
        Map<String, Object> arenaMap = arena.serialize();
        for (Map.Entry<String, Object> entry : arenaMap.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        
        try {
            config.save(arenaFile);
            arenas.put(arena.getName(), arena);
            plugin.getLogger().info("Saved arena: " + arena.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save arena " + arena.getName() + ": " + e.getMessage());
        }
    }
    
    public void deleteArena(String name) {
        File arenaFile = new File(arenasFolder, name + ".yml");
        if (arenaFile.exists()) {
            arenaFile.delete();
        }
        arenas.remove(name);
        setupArenas.remove(name);
        plugin.getLogger().info("Deleted arena: " + name);
    }
    
    public Arena getArena(String name) {
        return arenas.get(name);
    }
    
    public Arena getSetupArena(String name) {
        return setupArenas.computeIfAbsent(name, Arena::new);
    }
    
    public void finishSetup(String name) {
        Arena arena = setupArenas.remove(name);
        if (arena != null && arena.isComplete()) {
            arena.setEnabled(true);
            saveArena(arena);
        }
    }
    
    public Collection<Arena> getArenas() {
        return arenas.values();
    }
    
    public List<Arena> getAvailableArenas() {
        List<Arena> available = new ArrayList<>();
        for (Arena arena : arenas.values()) {
            if (arena.isEnabled() && arena.isComplete()) {
                available.add(arena);
            }
        }
        return available;
    }
    
    public boolean arenaExists(String name) {
        return arenas.containsKey(name);
    }
}
