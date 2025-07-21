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
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	public final static int COLS = 4;
	public final static int ROWS = 4;

	public TestBoard() {
		super();
		grid = new TestBoardCell[ROWS][COLS];
		
		// Initializes each cell in the grid as a TestBoardCell object
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				grid[row][col] = new TestBoardCell(row, col);
			}
		}
		
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (row > 0) {
					grid[row][col].addAdjacency(grid[row - 1][col]); // Checks the row above
				}
				
				if (row < ROWS - 1) {
					grid[row][col].addAdjacency(grid[row + 1][col]); // Checks the row below
				}
				
				if (col > 0) {
					grid[row][col].addAdjacency(grid[row][col - 1]); // Checks the column to the left
				}
				
				if (col < COLS - 1) {
					grid[row][col].addAdjacency(grid[row][col + 1]); // Checks the column to the right
				}
			}
		}
		
	}
	
	public void findAllTargets(TestBoardCell cell, int stepsRemaining) {
		for (TestBoardCell adjCell : cell.getAdjList()) {
			if (!visited.contains(adjCell) && !adjCell.getOccupied()) {
				visited.add(adjCell); // Add adjacent cell to visited if not in visited and the cell isn't occupied
				
				// If adjacent cell is a room or only 1 step remaining
				if (adjCell.isRoom() || stepsRemaining == 1) {
					targets.add(adjCell);
				} else { 
					// Otherwise remove one step recursively
					findAllTargets(adjCell, stepsRemaining - 1);
				}
				visited.remove(adjCell);
			}
			
		}
	}

	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets = new HashSet<>();
		visited = new HashSet<>();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}
	
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}
