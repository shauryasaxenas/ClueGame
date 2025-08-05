/**
 * Class: GameSetupTests
 * Unit tests for verifying Clue game setup, including player loading, deck creation,
 * solution selection, and card dealing to players.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 1, 2025
 */


package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.*;
import java.awt.Color;
import java.util.*;

public class GameSetupTests {
    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
        board.initialize();
    }

    // === PLAYER TESTS ===

    @Test
    public void testPlayersLoaded() {
        List<Player> players = board.getPlayers();
        assertEquals(6, players.size(), "There should be 6 players loaded.");

        long humanCount = players.stream().filter(p -> p instanceof HumanPlayer).count();
        long computerCount = players.stream().filter(p -> p instanceof ComputerPlayer).count();

        assertEquals(1, humanCount, "There should be 1 human player.");
        assertEquals(5, computerCount, "There should be 5 computer players.");

        Player first = players.get(0);
        assertEquals("Luke Skywalker", first.getName());
//        System.out.println(first.getColor());
        assertEquals("blue", first.getColor());
        assertEquals(0, first.getRow());
        assertEquals(8, first.getColumn());
    }

    @Test
    public void testPlayerHandsDealt() {
        List<Player> players = board.getPlayers();
        List<Card> allDealtCards = new ArrayList<>();

        for (Player player : players) {
            allDealtCards.addAll(player.getHand());
        }

        // Total cards in play = 21 - 3 (solution) = 18
        assertEquals(18, allDealtCards.size(), "Total dealt cards should be 18");

        Set<Card> uniqueCards = new HashSet<>(allDealtCards);
        assertEquals(18, uniqueCards.size(), "No duplicate cards should be dealt");

        // Check card distribution balance
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Player player : players) {
            int count = player.getHand().size();
            min = Math.min(min, count);
            max = Math.max(max, count);
        }
        assertTrue(max - min <= 1, "Players should have nearly equal number of cards");
    }

    // === CARD TESTS ===

    @Test
    public void testDeckCreation() {
        List<Card> deck = board.getDeck();
        int expected = 21 - 3; // Total cards minus 3 solution cards
        assertEquals(expected, deck.size(), "Deck should contain 18 cards after solution dealt");
    }

    @Test
    public void testCardType() {
        List<Card> cards = new ArrayList<>(board.getDeck());
        Solution solution = board.getSolution();
        cards.add(solution.person);
        cards.add(solution.weapon);
        cards.add(solution.room);
        
//        for (Card card : board.getDeck()) {
//        	System.out.println(card);
//        }
//        
//        for (Player p : board.getPlayers()) {
//        	System.out.println(p.getName() + " (" + p.getColor() + ")");
//        }

        assertTrue(cards.contains(new Card("Lightsaber", CardType.WEAPON)));
        assertTrue(cards.contains(new Card("Princess Leia", CardType.PERSON)));
        assertTrue(cards.contains(new Card("Mos Eisley Cantina", CardType.ROOM)));
    }

    // === SOLUTION TEST ===

    @Test
    public void testSolutionDealt() {
        Solution solution = board.getSolution();
        assertNotNull(solution);

        assertEquals(CardType.PERSON, solution.person.getType());
        assertEquals(CardType.WEAPON, solution.weapon.getType());
        assertEquals(CardType.ROOM, solution.room.getType());
    }
}
