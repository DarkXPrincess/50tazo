# Cincuentazo - Card Game in Java/JavaFX

A multiplayer card game where players (human + 1-3 AI opponents) compete to avoid busting a sum of 50. Built with **Java 17**, **JavaFX**, **Maven**, and **JUnit 5**.

## Game Rules

- **Objective**: Be the last player standing without exceeding a sum of 50.
- **Card Values**:
  - Cards 2-8, 10: Add their numeric value
  - Card 9: Neutral (adds 0)
  - Cards J, Q, K: Subtract 10
  - Card A: Adds 1 or 10 (smart choice to avoid busting)
- **Gameplay**: Each turn, a player plays a card and draws a replacement. If no card can be played without exceeding 50, the player is eliminated.
- **Deck Replenishment**: When the deck runs out, all cards from the table (except the last played) are shuffled back into the deck.

## Project Structure

```
50TAZO/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/example/eiscuno/
│   │           ├── Main.java                 # JavaFX launcher
│   │           ├── model/
│   │           │   ├── Card.java
│   │           │   ├── Rank.java
│   │           │   ├── Suit.java
│   │           │   ├── Deck.java
│   │           │   ├── Player.java
│   │           │   └── GameEngine.java       # Core game logic
│   │           ├── exception/
│   │           │   ├── NoPlayableCardException.java
│   │           │   └── EmptyDeckException.java
│   │           ├── controller/
│   │           │   ├── WelcomeStageController.java
│   │           │   └── GameUnoController.java
│   │           ├── view/
│   │           │   └── GameUnoView.java
│   │           └── ai/
│   │               ├── AIPlayer.java         # Threaded AI logic
│   │               └── GameLoopManager.java  # Turn management
│   ├── resources/
│   │   ├── welcome-stage.fxml
│   │   └── game-uno-view.fxml
│   └── test/
│       └── java/org/example/eiscuno/
│           ├── CardValueTest.java
│           ├── DeckTest.java
│           └── PlayerEliminationTest.java
├── pom.xml
└── README.md
```

## Technologies

- **Java**: 17+
- **JavaFX**: 21.0.2 (for GUI)
- **Maven**: 3.9.11 (build tool)
- **JUnit 5**: 5.10.0 (unit tests)

## Build & Run

### Prerequisites
- Java 17+ installed
- Maven 3.9+ installed

### Compile
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

All tests should pass:
- **CardValueTest**: 3 tests (card value logic)
- **DeckTest**: 1 test (deck operations)
- **PlayerEliminationTest**: 1 test (elimination detection)

### Run Application
```bash
mvn clean javafx:run
```

Or package and run:
```bash
mvn clean package
java -jar target/50TAZO-1.0-SNAPSHOT.jar
```

## Architecture

### Model (MVC)
- **GameEngine**: Core game state and rules validation
- **Player**: Manages hand, elimination status, playable card checks
- **Deck**: Shuffles, draws, and replenishes cards
- **Card/Rank/Suit**: Card representation

### Controller
- **WelcomeStageController**: Welcome screen (player name, AI count selection)
- **GameUnoController**: Main game view (card display, turn info)

### View
- **GameUnoView**: FXML loader for game UI
- FXML layouts: `welcome-stage.fxml`, `game-uno-view.fxml`

### AI & Concurrency
- **AIPlayer**: Runs in separate thread with realistic decision delays (2-4s think, 1-2s draw)
- **GameLoopManager**: Coordinates turn execution, handles AI and human players
- Synchronized access to GameEngine for thread-safe state updates

## Exception Handling

- **NoPlayableCardException** (checked): Thrown when a card cannot be played (would exceed 50)
- **EmptyDeckException** (unchecked): Thrown when attempting to draw from empty deck before replenish

## Testing

Unit tests validate:
1. Card value logic (Ace flexibility, face cards, neutral 9)
2. Deck operations (draw, shuffle, size tracking)
3. Player elimination detection (no playable cards)

Run with:
```bash
mvn test -q
```

## Future Enhancements

- Image assets for card display
- Sound effects
- Difficulty levels for AI
- Multiplayer networking
- Game statistics/leaderboard
- Save/load game state

## License

Proyecto Mini #3 - Cincuentazo (Educational)

## Authors

Grupo de 3 estudiantes - Programa de Java/JavaFX

---

**Last Updated**: November 10, 2025
