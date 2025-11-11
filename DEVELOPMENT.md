# Cincuentazo - Development Guide

## Project Setup for Developers

### Prerequisites
- **Java 17+**: Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or use Eclipse Adoptium
- **Maven 3.9+**: Download from [Apache Maven](https://maven.apache.org/)
- **IntelliJ IDEA** (recommended): Community or Ultimate edition

### First-Time Setup

1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd 50TAZO
   ```

2. Install dependencies and compile:
   ```bash
   mvn clean install
   ```

3. Verify tests pass:
   ```bash
   mvn test
   ```

## IDE Configuration (IntelliJ IDEA)

1. **Open Project**: File → Open → Select `50TAZO` folder
2. **Configure Java**: File → Project Structure → Project
   - Set Project SDK to Java 17+
   - Set Project Language Level to 17
3. **Configure Maven**: File → Settings → Build, Execution, Deployment → Build Tools → Maven
   - Set Maven home to your Maven installation
4. **Run Tests**: Right-click on `src/test/java` → Run Tests
5. **Run Application**: Run → Edit Configurations → Add New Configuration
   - Type: Application
   - Main class: `org.example.eiscuno.Main`
   - VM options: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`

## Code Structure

### Model Layer (`org.example.eiscuno.model`)
Handles all game logic and state:
- **GameEngine**: Central controller (synchronized for thread safety)
  - `dealInitial()`: Deal 4 cards to each player, place starter card
  - `playCard()`: Validate and execute card play
  - `checkEliminateIfNoPlay()`: Test for elimination
  - `replenishDeckFromTable()`: Handle deck reset from table pile
- **Player**: Represents a player (human or AI)
  - `getPlayableCard()`: Return first card that doesn't bust
  - `removeAllCards()`: For elimination
- **Deck**: Standard 52-card deck with shuffle/replenish
- **Card/Rank/Suit**: Immutable card representation

### Exception Layer (`org.example.eiscuno.exception`)
- **NoPlayableCardException** (checked): Invalid play attempt
- **EmptyDeckException** (unchecked): Deck exhausted

### AI Layer (`org.example.eiscuno.ai`)
Implements concurrency with realistic delays:
- **AIPlayer** (Thread): Single AI turn execution
  - 2-4s thinking time before card selection
  - 1-2s draw time after play
  - Synchronized calls to GameEngine
- **GameLoopManager**: Orchestrates all turns
  - Determines current player
  - Spawns AIPlayer threads for non-human players
  - Detects game end condition

### Controller Layer (`org.example.eiscuno.controller`)
Bridges UI and model:
- **WelcomeStageController**: Welcome screen
  - Collects player name and AI count (1-3)
  - Validates input
  - Creates GameEngine and launches game view
- **GameUnoController**: Game screen
  - Displays current player hand
  - Shows table card and current sum
  - Handles UI interactions (play card, draw, advance turn)

### View Layer (`org.example.eiscuno.view`)
- **GameUnoView**: Loads FXML and wires controller
- FXML files in `src/main/resources/`:
  - `welcome-stage.fxml`: Welcome layout (username, player count)
  - `game-uno-view.fxml`: Game layout (hand, table, deck, UI controls)

## Threading Model

### Game Loop Thread (GameLoopManager)
- Runs continuously, advancing turns
- Waits for AI threads to complete
- Checks elimination conditions
- Detects game end

### AI Player Thread (AIPlayer)
- Each AI player turn runs in its own thread
- Simulates thinking with `Thread.sleep()`
- Calls `engine.playCard()` inside `synchronized` block
- Ensures thread-safe access to GameEngine

### Synchronization
- **GameEngine methods** are `synchronized`:
  - `playCard()`
  - `checkEliminateIfNoPlay()`
  - `replenishDeckFromTable()`
- **UI updates** (from controller) must marshal to JavaFX Application Thread
  - Use `Platform.runLater()` for thread-safe UI updates

## Testing

### Test Coverage
3 JUnit 5 test classes, 5 total tests:

1. **CardValueTest** (3 tests):
   - `faceCardsAreNegativeTen()`: J/Q/K = -10
   - `nineIsNeutral()`: 9 = 0
   - `aceChooses10WhenNotBustOtherwise1()`: A smart logic

2. **DeckTest** (1 test):
   - `drawReducesSize()`: Draw operations

3. **PlayerEliminationTest** (1 test):
   - `playerWithNoPlayableCardsIsDetected()`: Elimination logic

Run all tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=CardValueTest
```

## Building & Packaging

### Generate Javadoc
```bash
mvn javadoc:javadoc
```
Output: `target/site/apidocs/index.html`

### Package JAR
```bash
mvn clean package
```
Output: `target/50TAZO-1.0-SNAPSHOT.jar`

### Full Build
```bash
mvn clean install
```

## Debugging Tips

1. **Enable Debug Mode in Maven**:
   ```bash
   mvn -X compile
   ```

2. **Print GameEngine State**:
   ```java
   System.out.println("Current Sum: " + engine.getCurrentSum());
   System.out.println("Active Players: " + engine.activePlayersCount());
   ```

3. **Test Card Values**:
   ```bash
   mvn test -Dtest=CardValueTest#aceChooses10WhenNotBustOtherwise1
   ```

4. **IntelliJ Debugger**:
   - Set breakpoints (left margin of code)
   - Right-click test → Debug
   - Step through with F8 (step over), F7 (step into)

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| JavaFX import errors | Ensure JavaFX is in pom.xml dependencies; Maven downloads needed JARs |
| Tests fail to compile | Run `mvn clean compile` first to resolve dependencies |
| Game doesn't start | Check that `welcome-stage.fxml` path is correct; verify FXML is in `src/main/resources/` |
| AI seems stuck | Check GameLoopManager thread join timeout; increase if needed |
| Thread safety errors | Ensure all GameEngine method calls are synchronized |

## Git Workflow

```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes and test
mvn clean test

# Commit with clear message
git commit -m "feat: Add AI threading for turn management"

# Push to remote
git push origin feature/my-feature

# Create Pull Request on GitHub
```

## Code Style Guidelines

1. **Naming**:
   - Classes: PascalCase (e.g., `GameEngine`)
   - Methods: camelCase (e.g., `playCard`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_PLAYERS = 3`)

2. **Documentation**:
   - All public classes and methods must have Javadoc
   - Use English comments
   - Explain "why", not just "what"

3. **Formatting**:
   - Max line length: 120 characters
   - Indentation: 4 spaces
   - No trailing whitespace

## Performance Considerations

- **Deck Shuffling**: O(n) randomized, acceptable for 52 cards
- **Hand Lookup**: O(n) for ~4 cards per player, negligible
- **Thread Creation**: New AIPlayer thread per AI turn; acceptable for 1-3 players
- **Memory**: All game state in memory; no database needed

## Future Enhancements

- [ ] Graphical card assets (images)
- [ ] Sound effects (background music, card play)
- [ ] Difficulty levels for AI (random vs. greedy vs. optimal)
- [ ] Multiplayer networking (TCP/WebSocket)
- [ ] Game statistics (wins, losses, streak)
- [ ] Save/load game state to file
- [ ] Animation for card transitions
- [ ] Undo/redo moves

---

For questions, refer to the main `README.md` or check inline code comments.
