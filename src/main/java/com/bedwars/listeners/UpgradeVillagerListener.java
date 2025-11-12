package com.bedwars.listeners;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class UpgradeVillagerListener implements Listener {

    private final BedwarsPlugin plugin;

    public UpgradeVillagerListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        // Check if entity is a villager
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }

        // Check if player is in a game
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }

        // Check if villager has upgrade NPC name
        if (entity.customName() != null &&
            entity.customName().toString().contains("Upgrade")) {
            event.setCancelled(true);
            plugin.getGeneratorUpgradeManager().openUpgradeGUI(player);
        }
    }
}
