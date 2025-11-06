# Quick Start Guide

Get your Bedwars server running in 5 minutes!

## Step 1: Installation (1 minute)

1. Download the latest release from GitHub
2. Stop your server
3. Copy `BedwarsPlugin-1.0.0.jar` to your `plugins` folder
4. Start your server

## Step 2: Basic Configuration (1 minute)

1. Stop the server
2. Edit `plugins/BedwarsPlugin/config.yml`
3. Set your lobby location (optional - you can use /bwa setlobby later)
4. Adjust settings if desired
5. Start the server

## Step 3: Create Your First Arena (3 minutes)

### Quick 2-Team Setup

1. **Start setup**
   ```
   /bwa setup MyFirstArena
   ```

2. **Set lobby spawn** (where players wait)
   - Stand in your arena lobby
   ```
   /bwa setlobby
   ```

3. **Set up Red Team**
   - Stand at red team's bed
   ```
   /bwa setbed RED
   ```
   - Stand where red team should spawn
   ```
   /bwa setspawn RED
   ```
   - Add their generators
   ```
   /bwa setresource IRON RED
   /bwa setresource GOLD RED
   ```

4. **Set up Blue Team**
   ```
   /bwa setbed BLUE
   /bwa setspawn BLUE
   /bwa setresource IRON BLUE
   /bwa setresource GOLD BLUE
   ```

5. **Add shared resources** (optional but recommended)
   - Stand at diamond locations
   ```
   /bwa setresource DIAMOND
   /bwa setresource DIAMOND
   ```
   - Stand at emerald location
   ```
   /bwa setresource EMERALD
   ```

6. **Save the arena**
   ```
   /bwa savearena
   ```

## Step 4: Play!

Players can now join:
```
/bw join MyFirstArena
```

## Tips for First Arena

### Recommended Distances
- Base to base: 40-60 blocks
- Base to diamonds: 20-30 blocks
- Diamonds to center: 15-25 blocks

### Essential Items
- Each base needs:
  - A bed block
  - Safe spawn point
  - Iron generator
  - Gold generator

### Quick Testing
1. Minimum 2 players needed to start
2. Game starts automatically when min players is reached
3. Test bed breaking and respawning
4. Test resource spawners
5. Test shop (right-click villagers)

## Common Issues

**"Arena is not complete!"**
- Make sure you set:
  - Lobby spawn (`/bwa setlobby`)
  - At least 2 teams with beds and spawns

**"Can't join game"**
- Check arena is enabled: `/bwa list`
- Enable if needed: `/bwa enable MyFirstArena`

**"Resources not spawning"**
- Resource spawners were added during setup
- They spawn automatically during active games

**"No shop"**
- Shop opens by right-clicking villagers
- You can spawn villagers at team bases for now
- Or add shop locations in future updates

## Next Steps

1. **Add more teams**: Repeat bed/spawn/resource setup for GREEN, YELLOW, etc.
2. **Build the map**: Make it look nice with themes
3. **Balance resources**: Adjust spawn rates in config.yml
4. **Create more arenas**: Use `/bwa setup AnotherArena`
5. **Customize**: Edit config.yml for custom messages, timings, etc.

## Full Documentation

For detailed information, see:
- **README.md** - Complete feature list and commands
- **ARENA_EXAMPLE.md** - Detailed 4-team arena setup
- **BUILD.md** - Build from source
- **FEATURES.md** - Complete feature documentation

## Need Help?

- Check the documentation files
- Open an issue on GitHub
- Include server version and error messages

Enjoy your Bedwars server! 🎮
