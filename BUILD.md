# Build Instructions

## Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Internet connection for downloading dependencies

## Building the Plugin

1. Clone the repository:
```bash
git clone https://github.com/Noctivag/Minecratf-Bedwars-Plugin.git
cd Minecratf-Bedwars-Plugin
```

2. Build with Maven:
```bash
mvn clean package
```

3. The compiled JAR will be located at:
```
target/BedwarsPlugin-1.0.0.jar
```

## Installation

1. Stop your Minecraft server
2. Copy `BedwarsPlugin-1.0.0.jar` to your server's `plugins` folder
3. Start your server
4. The plugin will generate a default `config.yml` in `plugins/BedwarsPlugin/`

## Troubleshooting

### Build Fails
- Ensure you have Java 21 installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Check your internet connection (Maven needs to download dependencies)

### Plugin Doesn't Load
- Check your server is running Spigot or Paper 1.21+
- Check the server console for error messages
- Verify the JAR is in the correct plugins folder

## Development

To work on the plugin in an IDE:

### IntelliJ IDEA
1. Open the project folder
2. Maven dependencies will be imported automatically
3. Set Project SDK to Java 21

### Eclipse
1. Import as Maven project
2. Right-click project > Maven > Update Project
3. Set Java compiler to 21

## Testing

Currently, there are no automated tests. Testing should be done on a test server:

1. Build the plugin
2. Install on a test server running Minecraft 1.21
3. Create a test arena following the README instructions
4. Test with multiple players to verify game mechanics
