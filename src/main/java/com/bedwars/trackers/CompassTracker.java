package com.bedwars.trackers;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import com.bedwars.models.TeamColor;
import com.bedwars.models.TeamData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CompassTracker implements Listener {

    private final BedwarsPlugin plugin;
    private final Map<UUID, TrackingMode> playerTrackingMode;
    private BukkitRunnable updateTask;

    public enum TrackingMode {
        NEAREST_PLAYER("Nearest Player"),
        NEAREST_BED("Nearest Bed");

        private final String displayName;

        TrackingMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public TrackingMode next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    public CompassTracker(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.playerTrackingMode = new HashMap<>();
        startUpdateTask();
    }

    @EventHandler
    public void onCompassInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.COMPASS) {
            return;
        }

        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        // Right click to cycle tracking mode
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            cycleTrackingMode(player);
        }
    }

    /**
     * Start the compass update task
     */
    private void startUpdateTask() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    updateCompass(player);
                }
            }
        };
        updateTask.runTaskTimer(plugin, 0L, 20L); // Update every second
    }

    /**
     * Update compass for a player
     */
    private void updateCompass(Player player) {
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        // Check if player has compass
        if (!hasCompass(player)) {
            return;
        }

        TrackingMode mode = playerTrackingMode.getOrDefault(player.getUniqueId(), TrackingMode.NEAREST_PLAYER);
        Location target = null;

        switch (mode) {
            case NEAREST_PLAYER:
                target = findNearestEnemy(player, game);
                break;
            case NEAREST_BED:
                target = findNearestBed(player, game);
                break;
        }

        if (target != null) {
            player.setCompassTarget(target);
        }

        // Update compass name
        updateCompassItem(player, mode, target);
    }

    /**
     * Find the nearest enemy player
     */
    private Location findNearestEnemy(Player player, BedwarsGame game) {
        TeamColor playerTeam = game.getPlayerTeam(player);
        if (playerTeam == null) {
            return null;
        }

        Player nearestEnemy = null;
        double nearestDistance = Double.MAX_VALUE;

        for (TeamColor team : game.getArena().getTeams().keySet()) {
            if (team == playerTeam) {
                continue; // Skip own team
            }

            List<Player> teamPlayers = game.getTeamPlayers(team);
            if (teamPlayers == null) {
                continue;
            }

            for (Player enemy : teamPlayers) {
                if (enemy != null && enemy.isOnline()) {
                    double distance = player.getLocation().distance(enemy.getLocation());
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestEnemy = enemy;
                    }
                }
            }
        }

        return nearestEnemy != null ? nearestEnemy.getLocation() : null;
    }

    /**
     * Find the nearest enemy bed
     */
    private Location findNearestBed(Player player, BedwarsGame game) {
        TeamColor playerTeam = game.getPlayerTeam(player);
        if (playerTeam == null) {
            return null;
        }

        Location nearestBed = null;
        double nearestDistance = Double.MAX_VALUE;

        for (TeamColor team : game.getArena().getTeams().keySet()) {
            if (team == playerTeam) {
                continue; // Skip own team
            }

            TeamData teamData = game.getArena().getTeam(team);
            if (teamData == null || !teamData.isBedAlive()) {
                continue; // Skip destroyed beds
            }

            Location bedLocation = teamData.getBedLocation();
            if (bedLocation != null) {
                double distance = player.getLocation().distance(bedLocation);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestBed = bedLocation;
                }
            }
        }

        // If no beds found, try to find nearest player instead
        if (nearestBed == null) {
            return findNearestEnemy(player, game);
        }

        return nearestBed;
    }

    /**
     * Check if player has a compass
     */
    private boolean hasCompass(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.COMPASS) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update compass item display name
     */
    private void updateCompassItem(Player player, TrackingMode mode, Location target) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.COMPASS) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    String targetInfo = target != null ?
                        String.format("§7(%.0fm)", player.getLocation().distance(target)) :
                        "§c(None)";

                    meta.setDisplayName("§aTracker §7- §e" + mode.getDisplayName() + " " + targetInfo);
                    meta.setLore(Arrays.asList("§7Right-click to change mode"));
                    item.setItemMeta(meta);
                }
            }
        }
    }

    /**
     * Cycle the tracking mode for a player
     */
    private void cycleTrackingMode(Player player) {
        TrackingMode currentMode = playerTrackingMode.getOrDefault(player.getUniqueId(), TrackingMode.NEAREST_PLAYER);
        TrackingMode nextMode = currentMode.next();
        playerTrackingMode.put(player.getUniqueId(), nextMode);

        player.sendMessage("§aTracker mode: §e" + nextMode.getDisplayName());
        updateCompass(player); // Immediate update
    }

    /**
     * Give a compass to a player
     */
    public void giveCompass(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aTracker");
            meta.setLore(Arrays.asList("§7Right-click to change mode"));
            compass.setItemMeta(meta);
        }
        player.getInventory().addItem(compass);
    }

    /**
     * Reset tracking mode for a player
     */
    public void resetPlayer(Player player) {
        playerTrackingMode.remove(player.getUniqueId());
    }

    /**
     * Shutdown the tracker
     */
    public void shutdown() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        playerTrackingMode.clear();
    }
}
