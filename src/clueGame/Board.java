package clueGame;

import java.util.*;
import java.awt.Color;
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
	private Set<BoardCell> targets = new HashSet<>();
	private Set<BoardCell> visited;
	private List<Player> players = new ArrayList<>();
	private List<Card> deck = new ArrayList<>();
	private Solution theAnswer;
	
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
    		 
    		 players.clear();
    		 deck.clear();
    		 theAnswer = null;
    		    
    		 loadSetupConfig();
             loadLayoutConfig();
             dealSolution();
             dealRemainingCardsToPlayers();
      
//             dealCardsToPlayers();
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
    	    assignSecretPassagesToCenters();
    	}
     
     // Method to set all the cells to each room
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
	 
	 private void assignSecretPassagesToCenters() {
		 for (int row = 0; row < numRows; row++) {
			 for (int col = 0; col < numColumns; col++) {
				 BoardCell cell = grid[row][col];
				 char sp = cell.getSecretPassage();
				 if (sp != ' ') {
					 Room sourceRoom = roomMap.get(cell.getInitial());
					 if (sourceRoom != null) {
						 BoardCell centerCell = sourceRoom.getCenterCell();
						 if (centerCell != null) {
							 centerCell.setSecretPassage(sp);
						 }
					 }
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
     
	 // Parses each cell in the csv file looking for specific symbols
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
		    
		 if (cellData.length() > 1) {
			 char firstChar = cellData.charAt(0);
			 char secondChar = cellData.charAt(1);

			 // Check if first char is door direction (door leading into room)
			 switch (firstChar) {
			 case DOOR_UP:
				 doorDir = DoorDirection.UP;
				 initial = secondChar;
				 break;
			 case DOOR_DOWN:
				 doorDir = DoorDirection.DOWN;
				 initial = secondChar;
				 break;
			 case DOOR_LEFT:
				 doorDir = DoorDirection.LEFT;
				 initial = secondChar;
				 break;
			 case DOOR_RIGHT:
				 doorDir = DoorDirection.RIGHT;
				 initial = secondChar;
				 break;
			 default:
				 // First char is the room initial or walkway
				 initial = firstChar;
				 // Now check second char
				 if (secondChar == DOOR_UP || secondChar == DOOR_DOWN || secondChar == DOOR_LEFT || secondChar == DOOR_RIGHT) {
					 // Walkway or room cell with door direction
					 switch (secondChar) {
					 	case DOOR_UP: doorDir = DoorDirection.UP; break;
					 	case DOOR_DOWN: doorDir = DoorDirection.DOWN; break;
					 	case DOOR_LEFT: doorDir = DoorDirection.LEFT; break;
					 	case DOOR_RIGHT: doorDir = DoorDirection.RIGHT; break;
					 }
				 } else if (secondChar == CENTER_MARKER) {
					 isCenter = true;
				 } else if (secondChar == '#') {
					 isLabel = true;
				 } else if (Character.isLetter(secondChar) && Character.isUpperCase(secondChar)) {
					 secretPassage = secondChar;
				 } else {
					 throw new BadConfigFormatException("Invalid modifier character '" + secondChar + "' in cell: " + cellData + " at row " + row + ", col " + col);
				 }
				 break;
			 }
		 } else {	
			 // Single character cell
			 initial = cellData.charAt(0);
		 }

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
    	    if (parts.length < 2) {
    	        throw new BadConfigFormatException("Invalid setup file format: " + line);
    	    }

    	    String type = parts[0].trim();

    	    switch (type) {
    	        case "Room": {
    	            if (parts.length < 3) throw new BadConfigFormatException("Invalid room line: " + line);
    	            String name = parts[1].trim();
    	            String initialStr = parts[2].trim();
    	            if (initialStr.length() != 1) throw new BadConfigFormatException("Room initial must be single char: " + initialStr);
    	            char initial = initialStr.charAt(0);
    	            Room room = new Room(name);
    	            roomMap.put(initial, room);

    	            // Add a card for this room (only if it's a Room, not Space)
    	            deck.add(new Card(name, CardType.ROOM));
    	            break;
    	        }
    	        case "Space": {
    	            // Don't add a card for spaces (Walkway, Unused)
    	            String name = parts[1].trim();
    	            String initialStr = parts[2].trim();
    	            if (initialStr.length() != 1) throw new BadConfigFormatException("Space initial must be single char: " + initialStr);
    	            char initial = initialStr.charAt(0);
    	            Room space = new Room(name);  // You may want to track spaces if needed
    	            roomMap.put(initial, space);
    	            break;
    	        }
    	        case "Player": {
    	            if (parts.length < 5) throw new BadConfigFormatException("Invalid player line: " + line);
    	            String name = parts[1].trim();
    	            Color color = parseColor(parts[2].trim());
    	            int row = Integer.parseInt(parts[3].trim());
    	            int col = Integer.parseInt(parts[4].trim());
    	            Player player = new HumanPlayer(name, color, row, col);
    	            players.add(player);

    	            deck.add(new Card(name, CardType.PERSON)); // Add PERSON card
    	            break;
    	        }
    	        case "Computer": {
    	            if (parts.length < 5) throw new BadConfigFormatException("Invalid computer player line: " + line);
    	            String name = parts[1].trim();
    	            Color color = parseColor(parts[2].trim());
    	            int row = Integer.parseInt(parts[3].trim());
    	            int col = Integer.parseInt(parts[4].trim());
    	            Player player = new ComputerPlayer(name, color, row, col);
    	            players.add(player);

    	            deck.add(new Card(name, CardType.PERSON)); // Add PERSON card
    	            break;
    	        }
    	        case "Weapon": {
    	            if (parts.length < 2) throw new BadConfigFormatException("Invalid weapon line: " + line);
    	            String weaponName = parts[1].trim();
    	            deck.add(new Card(weaponName, CardType.WEAPON)); // Add WEAPON card
    	            break;
    	        }
    	        default:
    	            throw new BadConfigFormatException("Unknown setup type: " + type);
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
	 
	 private void findAllTargets(BoardCell cell, int stepsRemaining) {
		 for (BoardCell adjCell : getAdjList(cell.getRow(), cell.getColumn())) {
			 boolean isRoom = adjCell.isRoomCenter() || adjCell.isLabel() || adjCell.getInitial() != 'W';
			 if (!visited.contains(adjCell) && (!adjCell.isOccupied() || isRoom)) {
				 visited.add(adjCell);
				 if (adjCell.isRoomCenter() || stepsRemaining == 1) {
					 targets.add(adjCell);
				 } else {
					 findAllTargets(adjCell, stepsRemaining - 1);
				 }
				 visited.remove(adjCell);
			 }
		 }
	 }
	 
	 public void calcTargets(BoardCell startCell, int pathLength) {
		 targets = new HashSet<>();
		 visited = new HashSet<>();
		 
		 if (startCell.getInitial() != 'W' && !startCell.isRoomCenter()) {
			 return;
		 }
		 visited.add(startCell);
		 findAllTargets(startCell, pathLength);
	 }

	 
	 public Set<BoardCell> getTargets() {
		 return new HashSet<>(targets);
	 }
	 
	 public Set<BoardCell> getAdjList(int row, int col) {
		 Set<BoardCell> adj = new HashSet<>();
		 BoardCell cell = getCell(row, col);
		 
		 if (cell.getInitial() == 'W' && !cell.isDoorway()) {
			 addWalkwayAdjacency(row, col, adj);
		 }
		    
		 else if (cell.isDoorway()) {
			 DoorDirection dir = cell.getDoorDirection();
			 int targetRow = row;
			 int targetCol = col;
		        
			 // Step into the room in the direction the door is facing
			 switch (dir) {
			 	case UP:    targetRow = row - 1; break;
			 	case DOWN:  targetRow = row + 1; break;
			 	case LEFT:  targetCol = col - 1; break;
			 	case RIGHT: targetCol = col + 1; break;
			 	default: break;
			 }

			 // Make sure it's inside the board bounds
			 if (targetRow >= 0 && targetRow < getNumRows() && targetCol >= 0 && targetCol < getNumColumns()) {
				 BoardCell roomCell = getCell(targetRow, targetCol);
				 char roomInitial = roomCell.getInitial();  // Now we have the room's initial
				 Room room = roomMap.get(roomInitial);
				 if (room != null && room.getCenterCell() != null) {
					 adj.add(room.getCenterCell());  // Add center of the room
				 }
			 }

			 // Also add the adjacent walkways (like in your previous code)
			 // Example: cell above
			 addWalkwayAdjacency(row, col, adj);
		 } 
		    
		 else if (cell.isRoomCenter()) {
			 for (int r = 0; r < getNumRows(); r++) {
				 for (int c = 0; c < getNumColumns(); c++) {
					 BoardCell potentialDoor = getCell(r, c);
					 if (!potentialDoor.isDoorway()) {
						 continue;
					 }

					 DoorDirection dir = potentialDoor.getDoorDirection();
					 int targetRow = r, targetCol = c;

					 // Find the room cell the door leads into
					 switch (dir) {
					 	case UP:    targetRow = r - 1; break;
					 	case DOWN:  targetRow = r + 1; break;
					 	case LEFT:  targetCol = c - 1; break;
					 	case RIGHT: targetCol = c + 1; break;
					 	default: break;
					 }

					 BoardCell roomCell = getCell(targetRow, targetCol);
		               
					 if (roomCell.getInitial() == cell.getInitial()) {
						 adj.add(potentialDoor);
					 }
				 }
			 }

			 // Add secret passage, if any
			 char secret = cell.getSecretPassage();
			 System.out.println(secret);
			 if (secret != ' ') {
				 Room destinationRoom = roomMap.get(secret);
				 if (destinationRoom != null && destinationRoom.getCenterCell() != null) {
					 adj.add(destinationRoom.getCenterCell());
				 }
			 }
		 }
		 return adj;
	 }
	 
	 private void addWalkwayAdjacency(int row, int col, Set<BoardCell> adj) {
		if (row > 0) {
			 BoardCell above = getCell(row - 1, col);
			 if (above.getInitial() == 'W') {
				 adj.add(above);
			 }
		 }
		 if (row < getNumRows() - 1) {
			 BoardCell below = getCell(row + 1, col);
			 if (below.getInitial() == 'W') {
				 adj.add(below);
			 }
		 }
		 if (col > 0) {
			 BoardCell left = getCell(row, col - 1);
			 if (left.getInitial() == 'W') {
				 adj.add(left);
			 }
		 }
		 if (col < getNumColumns() - 1) {
			 BoardCell right = getCell(row, col + 1);
			 if (right.getInitial() == 'W') {
				 adj.add(right);
			 }
		 }
	 }
	 
	 public List<Player> getPlayers() {
		    return players;
		}
	 
	 public List<Card> getDeck() {
		    return deck;
		}
	 
	 public Solution getSolution() {
		    return theAnswer;
		}
	 
	 private List<Card> getCardsOfType(CardType type) {
		    List<Card> result = new ArrayList<>();
		    for (Card card : deck) {
		        if (card.getType() == type) {
		            result.add(card);
		        }
		    }
		    return result;
		}

	 
	 private void dealSolution() {
		    List<Card> people = getCardsOfType(CardType.PERSON);
		    List<Card> weapons = getCardsOfType(CardType.WEAPON);
		    List<Card> rooms = getCardsOfType(CardType.ROOM);

		    Random rand = new Random();
		    Card person = people.remove(rand.nextInt(people.size()));
		    Card weapon = weapons.remove(rand.nextInt(weapons.size()));
		    Card room = rooms.remove(rand.nextInt(rooms.size()));

		    theAnswer = new Solution(person, weapon, room);

		    // Add remaining cards back to the deck for dealing
		    deck.clear();
		    deck.addAll(people);
		    deck.addAll(weapons);
		    deck.addAll(rooms);
		}
	 
	 private void dealRemainingCardsToPlayers() {
		    List<Player> players = getPlayers(); // Assumes this returns all players
		    Collections.shuffle(deck); // Randomize order

		    int playerIndex = 0;
		    for (Card card : deck) {
		        players.get(playerIndex % players.size()).addCard(card);
		        playerIndex++;
		    }
		}

	 private Color parseColor(String colorName) throws BadConfigFormatException {
		    switch(colorName.toLowerCase()) {
		        case "blue": return Color.BLUE;
		        case "black": return Color.BLACK;
		        case "green": return Color.GREEN;
		        case "pink": return Color.PINK;
		        case "gray": return Color.GRAY;
		        case "lightgray": return Color.LIGHT_GRAY;
		        default:
		            throw new BadConfigFormatException("Unknown color: " + colorName);
		    }
	 }
	 
	 public boolean checkAccusation (Solution accusation) {
		 return theAnswer.person.equals(accusation.person) 
				 && theAnswer.person.equals(accusation.weapon)
				 && theAnswer.person.equals(accusation.room);
	 }
	 
	 public Card handleSuggestion(Solution suggestion, Player accuser) {
		 int index = players.indexOf(accuser);
		 for (int i = 1; i < players.size(); i++) {
			 Player player = players.get((index + i) % players.size());
			 Card result = player.disproveSuggestion(suggestion);
			 
			 if (result != null) {
				 return result;
			 }
		 }
		 return null;
	 }
	 
	 

}
