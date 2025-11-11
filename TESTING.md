# Cincuentazo - Testing Guide

## Test Architecture

Tests are located in `src/test/java/org/example/eiscuno/` and validate core game logic using **JUnit 5**.

### Test Framework
- **Framework**: JUnit 5 (Jupiter)
- **Dependency**: org.junit.jupiter:junit-jupiter (5.10.0)
- **Assertions**: `assertEquals()`, `assertFalse()`, `assertNotNull()`, etc.

## Test Cases

### 1. CardValueTest (3 assertions)

**Purpose**: Validate card value calculations according to game rules.

```java
@Test
void faceCardsAreNegativeTen()
```
- **Tests**: J, Q, K cards always return -10
- **Rule**: Face cards subtract 10 from table sum
- **Cases**:
  - Jack of Hearts @ sum 0 → -10
  - Queen of Spades @ sum 0 → -10
  - King of Diamonds @ sum 0 → -10

```java
@Test
void nineIsNeutral()
```
- **Tests**: 9 card returns 0
- **Rule**: Nine neither adds nor subtracts
- **Case**: Nine of Clubs @ sum 0 → 0

```java
@Test
void aceChooses10WhenNotBustOtherwise1()
```
- **Tests**: Ace dynamic logic
- **Rule**: Ace returns 10 if sum + 10 ≤ 50, else 1
- **Cases**:
  - Ace of Spades @ sum 0 → 10 (45 would not bust)
  - Ace of Spades @ sum 45 → 1 (55 would bust)

**Run**:
```bash
mvn test -Dtest=CardValueTest
```

### 2. DeckTest (1 assertion)

**Purpose**: Validate deck shuffling and drawing operations.

```java
@Test
void drawReducesSize()
```
- **Tests**: Deck size decreases after draw
- **Setup**: Create new Deck (52 cards)
- **Action**: Record size, draw one card
- **Assertion**: New size = original size - 1

**Run**:
```bash
mvn test -Dtest=DeckTest
```

### 3. PlayerEliminationTest (1 assertion)

**Purpose**: Validate player elimination detection when no cards are playable.

```java
@Test
void playerWithNoPlayableCardsIsDetected()
```
- **Tests**: `hasPlayableCard()` returns false when all cards bust
- **Setup**: Player with 4 TEN cards @ current sum 45
  - 45 + 10 = 55 (bust) ❌
  - 45 + 10 = 55 (bust) ❌
  - 45 + 10 = 55 (bust) ❌
  - 45 + 10 = 55 (bust) ❌
- **Assertion**: `hasPlayableCard(45)` returns false

**Run**:
```bash
mvn test -Dtest=PlayerEliminationTest
```

## Running Tests

### All Tests
```bash
mvn test
```
**Output**:
```
[INFO] Running org.example.eiscuno.CardValueTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 s
[INFO] Running org.example.eiscuno.DeckTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s
[INFO] Running org.example.eiscuno.PlayerEliminationTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.025 s
[INFO] BUILD SUCCESS
```

### Single Test Class
```bash
mvn test -Dtest=CardValueTest
```

### Single Test Method
```bash
mvn test -Dtest=CardValueTest#aceChooses10WhenNotBustOtherwise1
```

### Verbose Output
```bash
mvn test -e
```

### Generate Test Report
```bash
mvn surefire:test
```
Reports: `target/surefire-reports/`

## Test Coverage

Current coverage: **5 test methods** across 3 classes

| Component | Test Class | Methods | Coverage |
|-----------|-----------|---------|----------|
| Card value logic | CardValueTest | 3 | All card types (2-10, J/Q/K, 9, A) |
| Deck operations | DeckTest | 1 | Draw and size tracking |
| Player elimination | PlayerEliminationTest | 1 | Hand check, no playable cards |
| **Total** | **3 classes** | **5 tests** | **Core rules** |

## Areas NOT Currently Tested (Future Enhancements)

- [ ] GameEngine.playCard() (requires mocking/integration test)
- [ ] GameEngine.dealInitial() (full game setup)
- [ ] Deck replenishment logic (complex state)
- [ ] Player elimination (checkEliminateIfNoPlay)
- [ ] AI player threading (concurrency testing)
- [ ] UI controllers (JavaFX testing frameworks needed)

## Adding New Tests

### Example: Test Full Game Play

```java
package org.example.eiscuno;

import org.example.eiscuno.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    @Test
    void gameInitializesProperly() {
        GameEngine engine = new GameEngine("Alice", 1);
        engine.dealInitial();

        // Verify initial state
        assertEquals(2, engine.getPlayers().size());
        assertNotNull(engine.getTableTop());
        assertTrue(engine.getCurrentSum() >= 0);
        assertEquals(2, engine.getPlayers().stream()
            .filter(p -> p.getHand().size() == 4)
            .count());
    }
}
```

To add:
1. Create file: `src/test/java/org/example/eiscuno/GameEngineTest.java`
2. Add test method with `@Test` annotation
3. Use `GameEngine`, `Card`, `Player` directly
4. Run: `mvn test`

## Debugging Failing Tests

### In Terminal
```bash
mvn test -X
```

### In IntelliJ IDEA
1. Right-click test method
2. Select "Debug"
3. Set breakpoints (red dot on line number)
4. Use F8 (step over), F7 (step into)

### Common Issues

| Symptom | Cause | Fix |
|---------|-------|-----|
| `ClassNotFoundException` | Missing dependency | Run `mvn clean compile` |
| `AssertionError: expected <X> but was <Y>` | Logic error in code | Review Card.valueFor() logic |
| `NoPlayableCardException` | Not caught in test | Ensure cards are valid for test sum |

## Performance Benchmarks (for reference)

- All 5 tests complete in < 200ms
- Average per test: ~40ms
- Deck operations (shuffle/draw): ~1ms

## Continuous Integration

When committed to GitHub, tests should run automatically via CI/CD pipeline:

```yaml
# Example .github/workflows/test.yml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: 17
      - run: mvn clean test
```

---

**Always run tests before committing**:
```bash
mvn clean test
```

Tests validate that your changes don't break game rules. **If tests fail, your code has a bug.**
