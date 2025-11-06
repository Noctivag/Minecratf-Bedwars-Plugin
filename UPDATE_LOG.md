# Update Log - Multi-Modus Support

## Datum: 6. November 2025

### 🎮 Neue Features

#### Mehrere Spielmodi
Das Plugin unterstützt jetzt 6 verschiedene Spielmodi, ähnlich wie Hypixel Bedwars:

1. **Solo** - 8 Teams mit 1 Spieler (max. 8)
2. **Doubles** - 8 Teams mit 2 Spielern (max. 16)
3. **Threes** - 4 Teams mit 3 Spielern (max. 12)
4. **Fours** - 4 Teams mit 4 Spielern (max. 16)
5. **Mega Doubles** - 16 Teams mit 2 Spielern (max. 32)
6. **Mega Fours** - 8 Teams mit 4 Spielern (max. 32)

#### GUI-System
- **Spielmodus-Selektor**: Visuelles Menü zur Auswahl des gewünschten Modus
- **Arena-Browser**: Zeigt alle verfügbaren Arenas für den gewählten Modus
- **Live-Updates**: Spieleranzahl und Status werden in Echtzeit angezeigt

### 📁 Neue Dateien

1. **GameMode.java** (`src/main/java/com/bedwars/models/GameMode.java`)
   - Enum mit allen Spielmodi
   - Konfiguration für Teamgröße, max. Teams und max. Spieler
   - Hilfsmethoden zur Modus-Verwaltung

2. **ArenaSelectorGUI.java** (`src/main/java/com/bedwars/gui/ArenaSelectorGUI.java`)
   - GUI für Spielmodus-Auswahl
   - GUI für Arena-Auswahl innerhalb eines Modus
   - Visuelle Darstellung mit Items und Lore

3. **GUIListener.java** (`src/main/java/com/bedwars/listeners/GUIListener.java`)
   - Event-Handler für GUI-Klicks
   - Verarbeitung von Modus- und Arena-Auswahl
   - Automatisches Joinen von Spielen

4. **GAMEMODES.md**
   - Vollständige Dokumentation aller Spielmodi
   - Setup-Anleitung für Admins
   - Best Practices für verschiedene Modi

### 🔧 Geänderte Dateien

#### Arena.java
- Neue Felder: `gameMode`, `spectatorSpawn`
- Automatische Anpassung von `minPlayers`/`maxPlayers` basierend auf Modus
- Serialisierung des Spielmodus

#### BedwarsGame.java
- Team-Zuweisung berücksichtigt jetzt Teamgröße des Modus
- Verbesserte Spieler-Beitritt-Nachrichten mit Team-Info
- Fix: org.bukkit.GameMode vs. com.bedwars.models.GameMode Konflikt

#### BedwarsAdminCommand.java
- Neuer Command: `/bwa setmode <modus>`
- Erweiterter `/bwa setup` Command mit optionalem Modus-Parameter
- Neuer Command: `/bwa setspectator`
- Aktualisierte Hilfe-Nachrichten mit allen Modi
- Arena-Liste zeigt jetzt Spielmodus an

#### BedwarsCommand.java
- Neuer Command: `/bw play` - Öffnet GUI
- `/bw join` funktioniert weiterhin für direkten Beitritt
- Aktualisierte `/bw list` zeigt Spielmodus pro Arena

#### BedwarsPlugin.java
- Registrierung des GUIListener
- Getter für GUIListener hinzugefügt
- Startup-Nachricht mit verfügbaren Modi

#### GameManager.java
- Neue Methoden: `hasGame(String)` und `getGame(String)`
- Ermöglicht Prüfung und Zugriff auf Spiele nach Arena-Name

#### ArenaManager.java
- Neue Methode: `addArena(Arena)`
- Ermöglicht manuelles Hinzufügen von Arenas

#### MessageUtil.java
- Neue Methode: `stripColor(String)`
- Entfernt Farbcodes für Text-Verarbeitung

### 🎨 Verbesserte Features

#### Team-Zuweisung
- Automatische Balance basierend auf Teamgröße des Modus
- Verhindert Überfüllung von Teams
- Gleichmäßige Verteilung der Spieler

#### Arena-Setup
- Admins können Modus beim Setup direkt angeben
- Modus kann nachträglich geändert werden
- Validierung von Team-Konfigurationen pro Modus

### 📊 Technische Details

#### Kompatibilität
- **Minecraft Version**: 1.21.x
- **Java Version**: 21
- **API**: Paper/Spigot 1.21.1

#### Dependencies
Keine neuen Dependencies hinzugefügt - nutzt weiterhin nur:
- Paper API 1.21.1
- Maven Shade Plugin

### 🐛 Behobene Bugs
- GameMode-Namenskonflikt zwischen Bukkit und Plugin behoben
- Korrekte Team-Größen-Validierung implementiert
- Spectator-Spawn fehlte (jetzt hinzugefügt)

### 📖 Dokumentation
- README.md komplett überarbeitet (auf Deutsch)
- Neue GAMEMODES.md mit detaillierten Infos
- Inline-Kommentare in neuen Klassen

### 🚀 Performance
- Keine negativen Auswirkungen auf Performance
- GUI-Operationen sind client-seitig
- Effiziente Modus-Validierung

### 🔜 Zukünftige Erweiterungen
Mögliche zukünftige Features:
- Custom Game Modes (admin-konfigurierbare Modi)
- Ranked Modes mit separaten Stats
- Private Games
- Tournament Mode
- Map Voting System

### 💾 Build-Informationen
```
Build: SUCCESS
JAR-Datei: target/BedwarsPlugin-1.0.0.jar
Größe: ~77 KB
Klassen: 23
```

## Migration Guide

### Für Server-Admins
1. Backup der bestehenden Arena-Konfigurationen erstellen
2. Plugin updaten
3. Server neustarten
4. Bestehende Arenas werden mit Modus "FOURS" (Standard) geladen
5. Mit `/bwa setmode` können Modi nachträglich angepasst werden

### Für Entwickler
Bei Erweiterung des Plugins beachten:
- `com.bedwars.models.GameMode` immer voll qualifiziert verwenden
- `org.bukkit.GameMode` für Bukkit-GameModes nutzen
- GUI-Events in GUIListener behandeln
- Neue Spielmodi in GameMode Enum hinzufügen
