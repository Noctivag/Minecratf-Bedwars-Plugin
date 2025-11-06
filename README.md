# Minecraft Bedwars Plugin

A fully-featured Hypixel-style Bedwars plugin for Minecraft 1.21 (Spigot/Paper).

## Features

### Core Gameplay
- **Team-based PvP**: Up to 8 teams (Red, Blue, Green, Yellow, Aqua, White, Pink, Gray)
- **Bed Mechanics**: Protect your bed to respawn; destroy enemy beds for final kills
- **Resource Spawners**: Iron, Gold, Diamond, and Emerald generators with customizable spawn rates
- **Team Upgrades**: Sharpness, Protection, Haste, and more
- **Player Statistics**: Track kills, deaths, wins, beds destroyed, and more

### Map Management
- **Easy Map Import**: Simple commands to set up new arenas
- **Admin Tools**: Configure spawn points, bed locations, and resource spawners
- **Multi-Arena Support**: Run multiple games simultaneously

### Admin Features
Complete setup system with the following configurable elements:
- Lobby spawn point
- Team bed locations
- Team spawn points
- Resource spawner locations (team-specific or shared)
- Arena enable/disable functionality

## Installation

1. Download the plugin JAR file from releases
2. Place it in your server's `plugins` folder
3. Restart your server
4. Configure the plugin using the commands below

## Building from Source

```bash
git clone https://github.com/Noctivag/Minecratf-Bedwars-Plugin.git
cd Minecratf-Bedwars-Plugin
mvn clean package
```

The compiled JAR will be in the `target` folder.

## Commands

### Player Commands
- `/bw join <arena>` - Join a Bedwars game
- `/bw leave` - Leave your current game
- `/bw stats` - View your statistics
- `/bw list` - List available arenas

### Admin Commands
- `/bwa setup <arena>` - Start setting up a new arena
- `/bwa setlobby` - Set the lobby spawn point for the current arena
- `/bwa setbed <team>` - Set a team's bed location
- `/bwa setspawn <team>` - Set a team's spawn point
- `/bwa setresource <type> [team]` - Add a resource spawner
  - Types: `IRON`, `GOLD`, `DIAMOND`, `EMERALD`
  - Optional team parameter for team-specific spawners
- `/bwa savearena` - Save and enable the current arena
- `/bwa deletearena <arena>` - Delete an arena
- `/bwa list` - List all arenas
- `/bwa enable <arena>` - Enable an arena
- `/bwa disable <arena>` - Disable an arena

### Teams
Available teams: `RED`, `BLUE`, `GREEN`, `YELLOW`, `AQUA`, `WHITE`, `PINK`, `GRAY`

## Setting Up an Arena

1. **Start Setup**
   ```
   /bwa setup MyArena
   ```

2. **Set Lobby Spawn**
   - Stand where players should spawn when joining
   ```
   /bwa setlobby
   ```

3. **Configure Teams**
   For each team you want (minimum 2 teams):
   
   - Set bed location (stand at the bed):
   ```
   /bwa setbed RED
   ```
   
   - Set spawn point (where players respawn):
   ```
   /bwa setspawn RED
   ```
   
   Repeat for other teams (BLUE, GREEN, YELLOW, etc.)

4. **Add Resource Spawners**
   
   - Team-specific spawners (e.g., iron and gold at each base):
   ```
   /bwa setresource IRON RED
   /bwa setresource GOLD RED
   /bwa setresource IRON BLUE
   /bwa setresource GOLD BLUE
   ```
   
   - Shared spawners (e.g., diamonds and emeralds in the center):
   ```
   /bwa setresource DIAMOND
   /bwa setresource EMERALD
   ```

5. **Save the Arena**
   ```
   /bwa savearena
   ```

Your arena is now ready! Players can join with `/bw join MyArena`

## Configuration

Edit `config.yml` to customize:

- Minimum/maximum players
- Countdown duration
- Resource spawn rates
- Team upgrade costs
- Messages and colors
- Lobby location

Example configuration:
```yaml
game:
  min-players: 2
  max-players: 16
  countdown-seconds: 10
  
  spawners:
    iron-spawn-rate: 1
    gold-spawn-rate: 8
    diamond-spawn-rate: 30
    emerald-spawn-rate: 60
```

## Permissions

- `bedwars.admin` - Access to admin commands (default: op)
- `bedwars.play` - Ability to join games (default: true)

## How to Play

1. **Join a Game**: `/bw join <arena>`
2. **Objective**: Destroy all enemy beds and eliminate all players
3. **Resources**: Collect iron, gold, diamonds, and emeralds from spawners
4. **Shop**: Use resources to buy items and upgrades (coming soon)
5. **Win**: Be the last team standing!

## Game Mechanics

### Respawning
- If your bed is alive, you respawn after 5 seconds
- If your bed is destroyed, you get one final life
- Final kills eliminate players permanently

### Teams
- Players are automatically balanced across teams
- Team damage is disabled
- Teams can have up to 4 players each (configurable per arena)

### Statistics
Track your performance:
- Kills and Deaths
- Final Kills and Final Deaths
- Beds Destroyed
- Wins and Losses
- K/D Ratio and Win Rate

## Requirements

- Minecraft 1.21 or higher
- Spigot or Paper server
- Java 21 or higher

## Support

For issues, questions, or suggestions, please open an issue on GitHub.

## License

This project is open source and available under the MIT License.

## Credits

Inspired by Hypixel Bedwars