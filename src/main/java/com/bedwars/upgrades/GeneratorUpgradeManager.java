package com.bedwars.upgrades;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import com.bedwars.models.ResourceSpawner;
import com.bedwars.models.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GeneratorUpgradeManager implements Listener {

    private final BedwarsPlugin plugin;
    private final Map<TeamColor, Map<ResourceSpawner.ResourceType, Integer>> teamUpgradeLevels;

    // Upgrade configurations
    private static final Map<ResourceSpawner.ResourceType, List<GeneratorUpgrade>> UPGRADES = new HashMap<>();

    static {
        // Iron generator upgrades
        List<GeneratorUpgrade> ironUpgrades = new ArrayList<>();
        ironUpgrades.add(new GeneratorUpgrade("Iron Forge", 1, 4, Material.DIAMOND,
            0.75, "§750% faster iron spawning"));
        ironUpgrades.add(new GeneratorUpgrade("Iron Forge", 2, 8, Material.DIAMOND,
            0.5, "§7100% faster iron spawning"));
        ironUpgrades.add(new GeneratorUpgrade("Iron Forge", 3, 12, Material.DIAMOND,
            0.33, "§7200% faster iron spawning"));
        ironUpgrades.add(new GeneratorUpgrade("Iron Forge", 4, 16, Material.DIAMOND,
            0.25, "§7300% faster iron spawning"));
        UPGRADES.put(ResourceSpawner.ResourceType.IRON, ironUpgrades);

        // Gold generator upgrades
        List<GeneratorUpgrade> goldUpgrades = new ArrayList<>();
        goldUpgrades.add(new GeneratorUpgrade("Gold Forge", 1, 6, Material.DIAMOND,
            0.75, "§750% faster gold spawning"));
        goldUpgrades.add(new GeneratorUpgrade("Gold Forge", 2, 12, Material.DIAMOND,
            0.5, "§7100% faster gold spawning"));
        goldUpgrades.add(new GeneratorUpgrade("Gold Forge", 3, 18, Material.DIAMOND,
            0.33, "§7200% faster gold spawning"));
        goldUpgrades.add(new GeneratorUpgrade("Gold Forge", 4, 24, Material.DIAMOND,
            0.25, "§7300% faster gold spawning"));
        UPGRADES.put(ResourceSpawner.ResourceType.GOLD, goldUpgrades);
    }

    public GeneratorUpgradeManager(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.teamUpgradeLevels = new HashMap<>();
    }

    /**
     * Initialize upgrades for a team
     */
    public void initializeTeam(TeamColor team) {
        Map<ResourceSpawner.ResourceType, Integer> upgrades = new HashMap<>();
        upgrades.put(ResourceSpawner.ResourceType.IRON, 0);
        upgrades.put(ResourceSpawner.ResourceType.GOLD, 0);
        teamUpgradeLevels.put(team, upgrades);
    }

    /**
     * Get current upgrade level for a team's generator
     */
    public int getUpgradeLevel(TeamColor team, ResourceSpawner.ResourceType type) {
        return teamUpgradeLevels
            .getOrDefault(team, new HashMap<>())
            .getOrDefault(type, 0);
    }

    /**
     * Get speed multiplier for a team's generator
     */
    public double getSpeedMultiplier(TeamColor team, ResourceSpawner.ResourceType type) {
        int level = getUpgradeLevel(team, type);
        if (level == 0) {
            return 1.0;
        }

        List<GeneratorUpgrade> upgrades = UPGRADES.get(type);
        if (upgrades == null || level > upgrades.size()) {
            return 1.0;
        }

        return upgrades.get(level - 1).getSpeedMultiplier();
    }

    /**
     * Open the generator upgrade GUI for a player
     */
    public void openUpgradeGUI(Player player) {
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        TeamColor team = game.getPlayerTeam(player);
        if (team == null) {
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§8§lGenerator Upgrades");

        // Iron forge upgrade
        int ironLevel = getUpgradeLevel(team, ResourceSpawner.ResourceType.IRON);
        inv.setItem(11, createUpgradeItem(ResourceSpawner.ResourceType.IRON, ironLevel));

        // Gold forge upgrade
        int goldLevel = getUpgradeLevel(team, ResourceSpawner.ResourceType.GOLD);
        inv.setItem(15, createUpgradeItem(ResourceSpawner.ResourceType.GOLD, goldLevel));

        player.openInventory(inv);
    }

    /**
     * Create an upgrade item for the GUI
     */
    private ItemStack createUpgradeItem(ResourceSpawner.ResourceType type, int currentLevel) {
        List<GeneratorUpgrade> upgrades = UPGRADES.get(type);
        if (upgrades == null) {
            return new ItemStack(Material.BARRIER);
        }

        Material displayMaterial = type == ResourceSpawner.ResourceType.IRON ?
            Material.IRON_INGOT : Material.GOLD_INGOT;

        ItemStack item = new ItemStack(displayMaterial);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            List<String> lore = new ArrayList<>();

            if (currentLevel >= upgrades.size()) {
                // Max level reached
                meta.setDisplayName("§a" + upgrades.get(0).getName() + " §7- §cMAX");
                lore.add("§7Your team's " + type.name().toLowerCase() + " generator");
                lore.add("§7is at maximum level!");
            } else {
                GeneratorUpgrade nextUpgrade = upgrades.get(currentLevel);
                meta.setDisplayName("§a" + nextUpgrade.getDisplayName());

                lore.add("§7Current Level: §e" + toRoman(currentLevel));
                lore.add("");
                lore.add("§7Upgrade to Level §e" + toRoman(currentLevel + 1));
                lore.add(nextUpgrade.getDescription());
                lore.add("");
                lore.add("§7Cost: §b" + nextUpgrade.getCost() + " Diamond" +
                    (nextUpgrade.getCost() > 1 ? "s" : ""));
                lore.add("");
                lore.add("§eClick to purchase!");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Handle upgrade GUI clicks
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        if (!title.equals("§8§lGenerator Upgrades")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }

        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null) {
            return;
        }

        TeamColor team = game.getPlayerTeam(player);
        if (team == null) {
            return;
        }

        // Determine which upgrade was clicked
        ResourceSpawner.ResourceType type = null;
        if (event.getSlot() == 11) {
            type = ResourceSpawner.ResourceType.IRON;
        } else if (event.getSlot() == 15) {
            type = ResourceSpawner.ResourceType.GOLD;
        }

        if (type != null) {
            purchaseUpgrade(player, team, type);
        }
    }

    /**
     * Purchase an upgrade
     */
    private void purchaseUpgrade(Player player, TeamColor team, ResourceSpawner.ResourceType type) {
        int currentLevel = getUpgradeLevel(team, type);
        List<GeneratorUpgrade> upgrades = UPGRADES.get(type);

        if (upgrades == null || currentLevel >= upgrades.size()) {
            player.sendMessage("§cThis upgrade is already at maximum level!");
            return;
        }

        GeneratorUpgrade upgrade = upgrades.get(currentLevel);

        // Check if player has enough diamonds
        int diamondCount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.DIAMOND) {
                diamondCount += item.getAmount();
            }
        }

        if (diamondCount < upgrade.getCost()) {
            player.sendMessage("§cYou need " + upgrade.getCost() + " diamonds to purchase this upgrade!");
            return;
        }

        // Remove diamonds
        int remaining = upgrade.getCost();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() == Material.DIAMOND) {
                if (item.getAmount() > remaining) {
                    item.setAmount(item.getAmount() - remaining);
                    remaining = 0;
                    break;
                } else {
                    remaining -= item.getAmount();
                    player.getInventory().setItem(i, null);
                    if (remaining <= 0) break;
                }
            }
        }

        // Apply upgrade
        teamUpgradeLevels.get(team).put(type, currentLevel + 1);

        // Broadcast to team
        game.broadcastMessage("§a" + player.getName() + " §epurchased §a" +
            upgrade.getDisplayName() + "§e!");

        player.sendMessage("§aUpgrade purchased successfully!");

        // Refresh GUI
        openUpgradeGUI(player);
    }

    /**
     * Convert number to Roman numerals
     */
    private static String toRoman(int number) {
        if (number < 1 || number > 4) return String.valueOf(number);
        String[] romanNumerals = {"", "I", "II", "III", "IV"};
        return romanNumerals[number];
    }

    /**
     * Clear all upgrades (on game end)
     */
    public void clear() {
        teamUpgradeLevels.clear();
    }
}
