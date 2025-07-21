package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Board {
	private BoardCell[][] grid;
	private int numRows, numColumns;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap = new HashMap<>();
	
	private static Board theInstance = new Board();
	
	public static Board getInstance() {
		return theInstance;
	}
	
	private Board() {}

	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}
	
	public void initialize() {
		loadSetupConfig();
		loadLayoutConfig();
	}
	
	public void loadSetupConfig() {
		try {
			File file = new File(setupConfigFile);
			Scanner scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Setup file not found: " + setupConfigFile);
			e.printStackTrace();
		}
	}
	
	public void loadLayoutConfig() {
		
	}
	
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
}
