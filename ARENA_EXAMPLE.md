# Example Arena Configuration

This is an example of a complete arena setup for a 4-team Bedwars map.

## Map Layout

```
         [EMERALD]
            |
    [DIAMOND]   [DIAMOND]
        \         /
         \       /
    RED --- + --- BLUE
         /       \
        /         \
   GREEN          YELLOW
```

## Setup Commands

### 1. Start Setup
```
/bwa setup ExampleArena
```

### 2. Set Lobby Spawn
Stand in the center lobby area and run:
```
/bwa setlobby
```

### 3. Configure Red Team
Navigate to the red base and set up:
```
/bwa setbed RED
/bwa setspawn RED
/bwa setresource IRON RED
/bwa setresource GOLD RED
```

### 4. Configure Blue Team
Navigate to the blue base:
```
/bwa setbed BLUE
/bwa setspawn BLUE
/bwa setresource IRON BLUE
/bwa setresource GOLD BLUE
```

### 5. Configure Green Team
Navigate to the green base:
```
/bwa setbed GREEN
/bwa setspawn GREEN
/bwa setresource IRON GREEN
/bwa setresource GOLD GREEN
```

### 6. Configure Yellow Team
Navigate to the yellow base:
```
/bwa setbed YELLOW
/bwa setspawn YELLOW
/bwa setresource IRON YELLOW
/bwa setresource GOLD YELLOW
```

### 7. Add Diamond Spawners
Navigate to diamond locations (usually between bases):
```
/bwa setresource DIAMOND
/bwa setresource DIAMOND
```

### 8. Add Emerald Spawners
Navigate to emerald location (usually center):
```
/bwa setresource EMERALD
```

### 9. Save Arena
```
/bwa savearena
```

## Tips for Map Design

### Base Layout
- Each team base should have:
  - A bed (any bed block)
  - A spawn point (safe from enemy fire)
  - Iron generator (1 second spawn rate)
  - Gold generator (8 second spawn rate)
  - Space for building defenses

### Distances
- Base to base: 40-60 blocks
- Base to diamonds: 20-30 blocks
- Diamonds to center: 15-25 blocks

### Resources
- **Iron & Gold**: At each team base (team-specific)
- **Diamonds**: 2-4 spawners between bases (shared)
- **Emeralds**: 1-2 spawners at center (shared)

### Build Limits
- Consider using WorldGuard or similar to:
  - Prevent building too high
  - Protect spawn areas
  - Define arena boundaries

### Testing
1. Test with 2 players minimum
2. Verify all beds are breakable
3. Check resource spawners are working
4. Ensure spawn points are safe
5. Test with full teams (16 players max)
