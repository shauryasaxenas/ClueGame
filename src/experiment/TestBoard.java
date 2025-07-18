package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * Class: TestBoard
 * 
 * Purpose:
 * Represents a test board composed of multiple TestBoardCell instances.
 * Responsible for managing board-related computations such as calculating
 * target cells reachable from a given starting cell within a specified path length.
 * 
 * Author(s): Shaurya Saxena
 * Date: July 18, 2025
 * Collaborators: None
 * Sources: None
 */

public class TestBoard {

	public TestBoard() {
		super();
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		return;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return new TestBoardCell(row, col);
	}
	
	public Set<TestBoardCell> getTargets() {
		return new HashSet<>();
	}
}
