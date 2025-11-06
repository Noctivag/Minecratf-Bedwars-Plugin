# Developer API Documentation

This document provides information for developers who want to extend or integrate with the Bedwars Plugin.

## Getting Started

### Adding as a Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.bedwars</groupId>
    <artifactId>BedwarsPlugin</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

Add to your `plugin.yml`:
```yaml
depend: [BedwarsPlugin]
```

## Core Classes

### BedwarsPlugin

Main plugin class - provides access to all managers.

```java
BedwarsPlugin plugin = BedwarsPlugin.getInstance();
```

**Methods:**
- `getArenaManager()` - Manages arenas
- `getGameManager()` - Manages games and players
- `getScoreboardManager()` - Manages scoreboards

### ArenaManager

Manages arena creation, loading, and saving.

```java
ArenaManager arenaManager = plugin.getArenaManager();

// Get an arena
Arena arena = arenaManager.getArena("arenaName");

// Get all available arenas
List<Arena> arenas = arenaManager.getAvailableArenas();

// Save an arena
arenaManager.saveArena(arena);

// Delete an arena
arenaManager.deleteArena("arenaName");
```

### GameManager

Manages game instances and player participation.

```java
GameManager gameManager = plugin.getGameManager();

// Check if player is in a game
boolean inGame = gameManager.isInGame(player);

// Get player's current game
BedwarsGame game = gameManager.getPlayerGame(player);

// Join a game
boolean success = gameManager.joinGame(player, "arenaName");

// Leave a game
boolean left = gameManager.leaveGame(player);

// Get player statistics
PlayerStats stats = gameManager.getPlayerStats(player);
```

### BedwarsGame

Represents an active game instance.

```java
BedwarsGame game = gameManager.getPlayerGame(player);

// Get game state
GameState state = game.getState();

// Get player's team
TeamColor team = game.getPlayerTeam(player);

// Get all players
List<Player> players = game.getAllPlayers();

// Get team players
List<Player> teamPlayers = game.getTeamPlayers(TeamColor.RED);

// Get team upgrades
TeamUpgrades upgrades = game.getTeamUpgrades(TeamColor.RED);

// Broadcast message to all players
game.broadcastMessage("Message");
```

## Models

### Arena

Represents a Bedwars arena configuration.

```java
Arena arena = new Arena("MyArena");
arena.setLobbySpawn(location);
arena.setDisplayName("My Awesome Arena");

// Add a team
TeamData teamData = new TeamData(TeamColor.RED);
teamData.setBedLocation(bedLocation);
teamData.setSpawnLocation(spawnLocation);
arena.addTeam(TeamColor.RED, teamData);

// Add resource spawner
ResourceSpawner spawner = new ResourceSpawner(
    location,
    ResourceSpawner.ResourceType.IRON,
    TeamColor.RED
);
arena.addResourceSpawner(spawner);
```

### PlayerStats

Player statistics tracking.

```java
PlayerStats stats = gameManager.getPlayerStats(player);

// Get statistics
int kills = stats.getKills();
int deaths = stats.getDeaths();
double kd = stats.getKDRatio();
int wins = stats.getWins();
int bedsDestroyed = stats.getBedsDestroyed();

// Modify statistics (only during games)
stats.addKill();
stats.addDeath();
stats.addFinalKill();
stats.addBedDestroyed();
stats.addWin();
```

### TeamUpgrades

Team upgrade tracking.

```java
TeamUpgrades upgrades = game.getTeamUpgrades(TeamColor.RED);

// Get upgrade levels
int sharpness = upgrades.getSharpnessLevel();
int protection = upgrades.getProtectionLevel();
int haste = upgrades.getHasteLevel();

// Upgrade
upgrades.upgradeSharpness();
upgrades.upgradeProtection();
upgrades.upgradeHaste();

// Check team upgrades
boolean hasHealPool = upgrades.hasHealPool();
boolean hasTrap = upgrades.hasTrap();
```

## Events

The plugin fires standard Bukkit events. You can listen to them in your plugin:

```java
@EventHandler
public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    
    // Check if player is in a Bedwars game
    BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
    if (game != null) {
        // Player is in a Bedwars game
        // Game handles death - don't interfere
    }
}

@EventHandler
public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
    
    if (game != null) {
        // Player is in game - check if it's a bed
        // Game handles bed breaking
    }
}
```

