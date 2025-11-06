package com.bedwars.game;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ShopManager {
    
    private static final Map<ShopItem.ShopCategory, List<ShopItem>> SHOP_ITEMS = new HashMap<>();
    
    static {
        initializeShop();
    }
    
    private static void initializeShop() {
        // BLOCKS
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("Wool", Material.WHITE_WOOL, 16, 
            ShopItem.CurrencyType.IRON, 4, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Quick blocks for bridging"));
        
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("Hardened Clay", Material.TERRACOTTA, 16,
            ShopItem.CurrencyType.IRON, 12, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Blast-resistant blocks"));
        
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("End Stone", Material.END_STONE, 12,
            ShopItem.CurrencyType.IRON, 24, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Very blast-resistant"));
        
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("Ladder", Material.LADDER, 8,
            ShopItem.CurrencyType.IRON, 4, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Climb walls easily"));
        
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("Oak Planks", Material.OAK_PLANKS, 16,
            ShopItem.CurrencyType.GOLD, 4, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Stronger than wool"));
        
        addItem(ShopItem.ShopCategory.BLOCKS, new ShopItem("Obsidian", Material.OBSIDIAN, 4,
            ShopItem.CurrencyType.EMERALD, 4, ShopItem.ShopCategory.BLOCKS)
            .addLore("§7Ultimate protection"));
        
        // WEAPONS
        addItem(ShopItem.ShopCategory.WEAPONS, new ShopItem("Stone Sword", Material.STONE_SWORD, 1,
            ShopItem.CurrencyType.IRON, 10, ShopItem.ShopCategory.WEAPONS));
        
        addItem(ShopItem.ShopCategory.WEAPONS, new ShopItem("Iron Sword", Material.IRON_SWORD, 1,
            ShopItem.CurrencyType.GOLD, 7, ShopItem.ShopCategory.WEAPONS));
        
        addItem(ShopItem.ShopCategory.WEAPONS, new ShopItem("Diamond Sword", Material.DIAMOND_SWORD, 1,
            ShopItem.CurrencyType.EMERALD, 4, ShopItem.ShopCategory.WEAPONS));
        
        addItem(ShopItem.ShopCategory.WEAPONS, new ShopItem("Knockback Stick", Material.STICK, 1,
            ShopItem.CurrencyType.GOLD, 5, ShopItem.ShopCategory.WEAPONS)
            .addLore("§7Knockback II")
            .setEnchanted(true));
        
        // ARMOR
        addItem(ShopItem.ShopCategory.ARMOR, new ShopItem("Chainmail Armor", Material.CHAINMAIL_BOOTS, 1,
            ShopItem.CurrencyType.IRON, 40, ShopItem.ShopCategory.ARMOR)
            .addLore("§7Permanent chainmail armor"));
        
        addItem(ShopItem.ShopCategory.ARMOR, new ShopItem("Iron Armor", Material.IRON_BOOTS, 1,
            ShopItem.CurrencyType.GOLD, 12, ShopItem.ShopCategory.ARMOR)
            .addLore("§7Permanent iron armor"));
        
        addItem(ShopItem.ShopCategory.ARMOR, new ShopItem("Diamond Armor", Material.DIAMOND_BOOTS, 1,
            ShopItem.CurrencyType.EMERALD, 6, ShopItem.ShopCategory.ARMOR)
            .addLore("§7Permanent diamond armor"));
        
        // TOOLS
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Wooden Pickaxe", Material.WOODEN_PICKAXE, 1,
            ShopItem.CurrencyType.IRON, 10, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Efficiency I"));
        
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Stone Pickaxe", Material.STONE_PICKAXE, 1,
            ShopItem.CurrencyType.IRON, 20, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Efficiency II"));
        
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Iron Pickaxe", Material.IRON_PICKAXE, 1,
            ShopItem.CurrencyType.GOLD, 3, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Efficiency III, Unbreaking I"));
        
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Diamond Pickaxe", Material.DIAMOND_PICKAXE, 1,
            ShopItem.CurrencyType.GOLD, 6, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Efficiency V, Unbreaking II"));
        
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Wooden Axe", Material.WOODEN_AXE, 1,
            ShopItem.CurrencyType.IRON, 10, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Efficiency I"));
        
        addItem(ShopItem.ShopCategory.TOOLS, new ShopItem("Shears", Material.SHEARS, 1,
            ShopItem.CurrencyType.IRON, 20, ShopItem.ShopCategory.TOOLS)
            .addLore("§7Cut wool quickly"));
        
        // FOOD
        addItem(ShopItem.ShopCategory.FOOD, new ShopItem("Apple", Material.APPLE, 1,
            ShopItem.CurrencyType.IRON, 4, ShopItem.ShopCategory.FOOD));
        
        addItem(ShopItem.ShopCategory.FOOD, new ShopItem("Bread", Material.BREAD, 3,
            ShopItem.CurrencyType.IRON, 8, ShopItem.ShopCategory.FOOD));
        
        addItem(ShopItem.ShopCategory.FOOD, new ShopItem("Golden Apple", Material.GOLDEN_APPLE, 1,
            ShopItem.CurrencyType.GOLD, 3, ShopItem.ShopCategory.FOOD)
            .addLore("§7Regeneration and Absorption"));
        
        // POTIONS
        addItem(ShopItem.ShopCategory.POTIONS, new ShopItem("Speed Potion", Material.POTION, 1,
            ShopItem.CurrencyType.EMERALD, 1, ShopItem.ShopCategory.POTIONS)
            .addLore("§7Speed II (45 seconds)"));
        
        addItem(ShopItem.ShopCategory.POTIONS, new ShopItem("Jump Potion", Material.POTION, 1,
            ShopItem.CurrencyType.EMERALD, 1, ShopItem.ShopCategory.POTIONS)
            .addLore("§7Jump Boost V (45 seconds)"));
        
        addItem(ShopItem.ShopCategory.POTIONS, new ShopItem("Invisibility Potion", Material.POTION, 1,
            ShopItem.CurrencyType.EMERALD, 2, ShopItem.ShopCategory.POTIONS)
            .addLore("§7Invisibility (30 seconds)"));
        
        // UTILITY
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Arrow", Material.ARROW, 8,
            ShopItem.CurrencyType.GOLD, 2, ShopItem.ShopCategory.UTILITY));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Bow", Material.BOW, 1,
            ShopItem.CurrencyType.GOLD, 12, ShopItem.ShopCategory.UTILITY));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Power Bow", Material.BOW, 1,
            ShopItem.CurrencyType.GOLD, 20, ShopItem.ShopCategory.UTILITY)
            .addLore("§7Power I, Punch I")
            .setEnchanted(true));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("TNT", Material.TNT, 1,
            ShopItem.CurrencyType.GOLD, 4, ShopItem.ShopCategory.UTILITY)
            .addLore("§7Auto-ignites!"));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Ender Pearl", Material.ENDER_PEARL, 1,
            ShopItem.CurrencyType.EMERALD, 4, ShopItem.ShopCategory.UTILITY)
            .addLore("§7Teleport instantly"));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Water Bucket", Material.WATER_BUCKET, 1,
            ShopItem.CurrencyType.GOLD, 3, ShopItem.ShopCategory.UTILITY)
            .addLore("§7MLG or defense"));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Bridge Egg", Material.EGG, 1,
            ShopItem.CurrencyType.EMERALD, 2, ShopItem.ShopCategory.UTILITY)
            .addLore("§7Creates a bridge where it lands"));
        
        addItem(ShopItem.ShopCategory.UTILITY, new ShopItem("Fireball", Material.FIRE_CHARGE, 1,
            ShopItem.CurrencyType.IRON, 40, ShopItem.ShopCategory.UTILITY)
            .addLore("§7Launch a fireball!"));
    }
    
    private static void addItem(ShopItem.ShopCategory category, ShopItem item) {
        SHOP_ITEMS.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
    }
    
    public static void openShop(Player player, TeamColor teamColor) {
        Inventory inv = Bukkit.createInventory(null, 54, "§8§lBedwars Shop");
        
        // Add category selectors
        inv.setItem(1, createCategoryItem(ShopItem.ShopCategory.BLOCKS, Material.TERRACOTTA));
        inv.setItem(2, createCategoryItem(ShopItem.ShopCategory.WEAPONS, Material.GOLDEN_SWORD));
        inv.setItem(3, createCategoryItem(ShopItem.ShopCategory.ARMOR, Material.CHAINMAIL_CHESTPLATE));
        inv.setItem(4, createCategoryItem(ShopItem.ShopCategory.TOOLS, Material.STONE_PICKAXE));
        inv.setItem(5, createCategoryItem(ShopItem.ShopCategory.FOOD, Material.BREAD));
        inv.setItem(6, createCategoryItem(ShopItem.ShopCategory.POTIONS, Material.POTION));
        inv.setItem(7, createCategoryItem(ShopItem.ShopCategory.UTILITY, Material.TNT));
        
        // Show all items by default (or show blocks category)
        List<ShopItem> items = SHOP_ITEMS.get(ShopItem.ShopCategory.BLOCKS);
        if (items != null) {
            int slot = 18;
            for (ShopItem item : items) {
                if (slot < 54) {
                    inv.setItem(slot++, item.toItemStack(teamColor));
                }
            }
        }
        
        player.openInventory(inv);
    }
    
    public static void openCategory(Player player, ShopItem.ShopCategory category, TeamColor teamColor) {
        Inventory inv = Bukkit.createInventory(null, 54, "§8§l" + category.getDisplayName());
        
        // Add back button
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§cBack");
        back.setItemMeta(backMeta);
        inv.setItem(0, back);
        
        // Add category items
        List<ShopItem> items = SHOP_ITEMS.get(category);
        if (items != null) {
            int slot = 9;
            for (ShopItem item : items) {
                if (slot < 54) {
                    inv.setItem(slot++, item.toItemStack(teamColor));
                }
            }
        }
        
        player.openInventory(inv);
    }
    
    private static ItemStack createCategoryItem(ShopItem.ShopCategory category, Material icon) {
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a§l" + category.getDisplayName());
        meta.setLore(Arrays.asList("§7Click to browse"));
        item.setItemMeta(meta);
        return item;
    }
    
    public static boolean purchaseItem(Player player, ShopItem item) {
        // Count currency in inventory
        int currencyCount = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.getType() == item.getCurrencyType().getMaterial()) {
                currencyCount += stack.getAmount();
            }
        }
        
        // Check if player has enough
        if (currencyCount < item.getCost()) {
            player.sendMessage("§cYou don't have enough " + item.getCurrencyType().name() + "!");
            return false;
        }
        
        // Remove currency
        int remaining = item.getCost();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack != null && stack.getType() == item.getCurrencyType().getMaterial()) {
                if (stack.getAmount() > remaining) {
                    stack.setAmount(stack.getAmount() - remaining);
                    remaining = 0;
                    break;
                } else {
                    remaining -= stack.getAmount();
                    player.getInventory().setItem(i, null);
                    if (remaining <= 0) break;
                }
            }
        }
        
        // Give item
        player.getInventory().addItem(item.createPurchaseItem());
        player.sendMessage("§aYou purchased §e" + item.getName() + "§a!");
        return true;
    }
    
    public static ShopItem findShopItem(ItemStack displayItem) {
        if (displayItem == null || !displayItem.hasItemMeta()) {
            return null;
        }
        
        String displayName = displayItem.getItemMeta().getDisplayName();
        
        for (List<ShopItem> items : SHOP_ITEMS.values()) {
            for (ShopItem shopItem : items) {
                if (("§a" + shopItem.getName()).equals(displayName)) {
                    return shopItem;
                }
            }
        }
        
        return null;
    }
    
    public static ShopItem.ShopCategory getCategoryFromIcon(Material material) {
        if (material == Material.TERRACOTTA) return ShopItem.ShopCategory.BLOCKS;
        if (material == Material.GOLDEN_SWORD) return ShopItem.ShopCategory.WEAPONS;
        if (material == Material.CHAINMAIL_CHESTPLATE) return ShopItem.ShopCategory.ARMOR;
        if (material == Material.STONE_PICKAXE) return ShopItem.ShopCategory.TOOLS;
        if (material == Material.BREAD) return ShopItem.ShopCategory.FOOD;
        if (material == Material.POTION) return ShopItem.ShopCategory.POTIONS;
        if (material == Material.TNT) return ShopItem.ShopCategory.UTILITY;
        return null;
    }
}
