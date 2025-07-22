package clueGame;

import java.util.*;
import java.io.*;

/**
 * Board represents the game board in the Clue game.
 * It is implemented as a singleton to ensure only one board exists.
 * 
 * This class supports initialization, lookup, and basic board info queries.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: July 21, 2025
 */

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap = new HashMap<>();
	
	/*
     * variable and methods used for singleton pattern
     */
     private static Board theInstance = new Board();
     // constructor is private to ensure only one can be created
     private Board() {
            super();
     }
     // this method returns the only Board
     public static Board getInstance() {
            return new Board();
     }
     /*
      * initialize the board (since we are using singleton pattern)
      */
     public void initialize()
     {
    		 loadSetupConfig();
    		 loadLayoutConfig();
     }
     
     public void loadLayoutConfig() {
    	    List<String[]> lines = new ArrayList<>();

    	    try (BufferedReader reader = new BufferedReader(new FileReader(layoutConfigFile))) {
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            line = line.trim();
    	            if (line.startsWith("//") || line.isEmpty()) continue;
    	            String[] cells = line.split("\t"); 
    	            lines.add(cells);
    	        }
    	    } catch (IOException e) {
    	        System.err.println("Error reading layout file: " + e.getMessage());
    	        return; // silently return on error
    	    }

    	    if (lines.isEmpty()) {
    	        System.err.println("Layout file is empty!");
    	        return;
    	    }

    	    numRows = lines.size();
    	    numColumns = lines.get(0).length;

    	    grid = new BoardCell[numRows][numColumns];

    	    for (int row = 0; row < numRows; row++) {
    	        String[] rowCells = lines.get(row);
    	        for (int col = 0; col < numColumns; col++) {
    	            char initial = (col < rowCells.length && rowCells[col].length() > 0) ? rowCells[col].charAt(0) : ' ';
    	            grid[row][col] = new BoardCell(row, col);
    	        }
    	    }
    	}


     
     public void loadSetupConfig() {
    	    try (BufferedReader reader = new BufferedReader(new FileReader(setupConfigFile))) {
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            line = line.trim();
    	            if (line.startsWith("//") || line.isEmpty()) continue;
    	            String[] parts = line.split(",");
    	            if (parts.length >= 3) {
    	                String name = parts[1].trim();
    	                char initial = parts[2].trim().charAt(0);
    	                Room room = new Room(name);
    	                roomMap.put(initial, room);
    	            }
    	        }
    	    } catch (IOException e) {
    	        System.err.println("Error reading setup file: " + e.getMessage());
    	    }
    	}

     
     public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
    	 this.layoutConfigFile = layoutConfigFile;
    	 this.setupConfigFile = setupConfigFile;
     }
     
     public Room getRoom(char roomInitial) {
    	 return new Room("N/A");
     }
     
     public int getNumRows() {
    	 return 0;
     }
     
     public int getNumColumns() {
    	 return 0;
     }
     
     public BoardCell getCell(int row, int column) {
    	 return new BoardCell(row, column);
     }
     
     public Room getRoom(BoardCell cell) {
    	 return null;
     }

	 public Map<Character, Room> getRoomMap() {
		 return new HashMap<>();
	 }
     
     
}
