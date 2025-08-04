/**
 * Class: GameSolutionTest
 * Unit tests for verifying Clue game solution, including player accusations and suggestions
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 2, 2025
 */

package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

import java.util.*;

public class GameSolutionTest {
    private static Board board;

    private static Card vader, leia, luke;
    private static Card blaster, lightsaber, detonator;
    private static Card cantina, trashCompactor, controlRoom;

    // Setup 
    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");

        try {
            board.loadSetupConfig();
            board.loadLayoutConfig();
        } catch (BadConfigFormatException e) {
            e.printStackTrace();
            fail("Setup failed due to config error");
        }

        mapCards();
        board.setSolution(new Solution(vader, cantina, lightsaber));
    }

    private static void mapCards() {
        for (Card c : board.getDeck()) {
            switch (c.getName()) {
                case "Darth Vader" -> vader = c;
                case "Princess Leia" -> leia = c;
                case "Luke Skywalker" -> luke = c;
                case "Blaster" -> blaster = c;
                case "Lightsaber" -> lightsaber = c;
                case "Thermal Detonator" -> detonator = c;
                case "Mos Eisley Cantina" -> cantina = c;
                case "Trash Compactor" -> trashCompactor = c;
                case "Death Star Control Room" -> controlRoom = c;
            }
        }
    }

    private void clearAllHands() {
        board.getPlayers().forEach(p -> p.getHand().clear());
    }

    // Accusation Tests

    @Test
    public void testAccusationCorrect() {
        assertTrue(board.checkAccusation(new Solution(vader, cantina, lightsaber)));
    }

    @Test
    public void testAccusationWrongPerson() {
        assertFalse(board.checkAccusation(new Solution(leia, cantina, lightsaber)));
    }

    @Test
    public void testAccusationWrongWeapon() {
        assertFalse(board.checkAccusation(new Solution(vader, cantina, detonator)));
    }

    @Test
    public void testAccusationWrongRoom() {
        assertFalse(board.checkAccusation(new Solution(vader, controlRoom, lightsaber)));
    }

    // disproveSuggestion() Tests 

    // Player has one matching card — should return it
    @Test
    public void testDisproveSingleMatchingCard() {
        Player player = new ComputerPlayer("Test", "black", 0, 0);
        player.addCard(leia);
        Solution suggestion = new Solution(leia, controlRoom, detonator);
        assertEquals(leia, player.disproveSuggestion(suggestion));
    }
    
    // Player has multiple matching cards — should return one randomly
    @Test
    public void testDisproveMultipleMatchesRandom() {
        Player player = new ComputerPlayer("Test", "black", 0, 0);
        player.addCard(vader);
        player.addCard(detonator);
        Solution suggestion = new Solution(vader, cantina, detonator);

        Set<Card> results = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Card result = player.disproveSuggestion(suggestion);
            assertNotNull(result);
            assertTrue(result.equals(vader) || result.equals(detonator));
            results.add(result);
        }
        assertTrue(results.contains(vader));
        assertTrue(results.contains(detonator));
    }
    
    // Player has no matching cards — should return null
    @Test
    public void testDisproveNoMatchingCard() {
        Player player = new ComputerPlayer("Test", "black", 0, 0);
        player.addCard(luke);
        player.addCard(trashCompactor);
        Solution suggestion = new Solution(vader, controlRoom, detonator);
        for (int i = 0; i < 100; i++) {
            assertNull(player.disproveSuggestion(suggestion));
        }
    }

    // handleSuggestion() Tests

    // No one can disprove the suggestion — should return null
    @Test
    public void testHandleSuggestionNoOneCanDisprove() {
        clearAllHands();
        Player suggester = board.getPlayers().get(0);
        Solution suggestion = new Solution(vader, cantina, detonator);
        assertNull(board.handleSuggestion(suggester, suggestion));
    }

    // Suggesting player is the only one who could disprove — should return null
    @Test
    public void testHandleSuggestionOnlySuggestingPlayerCanDisprove() {
        clearAllHands();
        Player suggester = board.getPlayers().get(0);
        suggester.addCard(vader);
        Solution suggestion = new Solution(vader, cantina, detonator);
        assertNull(board.handleSuggestion(suggester, suggestion));
    }
    
    // First player after suggester can disprove — should return that card
    @Test
    public void testHandleSuggestionFirstPlayerDisproves() {
        clearAllHands();
        Player suggester = board.getPlayers().get(0);
        Player next = board.getPlayers().get(1);
        Player third = board.getPlayers().get(2);

        next.addCard(vader);
        third.addCard(detonator);
        Solution suggestion = new Solution(vader, cantina, detonator);
        assertEquals(vader, board.handleSuggestion(suggester, suggestion));
    }

    // First player can't disprove, second can — should return second's card
    @Test
    public void testHandleSuggestionSecondPlayerDisproves() {
        clearAllHands();
        Player suggester = board.getPlayers().get(0);
        Player third = board.getPlayers().get(2);

        third.addCard(vader);
        Solution suggestion = new Solution(vader, cantina, detonator);
        assertEquals(vader, board.handleSuggestion(suggester, suggestion));
    }

}
