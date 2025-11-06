# Features Documentation

## Complete Feature List

### Core Gameplay
- ✅ Team-based multiplayer (2-8 teams)
- ✅ Bed protection mechanics
- ✅ Respawn system (with/without bed)
- ✅ Final kills
- ✅ Team elimination
- ✅ Win conditions

### Resource System
- ✅ Iron generators (team-specific)
- ✅ Gold generators (team-specific)
- ✅ Diamond generators (shared)
- ✅ Emerald generators (shared)
- ✅ Configurable spawn rates
- ✅ Automatic item stacking limits

### Shop System
Complete item shop with 7 categories:

#### Blocks
- Wool (bridging)
- Hardened Clay (blast-resistant)
- End Stone (very blast-resistant)
- Ladders
- Oak Planks
- Obsidian (ultimate protection)

#### Weapons
- Stone Sword
- Iron Sword
- Diamond Sword
- Knockback Stick

#### Armor
- Chainmail Armor (permanent)
- Iron Armor (permanent)
- Diamond Armor (permanent)

#### Tools
- Wooden Pickaxe (Efficiency I)
- Stone Pickaxe (Efficiency II)
- Iron Pickaxe (Efficiency III, Unbreaking I)
- Diamond Pickaxe (Efficiency V, Unbreaking II)
- Wooden Axe
- Shears

#### Food
- Apple
- Bread
- Golden Apple

#### Potions
- Speed Potion
- Jump Potion
- Invisibility Potion

#### Utility
- Arrows
- Bow
- Power Bow (enchanted)
- TNT
- Ender Pearl
- Water Bucket
- Bridge Egg
- Fireball

### Team Upgrades
- Sharpness (4 tiers)
- Protection (4 tiers)
- Haste (2 tiers)
- Heal Pool
- Trap

### Player Statistics
Tracked statistics:
- Kills
- Deaths
- K/D Ratio
- Final Kills
- Final Deaths
- Beds Destroyed
- Wins
- Losses
- Games Played
- Win Rate

### Scoreboard
Real-time scoreboard showing:
- Team status (bed alive/destroyed)
- Player count per team
- Your team indicator
- Personal stats (kills, final kills, beds destroyed)

### Arena Management
- ✅ Multi-arena support
- ✅ Easy arena creation
- ✅ Configurable team spawn points
- ✅ Configurable bed locations
- ✅ Configurable resource spawners
- ✅ Arena enable/disable
- ✅ Arena validation
- ✅ Persistent storage (YAML)

### Admin Tools
Complete setup system:
- `/bwa setup <arena>` - Start arena setup
- `/bwa setlobby` - Set lobby spawn
- `/bwa setbed <team>` - Set bed location
- `/bwa setspawn <team>` - Set spawn point
- `/bwa setresource <type> [team]` - Add resource spawner
- `/bwa savearena` - Save and enable arena
- `/bwa deletearena <arena>` - Delete arena
- `/bwa list` - List all arenas
- `/bwa enable/disable <arena>` - Toggle arena

### Player Commands
- `/bw join <arena>` - Join a game
- `/bw leave` - Leave current game
- `/bw stats` - View statistics
- `/bw list` - List available arenas

### Game States
- WAITING - Waiting for players
- STARTING - Countdown active
- ACTIVE - Game in progress
- ENDING - Game ended, cleanup
- DISABLED - Arena disabled

### Configuration
Fully configurable:
- Minimum/maximum players
- Countdown duration
- Resource spawn rates
- Team upgrade costs
- All messages and colors
- Team settings

### Quality of Life
- ✅ Automatic team balancing
- ✅ Team damage prevention
- ✅ Automatic game cleanup
- ✅ Player disconnect handling
- ✅ Scoreboard updates
- ✅ Sound effects
- ✅ Color-coded messages
- ✅ Permission system

## Future Enhancements

Possible additions:
- [ ] Team upgrades GUI
- [ ] Cosmetics system
- [ ] Achievements
- [ ] Leaderboards
- [ ] Spectator mode improvements
- [ ] Custom trap types
- [ ] More team upgrade options
- [ ] Database storage for stats
- [ ] Party system
- [ ] Auto-respawn item protection
- [ ] Fireball and TNT jump mechanics
- [ ] Bridge egg mechanics
- [ ] Compass tracker
- [ ] Generator upgrades

## Technical Details

### Architecture
- **Plugin System**: Spigot/Paper 1.21+
- **Storage**: YAML file-based
- **Event-driven**: Bukkit event system
- **Manager Pattern**: Separate managers for arenas, games, scoreboards
- **Model Classes**: Clean separation of data and logic

### Performance
- Async tasks for resource spawners
- Efficient scoreboard updates
- Minimal tick operations
- Smart entity tracking

### Compatibility
- ✅ Spigot 1.21+
- ✅ Paper 1.21+ (recommended)
- ✅ Java 21+
- ✅ Maven build system

## Code Structure

```
src/main/java/com/bedwars/
├── BedwarsPlugin.java          # Main plugin class
├── commands/
│   ├── BedwarsCommand.java      # Player commands
│   └── BedwarsAdminCommand.java # Admin commands
├── game/
│   ├── BedwarsGame.java         # Game logic
│   ├── ShopItem.java            # Shop item definitions
│   └── ShopManager.java         # Shop system
├── listeners/
│   ├── GameListener.java        # Game events
│   ├── PlayerListener.java      # Player events
│   └── ShopListener.java        # Shop interactions
├── managers/
│   ├── ArenaManager.java        # Arena management
│   ├── GameManager.java         # Game instances
│   └── ScoreboardManager.java   # Scoreboards
├── models/
│   ├── Arena.java               # Arena data
│   ├── GameState.java           # Game states
│   ├── PlayerStats.java         # Player statistics
│   ├── ResourceSpawner.java     # Resource spawner
│   ├── TeamColor.java           # Team colors
│   ├── TeamData.java            # Team information
│   └── TeamUpgrades.java        # Team upgrades
└── utils/
    └── MessageUtil.java         # Message utilities
```
