package com.bedwars.game;

import com.bedwars.models.TeamColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {
    
    private final String name;
    private final Material material;
    private final int amount;
    private final CurrencyType currencyType;
    private final int cost;
    private final ShopCategory category;
    private final List<String> lore;
    private boolean enchanted;
    
    public enum CurrencyType {
        IRON(Material.IRON_INGOT),
        GOLD(Material.GOLD_INGOT),
        DIAMOND(Material.DIAMOND),
        EMERALD(Material.EMERALD);
        
        private final Material material;
        
        CurrencyType(Material material) {
            this.material = material;
        }
        
        public Material getMaterial() {
            return material;
        }
    }
    
    public enum ShopCategory {
        BLOCKS("Blocks"),
        WEAPONS("Weapons"),
        ARMOR("Armor"),
        TOOLS("Tools"),
        FOOD("Food"),
        POTIONS("Potions"),
        UTILITY("Utility");
        
        private final String displayName;
        
        ShopCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public ShopItem(String name, Material material, int amount, CurrencyType currencyType, int cost, ShopCategory category) {
        this.name = name;
        this.material = material;
        this.amount = amount;
        this.currencyType = currencyType;
        this.cost = cost;
        this.category = category;
        this.lore = new ArrayList<>();
        this.enchanted = false;
    }
    
    public ShopItem addLore(String line) {
        this.lore.add(line);
        return this;
    }
    
    public ShopItem setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        return this;
    }
    
    public ItemStack toItemStack(TeamColor teamColor) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§a" + name);
            
            List<String> fullLore = new ArrayList<>(lore);
            fullLore.add("");
            fullLore.add("§7Cost: §e" + cost + " " + currencyType.name());
            meta.setLore(fullLore);
            
            // Color leather armor
            if (meta instanceof LeatherArmorMeta) {
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                armorMeta.setColor(getColorFromTeam(teamColor));
            }
            
            item.setItemMeta(meta);
        }
        
        if (enchanted) {
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        }
        
        return item;
    }
    
    public ItemStack createPurchaseItem() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§a" + name);
            if (!lore.isEmpty()) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        
        if (enchanted) {
            item.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        }
        
        return item;
    }
    
    private org.bukkit.Color getColorFromTeam(TeamColor teamColor) {
        switch (teamColor) {
            case RED: return org.bukkit.Color.RED;
            case BLUE: return org.bukkit.Color.BLUE;
            case GREEN: return org.bukkit.Color.GREEN;
            case YELLOW: return org.bukkit.Color.YELLOW;
            case AQUA: return org.bukkit.Color.AQUA;
            case WHITE: return org.bukkit.Color.WHITE;
            case PINK: return org.bukkit.Color.FUCHSIA;
            case GRAY: return org.bukkit.Color.GRAY;
            default: return org.bukkit.Color.WHITE;
        }
    }
    
    // Getters
    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public int getAmount() { return amount; }
    public CurrencyType getCurrencyType() { return currencyType; }
    public int getCost() { return cost; }
    public ShopCategory getCategory() { return category; }
}
