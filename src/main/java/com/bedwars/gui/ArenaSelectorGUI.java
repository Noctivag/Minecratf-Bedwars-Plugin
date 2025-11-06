package com.bedwars.gui;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.Arena;
import com.bedwars.models.GameMode;
import com.bedwars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaSelectorGUI {
    
    private final BedwarsPlugin plugin;
    
    public ArenaSelectorGUI(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openGameModeSelector(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, MessageUtil.color("&c&lSelect Game Mode"));
        
        // Solo Mode
        inv.setItem(10, createModeItem(GameMode.SOLO, Material.IRON_SWORD));
        
        // Doubles Mode
        inv.setItem(11, createModeItem(GameMode.DOUBLES, Material.GOLDEN_SWORD));
        
        // Threes Mode
        inv.setItem(12, createModeItem(GameMode.THREES, Material.DIAMOND_SWORD));
        
        // Fours Mode
        inv.setItem(13, createModeItem(GameMode.FOURS, Material.NETHERITE_SWORD));
        
        // Mega Doubles
        inv.setItem(15, createModeItem(GameMode.MEGA_DOUBLES, Material.DIAMOND_CHESTPLATE));
        
        // Mega Fours
        inv.setItem(16, createModeItem(GameMode.MEGA_FOURS, Material.NETHERITE_CHESTPLATE));
        
        player.openInventory(inv);
    }
    
    public void openArenaSelector(Player player, GameMode gameMode) {
        List<Arena> arenas = plugin.getArenaManager().getArenas().stream()
            .filter(a -> a.isEnabled() && a.getGameMode() == gameMode)
            .collect(Collectors.toList());
        
        if (arenas.isEmpty()) {
            player.sendMessage(MessageUtil.color("&cNo arenas available for this game mode!"));
            return;
        }
        
        int size = Math.min(54, ((arenas.size() + 8) / 9) * 9);
        Inventory inv = Bukkit.createInventory(null, size, 
            MessageUtil.color("&c&l" + gameMode.getDisplayName() + " - Select Arena"));
        
        for (int i = 0; i < arenas.size() && i < 54; i++) {
            Arena arena = arenas.get(i);
            inv.setItem(i, createArenaItem(arena));
        }
        
        player.openInventory(inv);
    }
    
    private ItemStack createModeItem(GameMode mode, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(MessageUtil.color("&e&l" + mode.getDisplayName()));
        
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtil.color("&7"));
        lore.add(MessageUtil.color("&7Players per team: &e" + mode.getTeamSize()));
        lore.add(MessageUtil.color("&7Max teams: &e" + mode.getMaxTeams()));
        lore.add(MessageUtil.color("&7Max players: &e" + mode.getMaxPlayers()));
        lore.add(MessageUtil.color("&7"));
        
        // Count available arenas for this mode
        long arenaCount = plugin.getArenaManager().getArenas().stream()
            .filter(a -> a.isEnabled() && a.getGameMode() == mode)
            .count();
        
        if (arenaCount > 0) {
            lore.add(MessageUtil.color("&aClick to join! &7(" + arenaCount + " arenas)"));
        } else {
            lore.add(MessageUtil.color("&cNo arenas available"));
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack createArenaItem(Arena arena) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(MessageUtil.color("&e&l" + arena.getDisplayName()));
        
        List<String> lore = new ArrayList<>();
        lore.add(MessageUtil.color("&7"));
        lore.add(MessageUtil.color("&7Mode: &e" + arena.getGameMode().getDisplayName()));
        lore.add(MessageUtil.color("&7Teams: &e" + arena.getTeams().size()));
        
        // Get current game status if running
        if (plugin.getGameManager().hasGame(arena.getName())) {
            int players = plugin.getGameManager().getGame(arena.getName()).getTotalPlayers();
            lore.add(MessageUtil.color("&7Players: &e" + players + "&7/&e" + arena.getMaxPlayers()));
            lore.add(MessageUtil.color("&7"));
            lore.add(MessageUtil.color("&aClick to join!"));
        } else {
            lore.add(MessageUtil.color("&7Status: &aWaiting for players"));
            lore.add(MessageUtil.color("&7"));
            lore.add(MessageUtil.color("&aClick to join!"));
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    public boolean isGameModeSelector(String title) {
        return title.equals(MessageUtil.color("&c&lSelect Game Mode"));
    }
    
    public boolean isArenaSelector(String title) {
        for (GameMode mode : GameMode.values()) {
            if (title.equals(MessageUtil.color("&c&l" + mode.getDisplayName() + " - Select Arena"))) {
                return true;
            }
        }
        return false;
    }
    
    public GameMode getGameModeFromTitle(String title) {
        for (GameMode mode : GameMode.values()) {
            if (title.contains(mode.getDisplayName())) {
                return mode;
            }
        }
        return null;
    }
}
