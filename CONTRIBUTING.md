# Contributing to Bedwars Plugin

Thank you for your interest in contributing to this Minecraft Bedwars plugin! We welcome contributions from the community.

## How to Contribute

### Reporting Bugs

If you find a bug, please create an issue with:
- A clear, descriptive title
- Steps to reproduce the bug
- Expected behavior
- Actual behavior
- Server version and plugin version
- Any error messages from console

### Suggesting Features

Feature suggestions are welcome! Please:
- Check if the feature has already been requested
- Provide a clear description of the feature
- Explain why this feature would be useful
- Include examples if applicable

### Code Contributions

1. **Fork the Repository**
   - Fork this repository to your GitHub account
   - Clone your fork locally

2. **Create a Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**
   - Follow the existing code style
   - Write clear, commented code
   - Test your changes thoroughly

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "Add feature: description"
   ```

5. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Select your branch
   - Describe your changes clearly

## Code Style Guidelines

### Java Code
- Use 4 spaces for indentation
- Follow standard Java naming conventions
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
- Add JavaDoc comments for public methods
- Keep methods focused and small
- Use meaningful variable names

### Example
```java
/**
 * Handles player joining a game
 * @param player The player joining
 * @param arenaName The arena to join
 * @return true if successful, false otherwise
 */
public boolean joinGame(Player player, String arenaName) {
    // Implementation
}
```

### Configuration Files
- Use 2 spaces for YAML indentation
- Add comments explaining configuration options
- Provide sensible defaults

## Testing

Before submitting:
1. Build the plugin: `mvn clean package`
2. Test on a Minecraft 1.21 server
3. Verify your changes work as expected
4. Check for any console errors
5. Test edge cases

## Areas We Need Help With

- **Documentation**: Improving guides and examples
- **Testing**: Testing on different server versions
- **Features**: Implementing items from the "Future Enhancements" list
- **Bug Fixes**: Resolving open issues
- **Performance**: Optimizing resource usage
- **Translations**: Multi-language support

## Pull Request Checklist

- [ ] Code follows the style guidelines
- [ ] Changes have been tested
- [ ] No new warnings or errors
- [ ] Documentation updated (if applicable)
- [ ] Commit messages are clear
- [ ] PR description explains changes

## Questions?

If you have questions about contributing:
- Check existing issues and PRs
- Create a new issue with the "question" label
- Be patient - maintainers are volunteers

## Code of Conduct

- Be respectful and inclusive
- Welcome newcomers
- Focus on constructive feedback
- Help others learn

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

Thank you for helping make this plugin better!
