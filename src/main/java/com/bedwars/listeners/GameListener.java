package com.bedwars.listeners;

import com.bedwars.BedwarsPlugin;
import com.bedwars.game.BedwarsGame;
import com.bedwars.models.GameState;
import com.bedwars.models.TeamColor;
import com.bedwars.models.TeamData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GameListener implements Listener {
    
    private final BedwarsPlugin plugin;
    
    public GameListener(BedwarsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }
        
        Block block = event.getBlock();
        
        // Check if it's a bed
        if (block.getType() == Material.RED_BED || 
            block.getType() == Material.BLUE_BED ||
            block.getType() == Material.GREEN_BED ||
            block.getType() == Material.YELLOW_BED ||
            block.getType() == Material.WHITE_BED ||
            block.getType() == Material.BLACK_BED ||
            block.getType() == Material.LIGHT_GRAY_BED ||
            block.getType() == Material.GRAY_BED ||
            block.getType() == Material.PINK_BED ||
            block.getType() == Material.LIME_BED ||
            block.getType() == Material.CYAN_BED ||
            block.getType() == Material.PURPLE_BED ||
            block.getType() == Material.MAGENTA_BED ||
            block.getType() == Material.ORANGE_BED ||
            block.getType() == Material.BROWN_BED) {
            
            handleBedBreak(event, game, player, block);
        }
    }
    
    private void handleBedBreak(BlockBreakEvent event, BedwarsGame game, Player breaker, Block bed) {
        TeamColor breakerTeam = game.getPlayerTeam(breaker);
        
        // Find which team's bed was broken
        for (TeamColor team : game.getArena().getTeams().keySet()) {
            TeamData teamData = game.getArena().getTeam(team);
            
            if (teamData.getBedLocation() != null && 
                teamData.getBedLocation().getBlock().getLocation().distance(bed.getLocation()) < 2) {
                
                // Can't break own bed
                if (team == breakerTeam) {
                    event.setCancelled(true);
                    breaker.sendMessage("§cYou cannot break your own bed!");
                    return;
                }
                
                // Bed is already broken
                if (!teamData.isBedAlive()) {
                    event.setCancelled(true);
                    return;
                }
                
                // Break the bed
                game.handleBedBreak(breaker, team);
                return;
            }
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        
        if (game == null) {
            return;
        }
        
        // Only allow block placement during active game
        if (game.getState() != GameState.ACTIVE) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(victim);
        
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }
        
        // Get killer
        Player killer = victim.getKiller();
        
        // Handle death in game
        game.handlePlayerDeath(victim, killer);
        
        // Clear drops in bedwars
        event.getDrops().clear();
        event.setDroppedExp(0);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
        
        if (game == null) {
            return;
        }
        
        // Respawn will be handled by the game
        TeamColor team = game.getPlayerTeam(player);
        if (team != null) {
            TeamData teamData = game.getArena().getTeam(team);
            if (teamData.getSpawnLocation() != null) {
                event.setRespawnLocation(teamData.getSpawnLocation());
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        
        BedwarsGame game = plugin.getGameManager().getPlayerGame(victim);
        
        if (game == null || game.getState() != GameState.ACTIVE) {
            return;
        }
        
        // Prevent team damage
        TeamColor victimTeam = game.getPlayerTeam(victim);
        TeamColor damagerTeam = game.getPlayerTeam(damager);
        
        if (victimTeam == damagerTeam) {
            event.setCancelled(true);
        }
    }
}
