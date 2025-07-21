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
	private int row, column;
	private Boolean isRoom, isOccupied;
	private Set<TestBoardCell> adjList;
	
	
	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.column = col;
		this.adjList = new HashSet<>();
		this.isRoom = false;
		this.isOccupied = false;
	}
	
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	public boolean getOccupied() {
		return isOccupied;
	}
	
}
