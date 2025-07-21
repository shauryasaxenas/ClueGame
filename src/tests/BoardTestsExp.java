package tests;

import static org.junit.jupiter.api.Assertions.*;

import experiment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

/**
 * Class: BoardTestsExp
 * 
 * Purpose:
 * Unit tests for the TestBoard and TestBoardCell classes in the experiment package.
 * Tests cover adjacency lists, target calculation with varying path lengths,
 * cell occupancy, and special cases such as room cells that stop movement.
 * 
 * Author(s): Shaurya Saxena, Logan Matthews
 * Date: July 18, 2025
 * Collaborators: None
 * Sources: None
 */

class BoardTestsExp {
	private TestBoard board;
	
	@BeforeEach
	public void setup() {
		board = new TestBoard();
	}
	
	@Test
	public void testAdjacencyTopLeft() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> list = cell.getAdjList();
		
		assertEquals(2, list.size());
		assertTrue(list.contains(board.getCell(0, 1)));
		assertTrue(list.contains(board.getCell(1, 0)));
	}
	
	@Test
	public void testAdjacencyBottomRight() {
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> list = cell.getAdjList();
		
		assertEquals(2, list.size());
		assertTrue(list.contains(board.getCell(3, 2)));
		assertTrue(list.contains(board.getCell(2, 3)));
	}
	
	@Test
	public void testAdjacencyRightEdge() {
		TestBoardCell cell = board.getCell(2, 3);
		Set<TestBoardCell> list = cell.getAdjList();
		
		assertEquals(3, list.size());
		assertTrue(list.contains(board.getCell(1, 3)));
		assertTrue(list.contains(board.getCell(2, 2)));
		assertTrue(list.contains(board.getCell(3, 3)));
	}
	
	@Test
	public void testAdjacencyLeftEdge() {
		TestBoardCell cell1 = board.getCell(1, 0);
		Set<TestBoardCell> list1 = cell1.getAdjList();
		
		assertEquals(3, list1.size());
		assertTrue(list1.contains(board.getCell(0, 0)));
		assertTrue(list1.contains(board.getCell(2, 0)));
		assertTrue(list1.contains(board.getCell(1, 1)));
		
		TestBoardCell cell2 = board.getCell(3, 0);
		Set<TestBoardCell> list2 = cell2.getAdjList();
		
		assertEquals(2, list2.size());
		assertTrue(list2.contains(board.getCell(3, 1)));
		assertTrue(list2.contains(board.getCell(2, 0)));
	}
	
	@Test
	public void testAdjacencyMiddle() {
		TestBoardCell cell = board.getCell(2, 2);
		Set<TestBoardCell> list = cell.getAdjList();
		
		assertEquals(4, list.size());
		assertTrue(list.contains(board.getCell(2, 1)));
		assertTrue(list.contains(board.getCell(3, 2)));
		assertTrue(list.contains(board.getCell(2, 3)));
		assertTrue(list.contains(board.getCell(1, 2)));
	}
	
	@Test
	public void testEmptyBoard() {
		TestBoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 1)));
	}
	
	@Test
	public void testOneCellOccupied() {
		TestBoardCell cell = board.getCell(1, 1);
		board.getCell(1, 2).setOccupied(true);
		board.calcTargets(cell, 1);
		
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertFalse(targets.contains(board.getCell(1, 2)));
	}
	
	@Test
	public void testOneCellRoom() {
		TestBoardCell startCell = board.getCell(1, 1);
		
		board.getCell(2, 1).setRoom(true); // Position (2,1) is a room
		
		board.calcTargets(startCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		
		/* 
		Since (2,1) is a room, movement stops even if steps remain
		This allows the valid targets to only be:  
			(0,1) -> (0,0) , (0,2)
			(1,0) -> (0,0) , (2,0) 
			(1,2) -> (0,2) , (2,2)
		*/
		
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertFalse(targets.contains(board.getCell(3, 1)));
	}
	
	@Test
	public void testCalcTargetsWith6Steps() {
		TestBoardCell startCell = board.getCell(0, 0);
		
		board.calcTargets(startCell, 6);
		Set<TestBoardCell> targets = board.getTargets();
		
		assertTrue(targets.size() > 5);
	}
	
	@Test
	public void testCalcTargetsWithTwoSteps() {
		TestBoardCell startCell = board.getCell(2, 2);
		
		board.calcTargets(startCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		
		// Should have more than 3 targets at 2 steps from startCell
		assertTrue(targets.size() >= 4);
	}
	
	@Test
	public void testCalcTargetsWithTwoStepsAndOneRoom() {
		TestBoardCell startCell = board.getCell(2, 2);
		
		board.getCell(2, 3).setRoom(true);
		
		board.calcTargets(startCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(2, 3))); // room
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1,1)));
		assertTrue(targets.contains(board.getCell(1,3)));
		assertTrue(targets.contains(board.getCell(3,1)));
		assertTrue(targets.contains(board.getCell(3,3)));
		assertTrue(targets.contains(board.getCell(2,0)));
	}
	

}
