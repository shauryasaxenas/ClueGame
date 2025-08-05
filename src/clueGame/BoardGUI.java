package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardGUI extends JPanel {

    private static final int CELL_SIZE = 30;

    private Board board;

    public BoardGUI() {
        board = Board.getInstance();
        setPreferredSize(new Dimension(board.getNumColumns() * CELL_SIZE, board.getNumRows() * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw each cell
        for (int row = 0; row < board.getNumRows(); row++) {
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);

                if (cell.isRoomCenter()) {
                    g.setColor(Color.LIGHT_GRAY);
                } else if (cell.getInitial() == 'W') {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.GRAY);
                }

                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (cell.isDoorway()) {
                    g.setColor(Color.BLUE);
                    int doorSize = CELL_SIZE / 2;
                    int x = col * CELL_SIZE + (CELL_SIZE - doorSize) / 2;
                    int y = row * CELL_SIZE + (CELL_SIZE - doorSize) / 2;
                    g.fillRect(x, y, doorSize, doorSize);
                }

                g.setColor(Color.BLACK);
                g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Draw players
        List<Player> players = board.getPlayers();
        for (Player player : players) {
            int x = player.getColumn() * CELL_SIZE + 5;
            int y = player.getRow() * CELL_SIZE + 5;

            g.setColor(getColorFromString(player.getColor()));
            g.fillOval(x, y, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    private Color getColorFromString(String colorName) {
        if (colorName == null) return Color.GRAY;
        switch (colorName.toLowerCase()) {
            case "blue": return Color.BLUE;
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "black": return Color.BLACK;
            case "white": return Color.WHITE;
            default: return Color.GRAY;
        }
    }
}
