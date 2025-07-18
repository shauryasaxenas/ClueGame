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

}
