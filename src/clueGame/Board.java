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
	private static final char CENTER_MARKER = '*';
	private static final char DOOR_RIGHT = '>';
	private static final char DOOR_LEFT = '<';
	private static final char DOOR_DOWN = 'v';
	private static final char DOOR_UP = '^';
	private static final String CSV_DELIMITER = ",";
	private static final String COMMENT_PREFIX = "//";
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
            return theInstance;
     }
     /*
      * initialize the board (since we are using singleton pattern)
      */
     public void initialize() {
         try {
             loadSetupConfig();
             loadLayoutConfig();
         } catch (BadConfigFormatException e) {
             // Log the error (for extra credit)
             System.err.println("Configuration error: " + e.getMessage());
         }
     }
     
     public void loadLayoutConfig() throws BadConfigFormatException {
    	    // Clear existing data for singleton pattern
    	    grid = null;
    	    numRows = 0;
    	    numColumns = 0;
    	    
    	    List<String[]> lines = new ArrayList<>();

    	    readLayoutFile(lines);

    	    validateAndSetGridDimensions(lines);

    	    grid = new BoardCell[numRows][numColumns];

    	    for (int row = 0; row < numRows; row++) {
    	        String[] rowCells = lines.get(row);
    	        for (int col = 0; col < numColumns; col++) {
    	            parseCellData(row, rowCells, col);
    	        }
    	    }
    	    
    	    linkCellsToRooms();
    	}
     
	 private void linkCellsToRooms() {
		for (int row = 0; row < numRows; row++) {
		    for (int col = 0; col < numColumns; col++) {
		        BoardCell cell = grid[row][col];
		        Room room = roomMap.get(cell.getInitial());
		        
		        if (cell.isLabel()) {
		            room.setLabelCell(cell);
		        }
		       
		        if (cell.isRoomCenter()) {
		            room.setCenterCell(cell);
		        }
		    }
		}
	 }
	 private void validateAndSetGridDimensions(List<String[]> lines) throws BadConfigFormatException {
		numRows = lines.size();
		numColumns = lines.get(0).length;
		
		// Check for consistent column count
		for (int i = 0; i < lines.size(); i++) {
		    if (lines.get(i).length != numColumns) {
		        throw new BadConfigFormatException("Row " + i + " has " + lines.get(i).length + 
		            " columns, expected " + numColumns);
		    }
		}
	 }
     
	 private void readLayoutFile(List<String[]> lines) throws BadConfigFormatException {
		try (BufferedReader reader = new BufferedReader(new FileReader(layoutConfigFile))) {
		    String line;
		    while ((line = reader.readLine()) != null) {
		        line = line.trim();
		        if (line.startsWith(COMMENT_PREFIX) || line.isEmpty()) continue;
		        
		        String[] cells = line.split(CSV_DELIMITER);
		        
		        // Trim each cell to remove extra whitespace
		        for (int i = 0; i < cells.length; i++) {
		            cells[i] = cells[i].trim();
		        }
		        
		        lines.add(cells);
		    }
		} catch (IOException e) {
		    throw new BadConfigFormatException("Error reading layout file: " + e.getMessage());
		}

		if (lines.isEmpty()) {
		    throw new BadConfigFormatException("Layout file is empty!");
		}
	 }
     
	 private void parseCellData(int row, String[] rowCells, int col) throws BadConfigFormatException {
		String cellData = rowCells[col];
		if (cellData.isEmpty()) {
		    throw new BadConfigFormatException("Empty cell data at row " + row + ", col " + col);
		}
		
		char initial;
		DoorDirection doorDir = DoorDirection.NONE;
		boolean isCenter = false;
		boolean isLabel = false;
		char secretPassage = ' ';
		
		// Check if first character is a direction indicator
		if (cellData.length() > 1) {
		    char firstChar = cellData.charAt(0);
		    switch (firstChar) {
		        case DOOR_UP:
		            doorDir = DoorDirection.UP;
		            initial = cellData.charAt(1);
		            break;
		        case DOOR_DOWN:
		            doorDir = DoorDirection.DOWN;
		            initial = cellData.charAt(1);
		            break;
		        case DOOR_LEFT:
		            doorDir = DoorDirection.LEFT;
		            initial = cellData.charAt(1);
		            break;
		        case DOOR_RIGHT:
		            doorDir = DoorDirection.RIGHT;
		            initial = cellData.charAt(1);
		            break;
		        default:
		            // First character is the room initial
		            initial = firstChar;
		            char secondChar = cellData.charAt(1);
		            switch (secondChar) {
		                case DOOR_UP:
		                    doorDir = DoorDirection.UP;
		                    break;
		                case DOOR_DOWN:
		                    doorDir = DoorDirection.DOWN;
		                    break;
		                case DOOR_LEFT:
		                    doorDir = DoorDirection.LEFT;
		                    break;
		                case DOOR_RIGHT:
		                    doorDir = DoorDirection.RIGHT;
		                    break;
		                case CENTER_MARKER:
		                    isCenter = true;
		                    break;
		                case '#':
		                    isLabel = true;
		                    break;
		                default:
		                    // Could be a secret passage (uppercase letter)
		                    if (Character.isLetter(secondChar) && Character.isUpperCase(secondChar)) {
		                        secretPassage = secondChar;
		                    }
		                    break;
		            }
		            break;
		    }
		} else {
		    // Single character - just the room initial
		    initial = cellData.charAt(0);
		}
		
		// Check if room exists in setup
		if (!roomMap.containsKey(initial)) {
		    throw new BadConfigFormatException("Room '" + initial + "' not found in setup file at row " + row + ", col " + col);
		}
		
		BoardCell cell = new BoardCell(row, col);
		cell.setInitial(initial);
		cell.setDoorDirection(doorDir);
		cell.setRoomCenter(isCenter);
		cell.setLabel(isLabel);
		if (secretPassage != ' ') {
		    cell.setSecretPassage(secretPassage);
		}
		
		grid[row][col] = cell;
	 }


     
     public void loadSetupConfig() throws BadConfigFormatException {
    	    // Clear existing room map for singleton pattern
    	    roomMap.clear();
    	    
    	    try (BufferedReader reader = new BufferedReader(new FileReader(setupConfigFile))) {
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            line = line.trim();
    	            if (line.startsWith(COMMENT_PREFIX) || line.isEmpty()) continue;
    	            
    	            parseSetupLine(line);
    	        }
    	    } catch (IOException e) {
    	        throw new BadConfigFormatException("Error reading setup file: " + e.getMessage());
    	    }
    	}
	 private void parseSetupLine(String line) throws BadConfigFormatException {
		String[] parts = line.split(CSV_DELIMITER);
		if (parts.length < 3) {
		    throw new BadConfigFormatException("Invalid setup file format: " + line);
		}
		
		String type = parts[0].trim();
		String name = parts[1].trim();
		String initialStr = parts[2].trim();
		
		if (initialStr.length() != 1) {
		    throw new BadConfigFormatException ("Room initial must be single character: " + initialStr);
		}
		
		char initial = initialStr.charAt(0);
		
		if (type.equals("Room") || type.equals("Space")) {
		    Room room = new Room(name);
		    roomMap.put(initial, room);
		} else {
		    throw new BadConfigFormatException("Unknown type in setup file: " + type);
		}
	 }

     
     public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
    	 this.layoutConfigFile = layoutConfigFile;
    	 this.setupConfigFile = setupConfigFile;
     }
     
     public Room getRoom(char roomInitial) {
    	 return roomMap.get(roomInitial);
     }
     
     public int getNumRows() {
    	 return numRows;
     }
     
     public int getNumColumns() {
    	 return numColumns;
     }
     
     public BoardCell getCell(int row, int column) {
    	 return grid[row][column];
     }
     
     public Room getRoom(BoardCell cell) {
    	 return roomMap.get(cell.getInitial());
     }

	 public Map<Character, Room> getRoomMap() {
		 return roomMap;
	 }
     
     
}
