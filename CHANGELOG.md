# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-11-06

### Added
- Complete Bedwars plugin implementation for Minecraft 1.21
- Team-based gameplay with support for up to 8 teams
- Bed destruction and respawn mechanics
- Final kill system
- Resource spawner system (Iron, Gold, Diamond, Emerald)
- Comprehensive shop system with 40+ items
  - 7 categories: Blocks, Weapons, Armor, Tools, Food, Potions, Utility
  - Permanent armor upgrades
  - Enchanted items
- Team upgrades system (Sharpness, Protection, Haste)
- Live scoreboard showing team status and player stats
- Player statistics tracking
  - Kills, Deaths, K/D Ratio
  - Final Kills, Final Deaths
  - Beds Destroyed
  - Wins, Losses, Games Played
  - Win Rate
- Multi-arena support with persistent storage
- Complete admin tool suite for arena setup
  - Easy map import system
  - Configurable spawn points
  - Configurable bed locations
  - Configurable resource spawners (team-specific or shared)
  - Arena enable/disable functionality
- Player commands (/bw join, leave, stats, list)
- Admin commands (/bwa setup, setbed, setspawn, setresource, etc.)
- Full configuration system
- Automatic team balancing
- Team damage prevention
- Comprehensive documentation
  - README with installation and usage guide
  - BUILD guide for developers
  - ARENA_EXAMPLE with setup walkthrough
  - FEATURES list
  - CONTRIBUTING guide

### Technical Details
- Built with Maven
- Spigot/Paper API 1.21+
- Java 21 support
- YAML-based arena storage
- Event-driven architecture
- Manager pattern for clean code organization
- Efficient resource management

## [Unreleased]

### Planned Features
- Team upgrades GUI
- Database storage option for statistics
- Leaderboards
- Party system
- Achievements
- Cosmetics system
- More trap types
- Generator upgrades
- Compass tracker
- Enhanced spectator mode
- Multi-language support

### Known Issues
- None reported yet

---

## Version History

- **1.0.0** - Initial release with full Bedwars functionality
