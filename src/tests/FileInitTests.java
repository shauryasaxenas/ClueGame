package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

class FileInitTests {
	// Constants that I will use to test whether the file was loaded correctly
		public static final int LEGEND_SIZE = 11;
		public static final int NUM_ROWS = 21;
		public static final int NUM_COLUMNS = 25;

		// NOTE: I made Board static because I only want to set it up one
		// time (using @BeforeAll), no need to do setup before each test.
		private static Board board;

		@BeforeAll
		public static void setUp() throws BadConfigFormatException {
			// Board is singleton, get the only instance
			board = Board.getInstance();
			// set the file names to use my config files
			board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
			// Initialize will load BOTH config files
			board.initialize();
		}

		@Test
		public void testRoomLabels() {
			assertEquals("Death Star Control Room", board.getRoom('D').getName() );
			assertEquals("Jedi Council Chamber", board.getRoom('J').getName() );
			assertEquals("Mos Eisley Cantina", board.getRoom('M').getName() );
			assertEquals("Sith Temple", board.getRoom('S').getName() );
			assertEquals("Walkway", board.getRoom('W').getName() );
		}
		
		@Test
		public void testBoardDimensions() {
			assertEquals(NUM_ROWS, board.getNumRows());
			assertEquals(NUM_COLUMNS, board.getNumColumns());
		}
		
		@Test
		public void testCellInitials() {
			BoardCell cell1 = board.getCell(0, 0);
			assertEquals('D', cell1.getInitial());
			
			BoardCell cell2 = board.getCell(12, 14);
			assertEquals('W', cell2.getInitial());
		}
		
		@Test
		public void testRoomMapSize() {
			assertEquals(LEGEND_SIZE, board.getRoomMap().size());
		}
		
		@Test
		public void testDoorDirections() {
			BoardCell doorCell1 = board.getCell(4, 9);
			assertEquals(DoorDirection.UP, doorCell1.getDoorDirection());
			
			BoardCell doorCell2 = board.getCell(3, 5);
			assertEquals(DoorDirection.RIGHT, doorCell2.getDoorDirection());
			
			BoardCell doorCell3 = board.getCell(0, 0);
			assertEquals(DoorDirection.NONE, doorCell3.getDoorDirection());
		}
		
		@Test
		public void testBadLayoutFile() {
			board.setConfigFiles("badLayout.csv","data/ClueSetup.txt");
			assertThrows(BadConfigFormatException.class, () -> {
				board.initialize();
			});
		}
		
		@Test
		public void testGetCell() {
			BoardCell cell = board.getCell(3, 3);
			assertNotNull(cell);
			assertEquals(3, cell.getRow());
			assertEquals(3, cell.getColumn());
		}
		
		@Test
		public void testSingleton() {
			Board board2 = Board.getInstance();
			assertSame(board, board2);
		}
	}
