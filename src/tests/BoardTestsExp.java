package tests;

import static org.junit.jupiter.api.Assertions.*;

import experiment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

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
		TestBoardCell cell = board.getCell(1, 0);
		Set<TestBoardCell> list = cell.getAdjList();
		
		assertEquals(3, list.size());
		assertTrue(list.contains(board.getCell(0, 0)));
		assertTrue(list.contains(board.getCell(2, 0)));
	}

}
