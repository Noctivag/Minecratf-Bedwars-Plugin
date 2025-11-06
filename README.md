# Minecraft Bedwars Plugin

Ein vollständig ausgestattetes Hypixel-Stil Bedwars Plugin für Minecraft 1.21 (Spigot/Paper) mit Multi-Modus Support!

## 🎮 Features

### Spielmodi (wie Hypixel!)
- **Solo (1v1v1v1v1v1v1v1)**: 8 Teams mit je 1 Spieler (max. 8 Spieler)
- **Doubles (2v2v2v2v2v2v2v2)**: 8 Teams mit je 2 Spielern (max. 16 Spieler)  
- **3v3v3v3**: 4 Teams mit je 3 Spielern (max. 12 Spieler)
- **4v4v4v4**: 4 Teams mit je 4 Spielern (max. 16 Spieler)
- **Mega Doubles**: 16 Teams mit je 2 Spielern (max. 32 Spieler)
- **Mega 4v4**: 8 Teams mit je 4 Spielern (max. 32 Spieler)

### Core Gameplay
- **Team-basiertes PvP**: Bis zu 8 Teams (Rot, Blau, Grün, Gelb, Aqua, Weiß, Pink, Grau)
- **Bett-Mechanik**: Beschütze dein Bett zum Respawnen; zerstöre gegnerische Betten für finale Kills
- **Ressourcen-Spawner**: Eisen, Gold, Diamant und Smaragd-Generatoren mit anpassbaren Spawn-Raten
- **Shop-System**: Kompletter Shop mit 40+ Items in 7 Kategorien (Blöcke, Waffen, Rüstung, Tools, Essen, Tränke, Utility)
- **Team-Upgrades**: Sharpness, Protection, Haste und mehr
- **Spieler-Statistiken**: Verfolge Kills, Deaths, Wins, zerstörte Betten und mehr
- **Live-Scoreboard**: Echtzeit-Spielinformationen und Team-Status

### Intuitive GUI
- **Spielmodus-Selektor**: Wähle deinen bevorzugten Spielmodus
- **Arena-Browser**: Sieh alle verfügbaren Arenas und deren Status
- **Einfacher Beitritt**: Ein Klick zum Joinen!

### Map-Verwaltung
- **Einfacher Map-Import**: Simple Commands zum Einrichten neuer Arenas
- **Admin-Tools**: Konfiguriere Spawn-Punkte, Bett-Positionen und Ressourcen-Spawner
- **Multi-Arena-Support**: Führe mehrere Spiele gleichzeitig aus
- **Modus-spezifische Arenas**: Erstelle dedizierte Arenas für jeden Spielmodus

### Admin Features
Komplettes Setup-System mit folgenden konfigurierbaren Elementen:
- Spielmodus-Auswahl pro Arena
- Lobby-Spawn-Punkt
- Spectator-Spawn-Punkt
- Team-Bett-Positionen
- Team-Spawn-Punkte
- Ressourcen-Spawner-Positionen (teamspezifisch oder geteilt)
- Arena Enable/Disable-Funktionalität

## 📦 Installation

1. Lade die Plugin-JAR-Datei von den Releases herunter
2. Platziere sie im `plugins`-Ordner deines Servers
3. Starte deinen Server neu
4. Konfiguriere das Plugin mit den unten aufgeführten Commands

## 🔨 Building from Source

```bash
git clone https://github.com/Noctivag/Minecratf-Bedwars-Plugin.git
cd Minecratf-Bedwars-Plugin
mvn clean package
```

Die kompilierte JAR-Datei befindet sich im `target`-Ordner.

## 📝 Commands

### Spieler-Commands
- `/bw play` - Öffne den Spielmodus-Selektor (GUI)
- `/bw join <arena>` - Trete einer bestimmten Bedwars-Arena bei
- `/bw leave` - Verlasse dein aktuelles Spiel
- `/bw stats` - Zeige deine Statistiken an
- `/bw list` - Liste alle verfügbaren Arenas auf

### Admin-Commands
- `/bwa setup <arena> [modus]` - Beginne mit dem Setup einer neuen Arena (optional mit Spielmodus)
  - Modi: `SOLO`, `DOUBLES`, `THREES`, `FOURS`, `MEGA_DOUBLES`, `MEGA_FOURS`
- `/bwa setmode <modus>` - Setze den Spielmodus für die aktuelle Arena
- `/bwa setlobby` - Setze den Lobby-Spawn-Punkt für die aktuelle Arena
- `/bwa setspectator` - Setze den Spectator-Spawn-Punkt
- `/bwa setbed <team>` - Setze die Bett-Position eines Teams
- `/bwa setspawn <team>` - Setze den Spawn-Punkt eines Teams
- `/bwa setresource <typ> [team]` - Füge einen Ressourcen-Spawner hinzu
  - Typen: `IRON`, `GOLD`, `DIAMOND`, `EMERALD`
  - Optionaler Team-Parameter für teamspezifische Spawner
- `/bwa savearena` - Speichere und aktiviere die aktuelle Arena
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
4. **Shop**: Right-click villagers at your base to open the shop
   - Buy blocks, weapons, armor, tools, food, potions, and utility items
   - Items are organized into 7 easy-to-navigate categories
   - Permanent armor upgrades available
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