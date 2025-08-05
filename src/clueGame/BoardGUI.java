package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Set<BoardCell> roomCentersDrawn = new HashSet<>();

        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                char initial = cell.getInitial();

                // Draw cell background based on type
                if (initial == 'W') { // Walkway
                    g.setColor(WALKWAY_COLOR);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else if (initial == 'X') { // Unused
                    g.setColor(UNUSED_COLOR);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else { // Room
                    g.setColor(ROOM_COLORS.getOrDefault(initial, Color.GRAY));
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Draw doors if cell is a doorway
                if (cell.isDoorway()) {
                    g.setColor(Color.CYAN);  // Door color - change as needed
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

                    // Calculate width and height for centering
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(roomName);
                    int textHeight = fm.getHeight();

                    // Draw string centered within the cell (or adjusted)
                    int textX = x + (CELL_SIZE - textWidth) / 2;
                    int textY = y + (CELL_SIZE + textHeight) / 2 - fm.getDescent();

                    g.drawString(roomName, textX, textY);
                }
            }
        }

        // Draw players at their current locations with their assigned colors
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
}
