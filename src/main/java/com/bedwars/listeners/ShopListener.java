package com.bedwars.listeners;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.game.ShopItem;
import com.bedwars.game.ShopManager;
import com.bedwars.models.GameState;
import com.bedwars.models.TeamColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {
    
    private final BedwarsPlugin plugin;
    
    public ShopListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onVillagerClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) {
            return;
        }
        
        Player player = event.getPlayer();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }
        
        event.setCancelled(true);
        
        TeamColor team = game.getPlayerTeam(player);
        if (team != null) {
            ShopManager.openShop(player, team);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Check if it's a shop inventory
        if (!title.contains("Shop") && !title.contains("Blocks") && 
            !title.contains("Weapons") && !title.contains("Armor") &&
            !title.contains("Tools") && !title.contains("Food") &&
            !title.contains("Potions") && !title.contains("Utility")) {
            return;
        }
        
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }
        
        TeamColor team = game.getPlayerTeam(player);
        if (team == null) {
            return;
        }
        
        // Check if it's a category selector
        ShopItem.ShopCategory category = ShopManager.getCategoryFromIcon(clicked.getType());
        if (category != null) {
            ShopManager.openCategory(player, category, team);
            return;
        }
        
        // Check if it's back button
        if (clicked.getType() == Material.ARROW && 
            clicked.hasItemMeta() && 
            clicked.getItemMeta().getDisplayName().equals("§cBack")) {
            ShopManager.openShop(player, team);
            return;
        }
        
        // Try to purchase item
        ShopItem shopItem = ShopManager.findShopItem(clicked);
        if (shopItem != null) {
            ShopManager.purchaseItem(player, shopItem);
        }
    }
}
