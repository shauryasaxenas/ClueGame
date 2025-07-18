package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 * Class: TestBoardCell
 * 
 * Purpose:
 * This class represents a cell on a test board grid with row and column coordinates.
 * It manages adjacency to other cells, whether the cell is part of a room, and if it
 * is currently occupied.
 * 
 * Author(s): Shaurya Saxena, Logan Matthews
 * Date: July 18, 2025
 * Collaborators: None
 * Sources: None
 */

public class TestBoardCell {
	private int row; 
	private int column;
	
	public TestBoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}
	
	public void addAdjacency(TestBoardCell cell) {
		return;
	}
	
	public Set<TestBoardCell> getAdjList() {
		return new HashSet<>();
	}
	
	public void setRoom(boolean isRoom) {
		return;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public void setOccupied(boolean occupied) {
		return;
	}
	
	public boolean getOccupied() {
		return false;
	}
	
}
