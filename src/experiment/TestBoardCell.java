package experiment;

import java.util.HashSet;
import java.util.Set;

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
