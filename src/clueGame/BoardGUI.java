package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;


/**
 * BoardGUI.java
 * 
 * This class handles the graphical representation of the Clue board.
 * It draws cells, rooms, walkways, unused areas, doorways, room names, 
 * and player pieces. The design incorporates Star Wars–themed color coding.
 * 
 * The board is rendered dynamically based on the current game state.
 * 
 * Authors: Shaurya Saxena, Logan Matthews  
 * Date: August 5, 2025
 */

public class BoardGUI extends JPanel {

    private static final int CELL_SIZE = 30;
    private Board board;

    // Star Wars–themed room colors
    private static final Map<Character, Color> ROOM_COLORS = Map.of(
        'D', new Color(0x3a3a3a),    // Death Star Control Room
        'J', new Color(0x87ceeb),    // Jedi Council Chamber
        'M', new Color(0xd2b48c),    // Mos Eisley Cantina
        'S', new Color(0x8b0000),    // Sith Temple
        'F', new Color(0xa9a9a9),    // Millennium Falcon Cockpit
        'C', new Color(0x5f9ea0),    // Carbon Freezing Chamber
        'T', new Color(0x556b2f),    // Trash Compactor
        'I', new Color(0x2f4f4f),    // Imperial Hangar Bay
        'H', new Color(0x6b8e23)     // Dagobah Swamp Hut
    );

    private static final Color WALKWAY_COLOR = new Color(0xf4e842); // Rebel Yellow
    private static final Color UNUSED_COLOR = Color.BLACK;

    public BoardGUI(Board board) {
        this.board = board;
        
        setPreferredSize(new Dimension(board.getNumColumns() * CELL_SIZE, board.getNumRows() * CELL_SIZE));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
                
                
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Set<BoardCell> roomCentersDrawn = new HashSet<>();

        // Get the current target cells from Board
        Set<BoardCell> targets = board.getTargets();

        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                char initial = cell.getInitial();

                // If cell is a target, highlight it first
                if (targets != null && targets.contains(cell)) {
                    g.setColor(Color.RED);  // Highlight color for targets
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } 
                else if (initial == 'W') { // Walkway
                    g.setColor(WALKWAY_COLOR);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } 
                else if (initial == 'X') { // Unused
                    g.setColor(UNUSED_COLOR);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } 
                else { // Room
                    g.setColor(ROOM_COLORS.getOrDefault(initial, Color.GRAY));
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Draw doors if cell is a doorway
                if (cell.isDoorway()) {
                    g.setColor(Color.CYAN);  // Door color
                    int doorThickness = 6;
                    switch (cell.getDoorDirection()) {
                        case UP:
                            g.fillRect(x, y, CELL_SIZE, doorThickness);
                            break;
                        case DOWN:
                            g.fillRect(x, y + CELL_SIZE - doorThickness, CELL_SIZE, doorThickness);
                            break;
                        case LEFT:
                            g.fillRect(x, y, doorThickness, CELL_SIZE);
                            break;
                        case RIGHT:
                            g.fillRect(x + CELL_SIZE - doorThickness, y, doorThickness, CELL_SIZE);
                            break;
                        default:
                            break;
                    }
                }

                // Draw room name at room center cells, only once per room
                if (cell.isRoomCenter() && !roomCentersDrawn.contains(cell)) {
                    roomCentersDrawn.add(cell);
                    String roomName = board.getRoom(initial).getName();
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("SansSerif", Font.BOLD, 14));

                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(roomName);
                    int textHeight = fm.getHeight();

                    int textX = x + (CELL_SIZE - textWidth) / 2;
                    int textY = y + (CELL_SIZE + textHeight) / 2 - fm.getDescent();

                    g.drawString(roomName, textX, textY);
                }
            }
        }

        // Draw players last to ensure they appear on top
        for (Player player : board.getPlayers()) {
            int px = player.getColumn() * CELL_SIZE + 5;
            int py = player.getRow() * CELL_SIZE + 5;
            Color playerColor = player.getColorObject();

            g.setColor(playerColor);
            g.fillOval(px, py, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }


    public static void main(String[] args) {
        Board board = Board.getInstance();
        board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
        board.initialize();

        JFrame frame = new JFrame("Clue Board - Star Wars Edition");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new BoardGUI(board));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
    
    public void handleBoardClick(int mouseX, int mouseY) {
    	GameControlPanel controlPanel = new GameControlPanel();
    	 if (!board.isHumanMustFinish()) return;
        // 1) Check if human must finish turn (ask Board)
        if (!board.isHumanMustFinish()) {
            return;  // Not human turn or already moved
        }

        int col = mouseX / CELL_SIZE;
        int row = mouseY / CELL_SIZE;

        if (row < 0 || row >= board.getNumRows() || col < 0 || col >= board.getNumColumns()) {
            return;  // Outside board
        }

        BoardCell clickedCell = board.getCell(row, col);

        // 2) Check if clicked cell is in targets (ask Board)
        if (board.isTarget(clickedCell)) {
            // 3) Move player (ask Board)
            board.moveCurrentPlayerTo(clickedCell);

            // 4) Clear highlights and targets
            board.clearHighlights();

            // 5) Mark human turn finished
            board.setHumanMustFinish(false);

            // 6) Update control panel (you might need a reference to controlPanel here)
            controlPanel.setTurn(board.getCurrentPlayer().getName() + "'s turn");
            controlPanel.setRoll(0);
            controlPanel.setGuess("");
            controlPanel.setGuessResult("");

            repaint();

         // After moving the human player:
            if (clickedCell.isRoomCenter()) {
                // Get the room name for the clicked cell
                String currentRoom = board.getRoom(clickedCell.getInitial()).getName();

                // Find the owning Frame for the dialog
                Frame owner = JOptionPane.getFrameForComponent(this);

                // Create the dialog with the SINGLE room name (not a list)
                SuggestionDialog suggestionDialog = new SuggestionDialog(
                    owner,
                    currentRoom,
                    board.getPeopleNames(),
                    board.getWeaponNames()
                );

                suggestionDialog.setVisible(true);

                if (suggestionDialog.isSubmitted()) {
                    Solution suggestion = board.createSolutionFromStrings(
                        suggestionDialog.getSelectedPerson(),
                        suggestionDialog.getSelectedWeapon(),
                        suggestionDialog.getSelectedRoom()   // returns the fixed room name
                    );
                    if (suggestion != null) {
                        board.handleSuggestion(board.getCurrentPlayer(), suggestion);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error creating suggestion.");
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(this,
                "That is not a valid location. Please pick a highlighted cell.");
        }
    }  
}
