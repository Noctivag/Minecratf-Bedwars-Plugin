package com.bedwars.game;

import com.bedwars.BedwarsPlugin;
import com.bedwars.models.*;
import com.bedwars.utils.MessageUtil;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class BedwarsGame {
    
    private final BedwarsPlugin plugin;
    private final Arena arena;
    private GameState state;
    
    private final Map<TeamColor, List<Player>> teams;
    private final Map<TeamColor, TeamUpgrades> teamUpgrades;
    private final Map<UUID, TeamColor> playerTeams;
    private final Set<TeamColor> aliveTeams;
    
    private BukkitTask countdownTask;
    private BukkitTask gameTask;
    private final List<BukkitTask> spawnerTasks;
    
    private int countdown;
    
    public BedwarsGame(BedwarsPlugin plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.state = GameState.WAITING;
        
        this.teams = new HashMap<>();
        this.teamUpgrades = new HashMap<>();
        this.playerTeams = new HashMap<>();
        this.aliveTeams = new HashSet<>();
        this.spawnerTasks = new ArrayList<>();
        
        // Initialize teams
        for (TeamColor color : arena.getTeams().keySet()) {
            teams.put(color, new ArrayList<>());
            teamUpgrades.put(color, new TeamUpgrades());
        }
    }
    
    public boolean addPlayer(Player player) {
        if (state != GameState.WAITING && state != GameState.STARTING) {
            return false;
        }
        
        // Check if game is full
        if (getTotalPlayers() >= arena.getMaxPlayers()) {
            return false;
        }
        
        // Find team with least players
        TeamColor assignedTeam = null;
        int minSize = Integer.MAX_VALUE;
        
        for (Map.Entry<TeamColor, List<Player>> entry : teams.entrySet()) {
            TeamData teamData = arena.getTeam(entry.getKey());
            int teamSize = entry.getValue().size();
            int maxTeamSize = arena.getGameMode().getTeamSize();
            
            // Only assign to teams that haven't reached max size
            if (teamSize < maxTeamSize && teamSize < minSize) {
                minSize = teamSize;
                assignedTeam = entry.getKey();
            }
        }
        
        if (assignedTeam == null) {
            return false; // All teams are full
        }
        
        // Add player to team
        teams.get(assignedTeam).add(player);
        playerTeams.put(player.getUniqueId(), assignedTeam);
        
        // Teleport to lobby
        player.teleport(arena.getLobbySpawn());
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.getInventory().clear();
        
        TeamData teamData = arena.getTeam(assignedTeam);
        
        // Send join message with team info
        broadcastMessage(MessageUtil.color("&e" + player.getName() + " &7joined &" + 
            assignedTeam.getChatColor() + assignedTeam.getDisplayName() + " Team &7(" + 
            getTotalPlayers() + "/" + arena.getMaxPlayers() + ")"));
        
        // Check if game should start
        if (getTotalPlayers() >= arena.getMinPlayers() && state == GameState.WAITING) {
            startCountdown();
        }
        
        return true;
    }
    
    public void removePlayer(Player player) {
        TeamColor team = playerTeams.remove(player.getUniqueId());
        if (team != null) {
            teams.get(team).remove(player);
        }
        
        // Reset player
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setHealth(20.0);
        player.setFoodLevel(20);
        
        if (state == GameState.STARTING && getTotalPlayers() < arena.getMinPlayers()) {
            cancelCountdown();
        }
        
        if (state == GameState.ACTIVE) {
            checkWinCondition();
        }
    }
    
    private void startCountdown() {
        state = GameState.STARTING;
        countdown = plugin.getConfig().getInt("game.countdown-seconds", 10);
        
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (countdown <= 0) {
                    startGame();
                    cancel();
                    return;
                }
                
                if (countdown <= 5 || countdown % 10 == 0) {
                    broadcastMessage(MessageUtil.getMessage("game-starting", 
                        Map.of("seconds", String.valueOf(countdown))));
                    playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
                }
                
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        state = GameState.WAITING;
        broadcastMessage(MessageUtil.color("&cCountdown cancelled! Not enough players."));
    }
    
    private void startGame() {
        state = GameState.ACTIVE;
        
        // Initialize alive teams
        for (TeamColor color : teams.keySet()) {
            if (!teams.get(color).isEmpty()) {
                aliveTeams.add(color);
                arena.getTeam(color).setBedAlive(true);
            }
        }
        
        broadcastMessage(MessageUtil.getMessage("game-started", Map.of()));
        
        // Teleport players to spawn points
        for (Map.Entry<TeamColor, List<Player>> entry : teams.entrySet()) {
            TeamData teamData = arena.getTeam(entry.getKey());
            for (Player player : entry.getValue()) {
                player.teleport(teamData.getSpawnLocation());
                player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                giveStartingItems(player, entry.getKey());
            }
        }
        
        // Start resource spawners
        startResourceSpawners();
        
        // Start game tick
        gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                gameTick();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void startResourceSpawners() {
        for (ResourceSpawner spawner : arena.getResourceSpawners()) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    spawnResource(spawner);
                }
            }.runTaskTimer(plugin, 0L, spawner.getSpawnRate() * 20L);
            
            spawnerTasks.add(task);
        }
    }
    
    private void spawnResource(ResourceSpawner spawner) {
        if (state != GameState.ACTIVE) {
            return;
        }
        
        Location loc = spawner.getLocation();
        ItemStack item = new ItemStack(spawner.getType().getMaterial(), 1);
        
        // Check if there are already too many items at this location
        long itemCount = loc.getWorld().getNearbyEntities(loc, 1, 1, 1).stream()
            .filter(e -> e instanceof Item)
            .count();
        
        if (itemCount < 32) {
            Item droppedItem = loc.getWorld().dropItem(loc.add(0.5, 0.5, 0.5), item);
            droppedItem.setPickupDelay(0);
        }
    }
    
    private void giveStartingItems(Player player, TeamColor team) {
        player.getInventory().clear();
        
        // Give wooden sword
        player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD));
    }
    
    private void gameTick() {
        // Update scoreboards
        for (Player player : getAllPlayers()) {
            plugin.getScoreboardManager().updateScoreboard(player, this);
        }
        
        // Apply team upgrades
        for (Map.Entry<TeamColor, List<Player>> entry : teams.entrySet()) {
            TeamUpgrades upgrades = teamUpgrades.get(entry.getKey());
            
            for (Player player : entry.getValue()) {
                if (player.isOnline()) {
                    // Apply haste
                    if (upgrades.getHasteLevel() > 0) {
                        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
                            org.bukkit.potion.PotionEffectType.HASTE, 
                            60, 
                            upgrades.getHasteLevel() - 1, 
                            true, 
                            false
                        ));
                    }
                }
            }
        }
    }
    
    public void handleBedBreak(Player breaker, TeamColor team) {
        TeamData teamData = arena.getTeam(team);
        
        if (!teamData.isBedAlive()) {
            return;
        }
        
        teamData.setBedAlive(false);
        
        String message = MessageUtil.getMessage("bed-destroyed", Map.of(
            "team", team.getColoredName(),
            "player", breaker.getName()
        ));
        
        broadcastMessage(message);
        playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);
        
        // Update stats
        plugin.getGameManager().getPlayerStats(breaker).addBedDestroyed();
    }
    
    public void handlePlayerDeath(Player victim, Player killer) {
        TeamColor victimTeam = playerTeams.get(victim.getUniqueId());
        TeamData teamData = arena.getTeam(victimTeam);
        
        boolean isFinalKill = !teamData.isBedAlive();
        
        if (killer != null) {
            if (isFinalKill) {
                broadcastMessage(MessageUtil.getMessage("final-kill", Map.of(
                    "victim", victim.getName(),
                    "killer", killer.getName()
                )));
                plugin.getGameManager().getPlayerStats(killer).addFinalKill();
                plugin.getGameManager().getPlayerStats(victim).addFinalDeath();
            } else {
                broadcastMessage(MessageUtil.getMessage("player-killed", Map.of(
                    "victim", victim.getName(),
                    "killer", killer.getName()
                )));
                plugin.getGameManager().getPlayerStats(killer).addKill();
                plugin.getGameManager().getPlayerStats(victim).addDeath();
            }
        }
        
        if (isFinalKill) {
            // Remove from game
            teams.get(victimTeam).remove(victim);
            victim.setGameMode(org.bukkit.GameMode.SPECTATOR);
            
            // Check if team is eliminated
            if (teams.get(victimTeam).isEmpty()) {
                aliveTeams.remove(victimTeam);
                broadcastMessage(MessageUtil.getMessage("team-eliminated", 
                    Map.of("team", victimTeam.getColoredName())));
                
                checkWinCondition();
            }
        } else {
            // Respawn player
            respawnPlayer(victim, victimTeam);
        }
    }
    
    private void respawnPlayer(Player player, TeamColor team) {
        new BukkitRunnable() {
            int seconds = 5;
            
            @Override
            public void run() {
                if (seconds <= 0) {
                    TeamData teamData = arena.getTeam(team);
                    player.teleport(teamData.getSpawnLocation());
                    player.setGameMode(org.bukkit.GameMode.SURVIVAL);
                    giveStartingItems(player, team);
                    cancel();
                    return;
                }
                
                player.sendMessage(MessageUtil.getMessage("respawn-in", 
                    Map.of("seconds", String.valueOf(seconds))));
                seconds--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    private void checkWinCondition() {
        if (aliveTeams.size() == 1) {
            TeamColor winner = aliveTeams.iterator().next();
            endGame(winner);
        } else if (aliveTeams.isEmpty()) {
            forceEnd();
        }
    }
    
    private void endGame(TeamColor winner) {
        state = GameState.ENDING;
        
        broadcastMessage(MessageUtil.getMessage("game-ended", 
            Map.of("team", winner.getColoredName())));
        
        // Update stats for winners
        for (Player player : teams.get(winner)) {
            plugin.getGameManager().getPlayerStats(player).addWin();
        }
        
        // Update stats for losers
        for (Map.Entry<TeamColor, List<Player>> entry : teams.entrySet()) {
            if (entry.getKey() != winner) {
                for (Player player : entry.getValue()) {
                    plugin.getGameManager().getPlayerStats(player).addLoss();
                }
            }
        }
        
        cleanup();
    }
    
    public void forceEnd() {
        state = GameState.ENDING;
        cleanup();
    }
    
    private void cleanup() {
        // Cancel all tasks
        if (countdownTask != null) {
            countdownTask.cancel();
        }
        if (gameTask != null) {
            gameTask.cancel();
        }
        for (BukkitTask task : spawnerTasks) {
            task.cancel();
        }
        spawnerTasks.clear();
        
        // Remove scoreboards
        for (Player player : getAllPlayers()) {
            plugin.getScoreboardManager().removeScoreboard(player);
        }
        
        // Teleport players back
        for (Player player : getAllPlayers()) {
            player.teleport(plugin.getConfig().getLocation("lobby"));
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.getInventory().clear();
            player.setHealth(20.0);
            player.setFoodLevel(20);
        }
        
        // Notify manager to remove this game
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getGameManager().endGame(BedwarsGame.this);
            }
        }.runTaskLater(plugin, 100L);
    }
    
    public List<Player> getAllPlayers() {
        List<Player> all = new ArrayList<>();
        for (List<Player> teamPlayers : teams.values()) {
            all.addAll(teamPlayers);
        }
        return all;
    }
    
    public void broadcastMessage(String message) {
        for (Player player : getAllPlayers()) {
            player.sendMessage(message);
        }
    }
    
    public void playSound(Sound sound) {
        for (Player player : getAllPlayers()) {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }
    
    public int getTotalPlayers() {
        return getAllPlayers().size();
    }
    
    // Getters
    public Arena getArena() { return arena; }
    public GameState getState() { return state; }
    public TeamColor getPlayerTeam(Player player) { return playerTeams.get(player.getUniqueId()); }
    public List<Player> getTeamPlayers(TeamColor team) { return teams.get(team); }
    public TeamUpgrades getTeamUpgrades(TeamColor team) { return teamUpgrades.get(team); }
}
