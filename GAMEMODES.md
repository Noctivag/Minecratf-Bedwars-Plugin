# Game Modes Dokumentation

## Übersicht

Das Bedwars Plugin unterstützt jetzt mehrere Spielmodi, ähnlich wie bei Hypixel Bedwars. Jeder Modus hat unterschiedliche Teamgrößen und maximale Spieleranzahlen.

## Verfügbare Spielmodi

### Solo (1v1v1v1v1v1v1v1)
- **Teamgröße:** 1 Spieler
- **Maximale Teams:** 8
- **Maximale Spieler:** 8
- **Minimale Spieler:** 4

Jeder Spieler ist sein eigenes Team. Ideal für intensive 1v1 Kämpfe!

### Doubles (2v2v2v2v2v2v2v2)
- **Teamgröße:** 2 Spieler
- **Maximale Teams:** 8
- **Maximale Spieler:** 16
- **Minimale Spieler:** 4

Der klassische 2er-Team Modus. Perfekt für Duo-Spieler!

### 3v3v3v3
- **Teamgröße:** 3 Spieler
- **Maximale Teams:** 4
- **Maximale Spieler:** 12
- **Minimale Spieler:** 2

Mittelgroße Teams mit 3 Spielern pro Team.

### 4v4v4v4
- **Teamgröße:** 4 Spieler
- **Maximale Teams:** 4
- **Maximale Spieler:** 16
- **Minimale Spieler:** 2

Der Standard-Modus mit 4 Spielern pro Team.

### Mega Doubles
- **Teamgröße:** 2 Spieler
- **Maximale Teams:** 16
- **Maximale Spieler:** 32
- **Minimale Spieler:** 8

Großer Modus mit vielen 2er-Teams für chaotische Matches!

### Mega 4v4
- **Teamgröße:** 4 Spieler
- **Maximale Teams:** 8
- **Maximale Spieler:** 32
- **Minimale Spieler:** 4

Großer Modus mit 4er-Teams für epische Schlachten!

## Setup für Admins

### Arena mit bestimmtem Modus erstellen

```
/bwadmin setup <arena-name> <modus>
```

Beispiele:
```
/bwadmin setup arena1 SOLO
/bwadmin setup arena2 DOUBLES
/bwadmin setup mega_arena MEGA_FOURS
```

### Spielmodus einer existierenden Arena ändern

```
/bwadmin setmode <modus>
```

**Hinweis:** Sie müssen zuerst `/bwadmin setup <arena>` ausführen, um eine Arena zu bearbeiten.

### Modus-spezifische Konfiguration

Beim Setzen eines Spielmodus werden automatisch folgende Werte aktualisiert:
- `minPlayers` - Minimale Spieleranzahl zum Start
- `maxPlayers` - Maximale Spieleranzahl

Die Teamgröße wird durch den Modus bestimmt und beim Spielerbeitritt automatisch überprüft.

## Für Spieler

### Spiel beitreten via GUI

```
/bw play
```

Dies öffnet ein GUI, wo Spieler:
1. Einen Spielmodus auswählen können
2. Eine verfügbare Arena für diesen Modus sehen
3. Der Arena beitreten können

### Direkter Beitritt

```
/bw join <arena-name>
```

Tritt direkt einer bestimmten Arena bei (unabhängig vom Modus).

### Arenen auflisten

```
/bw list
```

Zeigt alle verfügbaren Arenen mit ihrem jeweiligen Spielmodus an.

## Team-Zuordnung

Spieler werden automatisch dem Team mit den wenigsten Spielern zugeordnet, solange dieses Team noch nicht die maximale Teamgröße erreicht hat.

Beispiel für DOUBLES (Teamgröße 2):
- Team Rot: 0 Spieler → Spieler 1 wird zugeordnet
- Team Rot: 1 Spieler → Spieler 2 wird zugeordnet
- Team Rot: 2 Spieler (voll) → Spieler 3 geht zu Team Blau

## GUI Features

Das Spielmodus-Auswahlmenü zeigt:
- Den Namen des Modus
- Teamgröße, maximale Teams und maximale Spieler
- Anzahl verfügbarer Arenen für diesen Modus
- Visuelles Icon für jeden Modus

Das Arena-Auswahlmenü zeigt:
- Arena-Name
- Aktueller Spielmodus
- Anzahl der Teams
- Aktuelle Spieleranzahl / Maximale Spieleranzahl
- Status (Warten, Startend, Läuft)

## Konfigurationsdatei

Arenen werden mit ihrem Spielmodus gespeichert:

```yaml
arenas:
  arena1:
    name: "arena1"
    displayName: "Arena 1"
    gameMode: "SOLO"
    minPlayers: 4
    maxPlayers: 8
    # ... weitere Einstellungen
```

## Best Practices

1. **Verschiedene Modi pro Arena:** Erstellen Sie für jeden Modus separate Arenas, z.B.:
   - `solo_map1`, `solo_map2` für Solo
   - `doubles_map1`, `doubles_map2` für Doubles
   
2. **Passende Map-Größen:** 
   - Solo/Doubles: Kleinere, kompaktere Maps
   - Mega Modi: Größere Maps mit mehr Ressourcen-Spawnern

3. **Team-Farben:** 
   - Solo: Alle 8 Farben (RED, BLUE, GREEN, YELLOW, AQUA, WHITE, PINK, GRAY)
   - Doubles: Alle 8 Farben
   - 3v3v3v3/4v4v4v4: 4 Hauptfarben (RED, BLUE, GREEN, YELLOW)

## Technische Details

### GameMode Enum

Die `GameMode` Enum enthält:
- `displayName`: Anzeigename
- `teamSize`: Spieler pro Team
- `maxTeams`: Maximale Anzahl Teams
- `maxPlayers`: Maximale Spieleranzahl

### Automatische Validierung

Das Plugin prüft automatisch:
- Ob Teams die maximale Größe erreicht haben
- Ob die Arena voll ist
- Ob genug Spieler für den Start vorhanden sind
