package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row, col;
	private char initial;
	private DoorDirection doorDirection = DoorDirection.NONE;
	private boolean roomLabel, roomCenter = false;
	private char secretPassage;
	private Set<BoardCell> adjList = new HashSet<>();

	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getInitial() {
		return initial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public Character getSecretPassage() {
		return secretPassage;
	}
	
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	
	

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void setDoorDirection(DoorDirection direction) {
		this.doorDirection = direction;
	}

	public void setRoomLabel(boolean label) {
		this.roomLabel = label;
	}

	public void setRoomCenter(boolean center) {
		this.roomCenter = center;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public void addAdjacency(BoardCell cell) {
		adjList.add(cell);
	}
}
