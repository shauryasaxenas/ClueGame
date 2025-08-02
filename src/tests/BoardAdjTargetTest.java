package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
        board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
        board.initialize();
    }
    
    // Walkway adjacency only: (4,1), (13,13)
    @Test
    public void testWalkwayAdjacency() {
        Set<BoardCell> adj1 = board.getAdjList(4, 1);
        assertTrue(adj1.contains(board.getCell(4, 0)));
        assertTrue(adj1.contains(board.getCell(4, 2)));
        assertTrue(adj1.contains(board.getCell(3, 1)));
        assertEquals(3, adj1.size()); 
        
        Set<BoardCell> adj2 = board.getAdjList(13, 13);
        assertTrue(adj2.contains(board.getCell(12, 13)));
        assertTrue(adj2.contains(board.getCell(14, 13)));
        assertEquals(4, adj2.size());
    }


    // In-room non-center cell: (3,10), (13,23)
    // @Test
    public void testRoomNonCenter_NoAdjacency() {
        BoardCell cell1 = board.getCell(3, 10);
        Set<BoardCell> adj1 = board.getAdjList(3, 10);
        assertEquals(0, adj1.size());
        
        BoardCell cell2 = board.getCell(13, 13);
        Set<BoardCell> adj2 = board.getAdjList(13, 13);
        assertEquals(0, adj2.size());
    }

    // Edge of Board: (9,23), (20,13)
    @Test
    public void testEdgeWalkway() {
    	Set<BoardCell> testList = board.getAdjList(9, 23);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(9, 22)));
        
        testList = board.getAdjList(20, 13);
        assertEquals(1, testList.size());
        assertTrue(testList.contains(board.getCell(19, 13)));
    }

    // Beside Room Wall: (7,5), (5,12)
    @Test
    public void testBesideRoomWall() {
        Set<BoardCell> adj1 = board.getAdjList(7, 5);
        assertEquals(3, adj1.size());
        assertTrue(adj1.contains(board.getCell(6, 5)));
        assertTrue(adj1.contains(board.getCell(7, 6)));
        assertTrue(adj1.contains(board.getCell(8, 5)));
        
        adj1 = board.getAdjList(5, 12);
        assertEquals(3, adj1.size());
        assertTrue(adj1.contains(board.getCell(5, 11)));
        assertTrue(adj1.contains(board.getCell(6, 12)));
        assertTrue(adj1.contains(board.getCell(5, 13)));
    }

    // Doorway Cell: (3,5), (4,9)
    @Test
    public void testDoorwayAdjacencyAndBehavior() {
        BoardCell doorway1 = board.getCell(3, 5);
        assertTrue("Expected doorway at (3,5)", doorway1.isDoorway());
        assertEquals("Doorway direction incorrect at (3,5)", DoorDirection.RIGHT, doorway1.getDoorDirection());
        Set<BoardCell> adj1 = board.getAdjList(3, 5);
        assertEquals(3, adj1.size());
        assertTrue(adj1.contains(board.getCell(4, 5)));

        // Doorway at (4,9) facing UP (should connect to (3,9))
        BoardCell doorway2 = board.getCell(4, 9);
        assertTrue("Expected doorway at (4,9)", doorway2.isDoorway());
        assertEquals("Doorway direction incorrect at (4,9)", DoorDirection.UP, doorway2.getDoorDirection());
        Set<BoardCell> adj2 = board.getAdjList(4, 9);
        assertEquals("Wrong number of adjacents for doorway (4,9)", 3, adj2.size());
        assertTrue(adj2.contains(board.getCell(5, 9)));
    }

    // Secret Passage Room Centers: (17,3), (18,18)
    @Test
    public void testSecretPassageRoom() {
        Set<BoardCell> adj1 = board.getAdjList(17, 3);
        
        boolean foundRoomCenter1 = false;
        for (BoardCell adj : adj1) {
            if (adj.isRoomCenter()) {
                foundRoomCenter1 = true;
                break;
            }
        }
        assertTrue(foundRoomCenter1);

        Set<BoardCell> adj2 = board.getAdjList(18, 18);
        boolean foundRoomCenter2 = false;
        for (BoardCell adj : adj2) {
            if (adj.isRoomCenter()) {
                foundRoomCenter2 = true;
                break;
            }
        }
        assertTrue(foundRoomCenter2);
    }

    
    // Target along walkways: (4,0), (1,13)
    @Test
    public void testTargetWalkways2Steps() {
        // Test starting from (4,0) with 2 steps
        BoardCell start1 = board.getCell(4, 0);
        board.calcTargets(start1, 2);
        Set<BoardCell> targets1 = board.getTargets();
        assertTrue(targets1.size() >= 2);
        assertTrue(targets1.contains(board.getCell(3, 1)));
        assertTrue(targets1.contains(board.getCell(4, 2)));

        // Test starting from (1,13) with 2 steps
        BoardCell start2 = board.getCell(1, 13);
        board.calcTargets(start2, 2);
        Set<BoardCell> targets2 = board.getTargets();
        assertTrue(targets2.size() == 1);
        assertTrue(targets2.contains(board.getCell(3, 13)));
    }


    // Enter room: (10,4), (3,5)
    @Test
    public void testEnterRoom() {
        BoardCell start1 = board.getCell(3, 5);
        board.calcTargets(start1, 1);
        Set<BoardCell> targets1 = board.getTargets();

        boolean foundRoomCenter1 = false;
        for (BoardCell target : targets1) {
            if (target.isRoomCenter()) {
                foundRoomCenter1 = true;
            }
        }
        assertTrue(foundRoomCenter1);

        BoardCell start2 = board.getCell(10, 4);
        board.calcTargets(start2, 1);
        Set<BoardCell> targets2 = board.getTargets();

        boolean foundRoomCenter2 = false;
        for (BoardCell target : targets2) {
            if (target.isRoomCenter()) {
                foundRoomCenter2 = true;
            }
        }
        assertTrue(foundRoomCenter2);
    }


    // Leave room without secret passage: (2,11)
    @Test
    public void testExitRoom() {
        // Starting from a room center that does NOT have a secret passage
        BoardCell start = board.getCell(2, 11);

        // Ensure the cell is indeed a room center and has no secret passage
        assertTrue(start.isRoomCenter());
        assertEquals(' ', start.getSecretPassage());

        // Find targets with 2 steps
        board.calcTargets(start, 2);
        Set<BoardCell> targets = board.getTargets();

        assertFalse("Target list should not be empty", targets.isEmpty());
        assertTrue(targets.contains(board.getCell(5, 9)));
        assertTrue(targets.contains(board.getCell(4, 8)));

        // Should NOT include other room centers (unless through a passage)
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                fail("Should not reach another room center without secret passage");
            }
        }
    }
    
    @Test
    public void testTargetsOccupied() {
        // Occupied cell should NOT be reachable
        // Mark (4,8) as occupied
        board.getCell(4, 8).setOccupied(true);

        BoardCell start1 = board.getCell(4, 5);
        board.calcTargets(start1, 2);
        Set<BoardCell> targets1 = board.getTargets();

        // Occupied cell (4,8) should NOT be in the target list
        assertFalse(targets1.contains(board.getCell(4, 8)));

        // Reset occupation
        board.getCell(4, 8).setOccupied(false);

        // Occupied cell should NOT be reachable
        // Mark (11,5) as occupied
        board.getCell(11, 5).setOccupied(true);
        
        BoardCell start2 = board.getCell(11, 3);
        board.calcTargets(start2, 3);
        Set<BoardCell> targets2 = board.getTargets();

        // Occupied cell (11,5) should NOT be in the target list
        assertFalse(targets2.contains(board.getCell(11, 5)));
    }



    @Test
    public void testExitRoomWithPassage() {
        // Start from a room center that has a secret passage
        // (e.g., assume cell (8,2) is center of room 'C' and has secret passage to 'O')
        BoardCell start = board.getCell(8, 2);
        assertTrue(start.isRoomCenter());
        assertEquals('C', start.getSecretPassage());

        // Calculate targets with a roll of 1
        board.calcTargets(start, 1);
        Set<BoardCell> targets = board.getTargets();

        // Get the expected destination room's center cell
        Room destinationRoom = board.getRoom('C');
        BoardCell destinationCenter = destinationRoom.getCenterCell();

        // Assert that the secret passage destination is in the targets
        assertTrue(targets.contains(destinationCenter));
    }

}
