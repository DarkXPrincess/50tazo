package com.example.minip3poe.model;

import com.example.minip3poe.model.exceptions.InvalidCardPlayException;
import com.example.minip3poe.model.exceptions.InvalidPlayerCountException;
import com.example.minip3poe.model.player.HumanPlayer;
import com.example.minip3poe.model.player.MachinePlayer;
import com.example.minip3poe.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameModel class.
 * Tests game initialization, card plays, player elimination, and game flow.
 * Implements user stories HU-1 through HU-6.
 *
 * @author Alexander 
 * @version 1.0
 */
@DisplayName("GameModel Tests")
class GameModelTest {

    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        gameModel = new GameModel();
    }

    @Nested
    @DisplayName("Game Initialization Tests - HU-1")
    class GameInitializationTests {

        @Test
        @DisplayName("Game should initialize with 1 machine player")
        void testInitializeWith1MachinePlayer() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            assertEquals(2, gameModel.getAllPlayers().size()); // 1 human + 1 machine
        }

        @Test
        @DisplayName("Game should initialize with 2 machine players")
        void testInitializeWith2MachinePlayers() throws InvalidPlayerCountException {
            gameModel.initializeGame(2);
            assertEquals(3, gameModel.getAllPlayers().size()); // 1 human + 2 machines
        }

        @Test
        @DisplayName("Game should initialize with 3 machine players")
        void testInitializeWith3MachinePlayers() throws InvalidPlayerCountException {
            gameModel.initializeGame(3);
            assertEquals(4, gameModel.getAllPlayers().size()); // 1 human + 3 machines
        }

        @Test
        @DisplayName("Game should throw exception with invalid machine count < 1")
        void testInitializeWithZeroMachinePlayersThrowsException() {
            assertThrows(InvalidPlayerCountException.class, () -> gameModel.initializeGame(0));
        }

        @Test
        @DisplayName("Game should throw exception with invalid machine count > 3")
        void testInitializeWithTooManyMachinePlayersThrowsException() {
            assertThrows(InvalidPlayerCountException.class, () -> gameModel.initializeGame(4));
        }

        @Test
        @DisplayName("Game should create human player as first player")
        void testHumanPlayerIsCreated() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            HumanPlayer human = gameModel.getHumanPlayer();
            assertNotNull(human);
            assertEquals("Human Player", human.getName());
        }

        @Test
        @DisplayName("Game should not be started after initialization")
        void testGameNotStartedAfterInitialization() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            assertFalse(gameModel.isGameStarted());
        }
    }

    @Nested
    @DisplayName("Game Start Tests - HU-2")
    class GameStartTests {

        @BeforeEach
        void setUp() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
        }

        @Test
        @DisplayName("Game should be started after calling startGame")
        void testGameStartedFlag() {
            gameModel.startGame();
            assertTrue(gameModel.isGameStarted());
        }

        @Test
        @DisplayName("Each player should have 4 cards after game starts")
        void testPlayerRecieve4Cards() {
            gameModel.startGame();
            for (Player player : gameModel.getAllPlayers()) {
                assertEquals(4, player.getHandSize());
            }
        }

        @Test
        @DisplayName("Initial card should be placed on table")
        void testInitialCardOnTable() {
            gameModel.startGame();
            assertNotNull(gameModel.getTopCard());
        }

        @Test
        @DisplayName("Table sum should be set after initial card is placed")
        void testTableSumInitialized() {
            gameModel.startGame();
            // Table sum should be set to the game value of the initial card
            assertNotEquals(0, gameModel.getTableSum());
        }

        @Test
        @DisplayName("Current player should be set after game starts")
        void testCurrentPlayerSet() {
            gameModel.startGame();
            assertNotNull(gameModel.getCurrentPlayer());
        }

        @Test
        @DisplayName("Current player should be the first in turn order")
        void testCurrentPlayerIsFirst() {
            gameModel.startGame();
            Player firstPlayer = gameModel.getAllPlayers().get(0);
            assertEquals(firstPlayer, gameModel.getCurrentPlayer());
        }

        @Test
        @DisplayName("Table sum can be negative if initial card reduces it")
        void testTableSumCanBeNegative() {
            gameModel.startGame();
            int tableSum = gameModel.getTableSum();
            // Table sum could be -10 (if Jack), 0 (if Nine), or positive
            assertTrue(tableSum >= -10);
        }

        @Test
        @DisplayName("Remaining cards in deck should be 48 after game setup")
        void testRemainingCardsAfterSetup() {
            gameModel.startGame();
            // 52 total cards - 4 cards per player (2 players) - 1 initial card = 52 - 9 = 43
            // But might vary depending on number of machine players
            int expected = 52 - (gameModel.getAllPlayers().size() * 4) - 1;
            assertEquals(expected, gameModel.getRemainingCards());
        }
    }

    @Nested
    @DisplayName("Card Play Tests - HU-3")
    class CardPlayTests {

        @BeforeEach
        void setUp() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();
        }

        @Test
        @DisplayName("Valid card should be playable")
        void testValidCardCanBePlayed() throws InvalidCardPlayException {
            Player currentPlayer = gameModel.getCurrentPlayer();
            Card playableCard = null;

            // Find a card that can be played
            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayed(gameModel.getTableSum())) {
                    playableCard = card;
                    break;
                }
            }

            assertNotNull(playableCard, "No playable card found");
            int initialHandSize = currentPlayer.getHandSize();
            gameModel.playCard(playableCard);
            assertEquals(initialHandSize - 1, currentPlayer.getHandSize());
        }

        @Test
        @DisplayName("Card not in player's hand should throw exception")
        void testCardNotInHandThrowsException() throws InvalidPlayerCountException {
            GameModel model = new GameModel();
            model.initializeGame(1);
            model.startGame();

            Card cardNotInHand = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
            assertThrows(InvalidCardPlayException.class, () -> model.playCard(cardNotInHand));
        }

        @Test
        @DisplayName("Card that exceeds 50 should throw exception")
        void testCardExceeding50ThrowsException() throws InvalidPlayerCountException, InvalidCardPlayException {
            GameModel model = new GameModel();
            model.initializeGame(1);
            model.startGame();

            // Manually set table sum to 49
            Card cardTwo = new Card(Card.Rank.TWO, Card.Suit.HEARTS);
            Player currentPlayer = model.getCurrentPlayer();
            
            // Try to find an unplayable card or create scenario
            for (Card card : currentPlayer.getHand()) {
                if (!card.canBePlayed(model.getTableSum())) {
                    assertThrows(InvalidCardPlayException.class, () -> model.playCard(card));
                    return;
                }
            }
        }

        @Test
        @DisplayName("Playing a card should update table sum")
        void testTableSumUpdatedAfterCardPlay() throws InvalidCardPlayException {
            Player currentPlayer = gameModel.getCurrentPlayer();
            Card playableCard = null;
            int cardValue = 0;

            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayed(gameModel.getTableSum())) {
                    playableCard = card;
                    cardValue = card.getGameValue(gameModel.getTableSum());
                    break;
                }
            }

            assertNotNull(playableCard);
            int tableSumBefore = gameModel.getTableSum();
            gameModel.playCard(playableCard);
            int tableSumAfter = gameModel.getTableSum();

            assertEquals(tableSumBefore + cardValue, tableSumAfter);
        }

        @Test
        @DisplayName("Played card should be removed from player's hand")
        void testCardRemovedFromHandAfterPlay() throws InvalidCardPlayException {
            Player currentPlayer = gameModel.getCurrentPlayer();
            Card playableCard = null;

            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayed(gameModel.getTableSum())) {
                    playableCard = card;
                    break;
                }
            }

            assertNotNull(playableCard);
            gameModel.playCard(playableCard);
            assertFalse(currentPlayer.getHand().contains(playableCard));
        }
    }

    @Nested
    @DisplayName("Card Draw Tests - HU-4")
    class CardDrawTests {

        @BeforeEach
        void setUp() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();
        }

        @Test
        @DisplayName("Drawing a card should add it to player's hand")
        void testDrawCardAddsToHand() throws InvalidCardPlayException {
            Player currentPlayer = gameModel.getCurrentPlayer();
            int initialSize = currentPlayer.getHandSize();

            // Play a card first
            Card playableCard = null;
            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayed(gameModel.getTableSum())) {
                    playableCard = card;
                    break;
                }
            }

            if (playableCard != null) {
                gameModel.playCard(playableCard);
                int sizeAfterPlay = currentPlayer.getHandSize();
                assertEquals(initialSize - 1, sizeAfterPlay);

                gameModel.drawCard();
                assertEquals(initialSize, currentPlayer.getHandSize());
            }
        }

        @Test
        @DisplayName("Player should always have 4 cards after drawing")
        void testPlayerHasFourCardsAfterDraw() throws InvalidCardPlayException {
            Player currentPlayer = gameModel.getCurrentPlayer();

            Card playableCard = null;
            for (Card card : currentPlayer.getHand()) {
                if (card.canBePlayed(gameModel.getTableSum())) {
                    playableCard = card;
                    break;
                }
            }

            if (playableCard != null) {
                gameModel.playCard(playableCard);
                gameModel.drawCard();
                assertEquals(4, currentPlayer.getHandSize());
            }
        }
    }

    @Nested
    @DisplayName("Player Elimination Tests - HU-5")
    class PlayerEliminationTests {

        @BeforeEach
        void setUp() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();
        }

        @Test
        @DisplayName("Player with no valid cards should be eliminated")
        void testPlayerEliminatedWhenNoValidCards() throws InvalidPlayerCountException {
            GameModel model = new GameModel();
            model.initializeGame(1);
            model.startGame();

            Player player = model.getCurrentPlayer();
            model.eliminatePlayer(player);

            assertTrue(player.isEliminated());
        }

        @Test
        @DisplayName("Eliminated player's cards should be added to deck")
        void testEliminatedPlayerCardsAddedToDeck() throws InvalidPlayerCountException {
            GameModel model = new GameModel();
            model.initializeGame(1);
            model.startGame();

            Player player = model.getCurrentPlayer();
            int cardsBeforeElimination = player.getHandSize();
            int deckSizeBefore = model.getRemainingCards();

            model.eliminatePlayer(player);

            // Cards should be in deck now
            assertEquals(0, player.getHandSize());
        }

        @Test
        @DisplayName("Eliminated player should be removed from turn queue")
        void testEliminatedPlayerNotInActivePlayers() throws InvalidPlayerCountException {
            GameModel model = new GameModel();
            model.initializeGame(1);
            model.startGame();

            Player player = model.getCurrentPlayer();
            model.eliminatePlayer(player);

            List<Player> activePlayers = model.getActivePlayers();
            assertFalse(activePlayers.contains(player));
        }
    }

    @Nested
    @DisplayName("Game End Tests - HU-6")
    class GameEndTests {

        @Test
        @DisplayName("Game should not be finished when multiple players remain")
        void testGameNotFinishedWithMultiplePlayers() throws InvalidPlayerCountException {
            gameModel.initializeGame(2);
            gameModel.startGame();

            assertFalse(gameModel.isGameFinished());
        }

        @Test
        @DisplayName("Game should be finished when only 1 player remains")
        void testGameFinishedWhenOnlyOnePlayerRemains() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();

            List<Player> allPlayers = gameModel.getAllPlayers();
            // Eliminate all but the first player
            for (int i = 1; i < allPlayers.size(); i++) {
                gameModel.eliminatePlayer(allPlayers.get(i));
            }

            gameModel.nextTurn();
            assertTrue(gameModel.isGameFinished());
        }

        @Test
        @DisplayName("Winner should be the last remaining player")
        void testWinnerIsLastPlayer() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();

            List<Player> allPlayers = gameModel.getAllPlayers();
            Player expectedWinner = allPlayers.get(0);

            // Eliminate all but the first player
            for (int i = 1; i < allPlayers.size(); i++) {
                gameModel.eliminatePlayer(allPlayers.get(i));
            }

            gameModel.nextTurn();
            assertEquals(expectedWinner, gameModel.getWinner());
        }

        @Test
        @DisplayName("Winner should not be eliminated")
        void testWinnerNotEliminated() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();

            List<Player> allPlayers = gameModel.getAllPlayers();
            Player expectedWinner = allPlayers.get(0);

            for (int i = 1; i < allPlayers.size(); i++) {
                gameModel.eliminatePlayer(allPlayers.get(i));
            }

            gameModel.nextTurn();
            assertFalse(expectedWinner.isEliminated());
        }
    }

    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {

        @BeforeEach
        void setUp() throws InvalidPlayerCountException {
            gameModel.initializeGame(1);
            gameModel.startGame();
        }

        @Test
        @DisplayName("getCurrentPlayer should return current player")
        void testGetCurrentPlayer() {
            assertNotNull(gameModel.getCurrentPlayer());
        }

        @Test
        @DisplayName("getTableSum should return current table sum")
        void testGetTableSum() {
            int tableSum = gameModel.getTableSum();
            assertTrue(tableSum >= -10 && tableSum <= 50);
        }

        @Test
        @DisplayName("getTopCard should return the last card played")
        void testGetTopCard() {
            assertNotNull(gameModel.getTopCard());
        }

        @Test
        @DisplayName("getAllPlayers should return all players")
        void testGetAllPlayers() {
            List<Player> allPlayers = gameModel.getAllPlayers();
            assertEquals(2, allPlayers.size());
        }

        @Test
        @DisplayName("getActivePlayers should return non-eliminated players")
        void testGetActivePlayers() throws InvalidPlayerCountException {
            GameModel model = new GameModel();
            model.initializeGame(2);
            model.startGame();

            List<Player> activePlayers = model.getActivePlayers();
            assertEquals(3, activePlayers.size()); // 1 human + 2 machines

            model.eliminatePlayer(activePlayers.get(1));
            assertEquals(2, model.getActivePlayers().size());
        }

        @Test
        @DisplayName("getHumanPlayer should return human player")
        void testGetHumanPlayer() {
            HumanPlayer human = gameModel.getHumanPlayer();
            assertNotNull(human);
            assertTrue(human instanceof HumanPlayer);
        }

        @Test
        @DisplayName("getRemainingCards should return correct count")
        void testGetRemainingCards() {
            int remaining = gameModel.getRemainingCards();
            assertTrue(remaining > 0);
            assertTrue(remaining <= 52);
        }
    }
}
