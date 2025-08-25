package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single cell on the game board.
 * Stores information such as location, room initial, door direction, and adjacency.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 21, 2025
 */

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection = DoorDirection.NONE;
	private boolean roomLabel = false;
	private boolean roomCenter = false;
	private char secretPassage = ' ';
	private Set<BoardCell> adjList = new HashSet<>();
	private boolean isOccupied = false;
	private boolean isHighlighted = false;
	
	public BoardCell() {
		
	}
	
	public BoardCell(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public void setLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}
	
	public void addAdj(BoardCell adj) {
		adjList.add(adj);
	}
	
	public Set<BoardCell> getAdjList() {
		return adjList;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return col;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	
	public boolean isRoom() {
	    return initial != 'W' && initial != 'X';
	}
	
	public void setHighlighted(boolean highlight) {
	    this.isHighlighted = highlight;
	}

	public boolean isHighlighted() {
	    return isHighlighted;
	}
	
	public void draw(Graphics g, int cellSize) {
	    if (isHighlighted) {
	        g.setColor(Color.CYAN);  // highlight color
	    } else {
	        g.setColor(Color.LIGHT_GRAY); // normal color
	    }
	    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
	    g.setColor(Color.BLACK);
	    g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
	}
}
