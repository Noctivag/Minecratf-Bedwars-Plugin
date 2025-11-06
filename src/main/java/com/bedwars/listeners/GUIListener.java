package com.bedwars.listeners;

import com.bedwars.BedwarsPlugin;
import com.bedwars.gui.ArenaSelectorGUI;
import com.bedwars.models.Arena;
import com.bedwars.models.GameMode;
import com.bedwars.utils.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    
    private final BedwarsPlugin plugin;
    private final ArenaSelectorGUI selectorGUI;
    
    public GUIListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.selectorGUI = new ArenaSelectorGUI(plugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Check if it's a selector GUI
        if (!selectorGUI.isGameModeSelector(title) && !selectorGUI.isArenaSelector(title)) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        
        // Handle game mode selection
        if (selectorGUI.isGameModeSelector(title)) {
            handleGameModeSelection(player, event.getSlot());
        }
        // Handle arena selection
        else if (selectorGUI.isArenaSelector(title)) {
            GameMode mode = selectorGUI.getGameModeFromTitle(title);
            if (mode != null) {
                handleArenaSelection(player, mode, clicked);
            }
        }
    }
    
    private void handleGameModeSelection(Player player, int slot) {
        GameMode selectedMode = null;
        
        switch (slot) {
            case 10:
                selectedMode = GameMode.SOLO;
                break;
            case 11:
                selectedMode = GameMode.DOUBLES;
                break;
            case 12:
                selectedMode = GameMode.THREES;
                break;
            case 13:
                selectedMode = GameMode.FOURS;
                break;
            case 15:
                selectedMode = GameMode.MEGA_DOUBLES;
                break;
            case 16:
                selectedMode = GameMode.MEGA_FOURS;
                break;
        }
        
        if (selectedMode != null) {
            player.closeInventory();
            selectorGUI.openArenaSelector(player, selectedMode);
        }
    }
    
    private void handleArenaSelection(Player player, GameMode mode, ItemStack item) {
        String arenaName = MessageUtil.stripColor(item.getItemMeta().getDisplayName());
        
        Arena arena = plugin.getArenaManager().getArena(arenaName);
        if (arena == null || !arena.isEnabled()) {
            player.sendMessage(MessageUtil.color("&cThis arena is no longer available!"));
            player.closeInventory();
            return;
        }
        
        player.closeInventory();
        
        // Join or create game
        if (plugin.getGameManager().hasGame(arena.getName())) {
            boolean joined = plugin.getGameManager().getGame(arena.getName()).addPlayer(player);
            if (!joined) {
                player.sendMessage(MessageUtil.color("&cCould not join this game!"));
            }
        } else {
            plugin.getGameManager().createGame(arena);
            boolean joined = plugin.getGameManager().getGame(arena.getName()).addPlayer(player);
            if (!joined) {
                player.sendMessage(MessageUtil.color("&cCould not join this game!"));
            }
        }
    }
    
    public ArenaSelectorGUI getSelectorGUI() {
        return selectorGUI;
    }
}
