package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

/**
 * Class: BoardAdjTargetTest
 * 
 * This class tests that movement on the Clue board behaves 
 * correctly in different situations like hallways, rooms, 
 * and secret passages. It helps make sure the game's logic
 * works before anything gets played.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 22, 2025
 */

public class BoardAdjTargetTest {

    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("data/ClueLayout306.csv", "data/ClueSetup306.txt");
        board.initialize();
    }
    
    // Walkway adjacency only: (4,2), (13,13)
    @Test
    public void testWalkwayAdjacency() {
        BoardCell cell1 = board.getCell(4, 2);
        Set<BoardCell> adj1 = board.getAdjList(4, 2);
        assertTrue(adj1.contains(board.getCell(4, 1)));
        assertTrue(adj1.contains(board.getCell(5, 2)));
        assertTrue(adj1.contains(board.getCell(3, 2)));
        assertEquals(3, adj1.size()); 
        
        BoardCell cell2 = board.getCell(13, 13);
        Set<BoardCell> adj2 = board.getAdjList(13, 13);
        assertTrue(adj2.contains(board.getCell(12, 13)));
        assertTrue(adj2.contains(board.getCell(14, 13)));
        assertEquals(2, adj2.size());
    }


    // In-room non-center cell: (3,10), (13,23)
    @Test
    public void testRoomNonCenter_NoAdjacency() {
        BoardCell cell1 = board.getCell(3, 10);
        Set<BoardCell> adj1 = board.getAdjList(3, 10);
        assertEquals(0, adj1.size());
        
        BoardCell cell2 = board.getCell(13, 13);
        Set<BoardCell> adj2 = board.getAdjList(13, 13);
        assertEquals(0, adj2.size());
    }

    // Edge of Board: (9,24), (20,13)
    @Test
    public void testEdgeWalkway() {
        BoardCell cell1 = board.getCell(9, 23);
        Set<BoardCell> adj1 = board.getAdjList(9, 24);
        assertTrue(adj1.size() > 0);
        
        BoardCell cell2 = board.getCell(20, 13);
        Set<BoardCell> adj2 = board.getAdjList(20, 13);
        assertTrue(adj2.size() > 0);
    }

    // Beside Room Wall: (7,5), (5,12)
    @Test
    public void testBesideRoomWall() {
        BoardCell cell1 = board.getCell(7, 5);
        Set<BoardCell> adj1 = board.getAdjList(7, 5);
        for (BoardCell adjCell1 : adj1) {
            assertFalse(adjCell1.isRoomCenter()); // not entering room
        }
        
        BoardCell cell2 = board.getCell(5, 12);
        Set<BoardCell> adj2 = board.getAdjList(5, 12);
        for (BoardCell adjCell2 : adj2) {
            assertFalse(adjCell2.isRoomCenter()); // not entering room
        }
    }

    // Doorway Cell: (3,5), (4,9)
    @Test
    public void testDoorway() {
        BoardCell cell1 = board.getCell(3, 5);
        Set<BoardCell> adj1 = board.getAdjList(3, 5);
        assertTrue(adj1.size() > 0);
        
        BoardCell cell2 = board.getCell(4, 9);
        Set<BoardCell> adj2 = board.getAdjList(4, 9);
        assertTrue(adj2.size() > 0);
    }

    // Secret Passage Room Centers: (19,5), (2,22)
    @Test
    public void testSecretPassageRoom() {
        BoardCell cell1 = board.getCell(19, 5); // Room center with passage
        Set<BoardCell> adj1 = board.getAdjList(19, 5);
        assertTrue(adj1.stream().anyMatch(BoardCell::isRoomCenter));
        
        BoardCell cell2 = board.getCell(2, 22); // Room center with passage
        Set<BoardCell> adj2 = board.getAdjList(2, 22);
        assertTrue(adj2.stream().anyMatch(BoardCell::isRoomCenter));
    }
    
    // Target along walkways: (4,0), (1,13)
    @Test
    public void testTargetWalkways2Steps() {
        BoardCell start1 = board.getCell(4, 0);
        board.calcTargets(start1, 2);
        Set<BoardCell> targets1 = board.getTargets();
        assertTrue(targets1.size() >= 2);
        
        BoardCell start2 = board.getCell(1, 13);
        board.calcTargets(start2, 2);
        Set<BoardCell> targets2 = board.getTargets();
        assertTrue(targets2.size() >= 2);
    }

    // Enter room: (10,5), (4,5)
    @Test
    public void testEnterRoom() {
        BoardCell start1 = board.getCell(4, 5);
        board.calcTargets(start1, 1);
        Set<BoardCell> targets1 = board.getTargets();
        assertTrue(targets1.stream().anyMatch(BoardCell::isRoomCenter));
        
        BoardCell start2 = board.getCell(10, 5);
        board.calcTargets(start2, 1);
        Set<BoardCell> targets2 = board.getTargets();
        assertTrue(targets2.stream().anyMatch(BoardCell::isRoomCenter));
    }

    // Leave room without secret passage: (2,11)
    @Test
    public void testExitRoom() {
        BoardCell start = board.getCell(2, 11); // Room center
        board.calcTargets(start, 2);
        Set<BoardCell> targets = board.getTargets();
        assertTrue(targets.size() > 0);
    }
    
    @Test
    public void testTargetsOccupied() {
        // Mark (4,8) as occupied
        board.getCell(4, 8).setOccupied(true);

        // Start from (4,5) with 2 steps
        BoardCell start1 = board.getCell(4, 5);
        board.calcTargets(start1, 2);
        Set<BoardCell> targets1 = board.getTargets();

        // Occupied cell (4,8) should NOT be in the target list
        assertFalse(targets1.contains(board.getCell(4, 8)));

        // Reset occupation
        board.getCell(4, 8).setOccupied(false);
        
        // Mark (11,5) as occupied
        board.getCell(11, 5).setOccupied(true);

        // Start from (11,3) with 3 steps
        BoardCell start2 = board.getCell(11, 3);
        board.calcTargets(start2, 3);
        Set<BoardCell> targets2 = board.getTargets();

        // Occupied cell (11,5) should NOT be in the target list
        assertFalse(targets2.contains(board.getCell(11, 5)));

        // Reset occupation
        board.getCell(11, 5).setOccupied(false);
    }


    @Test
    public void testExitRoomWithPassage() {
        // Start from room center with a secret passage
        BoardCell start = board.getCell(8, 2);

        // Calculate 1-step targets (should include the passage exit)
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();

        // Ensure at least one room center (via secret passage) is in targets
        assertTrue(targets.stream().anyMatch(BoardCell::isRoomCenter));
    }

}