## Custom Shop Items

You can add custom shop items programmatically:

```java
// Create a custom shop item
ShopItem customItem = new ShopItem(
    "Custom Item",                      // Name
    Material.DIAMOND,                   // Material
    1,                                  // Amount
    ShopItem.CurrencyType.EMERALD,     // Currency
    10,                                 // Cost
    ShopItem.ShopCategory.UTILITY      // Category
);

customItem.addLore("§7This is a custom item");
customItem.setEnchanted(true);

// Items are currently static - you'd need to modify ShopManager
// to support dynamic items
```

## Extending Game Mechanics

### Custom Game Events

Create custom events for Bedwars actions:

```java
public class BedDestroyEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final Player breaker;
    private final TeamColor team;
    
    public BedDestroyEvent(Player breaker, TeamColor team) {
        this.breaker = breaker;
        this.team = team;
    }
    
    public Player getBreaker() { return breaker; }
    public TeamColor getTeam() { return team; }
    
    @Override
    public boolean isCancelled() { return cancelled; }
    
    @Override
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    
    @Override
    public HandlerList getHandlers() { return HANDLERS; }
    
    public static HandlerList getHandlerList() { return HANDLERS; }
}
```

## Utility Methods

### MessageUtil

Format messages with color codes:

```java
String colored = MessageUtil.color("&aGreen &cRed &eYellow");
player.sendMessage(colored);

// Get configured message with placeholders
String message = MessageUtil.getMessage("game-starting", 
    Map.of("seconds", "10"));
```

## Best Practices

### Checking Game State

Always check if a player is in a game before modifying their state:

```java
BedwarsGame game = plugin.getGameManager().getPlayerGame(player);
if (game != null && game.getState() == GameState.ACTIVE) {
    // Player is in active game - be careful with modifications
}
```

### Working with Teams

```java
TeamColor team = game.getPlayerTeam(player);
if (team != null) {
    TeamData teamData = game.getArena().getTeam(team);
    Location spawn = teamData.getSpawnLocation();
    boolean bedAlive = teamData.isBedAlive();
}
```

### Safe Arena Access

```java
Arena arena = arenaManager.getArena("MyArena");
if (arena != null && arena.isEnabled() && arena.isComplete()) {
    // Arena is ready to use
}
```

## Threading

The plugin uses the Bukkit scheduler for async tasks:

- Resource spawners run on async timers
- Game tick runs every second
- All game modifications should be on main thread

```java
// Run on main thread
Bukkit.getScheduler().runTask(plugin, () -> {
    // Safe to modify game state
});

// Run async (read-only operations)
Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
    // Don't modify game state
});
```

## Configuration Access

```java
FileConfiguration config = plugin.getConfig();

// Get values
int minPlayers = config.getInt("game.min-players");
int ironRate = config.getInt("game.spawners.iron-spawn-rate");
String prefix = config.getString("messages.prefix");

// Messages with placeholders
String msg = config.getString("messages.game-starting");
msg = msg.replace("{seconds}", "10");
```

## Examples

### Custom Statistic Tracking

```java
public class CustomStatsTracker implements Listener {
    
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        BedwarsGame game = plugin.getGameManager().getPlayerGame(victim);
        if (game != null && killer != null) {
            // Track custom stats
            // Bedwars plugin already tracks kills/deaths
            // Add your own tracking here
        }
    }
}
```

### Custom Team Upgrade

```java
public class CustomUpgradeManager {
    
    public void applyCustomUpgrade(BedwarsGame game, TeamColor team) {
        TeamUpgrades upgrades = game.getTeamUpgrades(team);
        
        // Apply custom effects to team
        for (Player player : game.getTeamPlayers(team)) {
            // Give custom potion effects, items, etc.
        }
    }
}
```

## Support

For questions about the API:
1. Check this documentation
2. Review the source code
3. Open an issue on GitHub with the "question" label

## Future API Improvements

Planned API enhancements:
- Event system for game actions
- Hook for custom shop items
- Plugin messaging API
- Database integration hooks
- Custom game mode support
