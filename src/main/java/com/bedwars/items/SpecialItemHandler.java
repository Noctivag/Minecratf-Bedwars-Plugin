package com.bedwars.items;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class SpecialItemHandler implements Listener {

    private final BedwarsPlugin plugin;
    private final Set<Entity> specialProjectiles;

    public SpecialItemHandler(BedwarsPlugin plugin) {
        this.plugin = plugin;
        this.specialProjectiles = new HashSet<>();
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Egg)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);

        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Check if it's a bridge egg (egg with specific name)
        if (itemInHand.getType() == Material.EGG &&
            itemInHand.hasItemMeta() &&
            itemInHand.getItemMeta().hasDisplayName() &&
            itemInHand.getItemMeta().getDisplayName().contains("Bridge Egg")) {

            Egg egg = (Egg) event.getEntity();
            egg.setMetadata("bridge_egg", new FixedMetadataValue(plugin, true));
            specialProjectiles.add(egg);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        // Handle Bridge Egg
        if (projectile instanceof Egg && projectile.hasMetadata("bridge_egg")) {
            handleBridgeEgg(event, (Egg) projectile);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !event.hasItem()) {
            return;
        }

        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        // Handle TNT
        if (item.getType() == Material.TNT &&
            (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            handleTNTJump(player, item);
        }

        // Handle Fireball
        if (item.getType() == Material.FIRE_CHARGE &&
            (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);
            handleFireball(player, item);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Handle fireball self-damage for jumping
        if (event.getDamager() instanceof Fireball) {
            Fireball fireball = (Fireball) event.getDamager();

            if (fireball.hasMetadata("bedwars_fireball") && event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player shooter = (Player) fireball.getShooter();

                if (shooter != null && shooter.equals(player)) {
                    // Reduce self-damage for jumping
                    event.setDamage(event.getDamage() * 0.5);
                }
            }
        }

        // Handle TNT self-damage for jumping
        if (event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();

            if (tnt.hasMetadata("bedwars_tnt") && event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();

                if (tnt.getMetadata("bedwars_tnt").get(0).value() != null &&
                    tnt.getMetadata("bedwars_tnt").get(0).value().equals(player.getUniqueId())) {
                    // Reduce self-damage for jumping
                    event.setDamage(event.getDamage() * 0.5);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        // Reduce block damage from special TNT and fireballs in bedwars
        if (entity != null && (entity.hasMetadata("bedwars_tnt") || entity.hasMetadata("bedwars_fireball"))) {
            // Remove 50% of block breaks
            event.blockList().removeIf(block -> Math.random() < 0.5);
        }
    }

    /**
     * Handle Bridge Egg - creates a bridge when thrown
     */
    private void handleBridgeEgg(ProjectileHitEvent event, Egg egg) {
        if (!(egg.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) egg.getShooter();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);

        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        Location hitLoc = egg.getLocation();

        // Get the direction the egg was traveling
        Vector direction = egg.getVelocity().normalize();

        // Create bridge blocks (up to 20 blocks or until hitting solid ground)
        new BukkitRunnable() {
            int blocksPlaced = 0;
            Location currentLoc = hitLoc.clone();

            @Override
            public void run() {
                if (blocksPlaced >= 20) {
                    cancel();
                    return;
                }

                Block block = currentLoc.getBlock();
                Block below = currentLoc.clone().subtract(0, 1, 0).getBlock();

                // Place block if air and not too far down
                if (block.getType() == Material.AIR && below.getType() == Material.AIR) {
                    below.setType(Material.WHITE_WOOL);
                    blocksPlaced++;
                }

                // Stop if we hit solid ground
                if (below.getType() != Material.AIR && below.getType() != Material.WATER &&
                    below.getType() != Material.LAVA && blocksPlaced > 0) {
                    cancel();
                    return;
                }

                // Move forward
                currentLoc.add(direction.clone().multiply(0.5));
            }
        }.runTaskTimer(plugin, 0L, 2L);

        egg.remove();
    }

    /**
     * Handle Fireball - launches a fireball that can be used for jumping
     */
    private void handleFireball(Player player, ItemStack item) {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setMetadata("bedwars_fireball", new FixedMetadataValue(plugin, player.getUniqueId()));
        fireball.setYield(2.0f); // Explosion power
        fireball.setIsIncendiary(false); // Don't set blocks on fire

        // Apply velocity boost
        Vector direction = player.getLocation().getDirection();
        fireball.setVelocity(direction.multiply(2.0));

        specialProjectiles.add(fireball);

        // Remove one fireball from inventory
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }

        player.sendMessage("§aFireball launched!");
    }

    /**
     * Handle TNT - spawns primed TNT that can be used for jumping
     */
    private void handleTNTJump(Player player, ItemStack item) {
        Location spawnLoc = player.getLocation().clone().add(0, 0.5, 0);

        TNTPrimed tnt = player.getWorld().spawn(spawnLoc, TNTPrimed.class);
        tnt.setMetadata("bedwars_tnt", new FixedMetadataValue(plugin, player.getUniqueId()));
        tnt.setFuseTicks(40); // 2 seconds fuse
        tnt.setYield(2.0f); // Explosion power

        // Apply small upward velocity
        Vector velocity = player.getLocation().getDirection().multiply(0.5).setY(0.2);
        tnt.setVelocity(velocity);

        specialProjectiles.add(tnt);

        // Remove one TNT from inventory
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().remove(item);
        }

        player.sendMessage("§aTNT deployed!");
    }

    /**
     * Clean up tracked projectiles
     */
    public void cleanup() {
        for (Entity entity : specialProjectiles) {
            if (entity != null && entity.isValid()) {
                entity.remove();
            }
        }
        specialProjectiles.clear();
    }
}
