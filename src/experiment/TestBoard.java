package experiment;

import java.util.HashSet;
import java.util.Set;

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
