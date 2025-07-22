package clueGame;

/**
 * Class: Room
 * Represents a room in the game with a name, and references to its center and label cells.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 21, 2025
 */

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	public Room(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}
	
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}
	
}
