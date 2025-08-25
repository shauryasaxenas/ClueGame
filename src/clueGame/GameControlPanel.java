package clueGame;

import javax.swing.*;
import java.awt.*;

/**
 * GameControlPanel.java
 * 
 * This class defines the GUI control panel for the Clue game, which includes:
 * - Displaying the current player's turn
 * - Showing the die roll
 * - Displaying the current guess and guess result
 * - Buttons for making an accusation and moving to the next player
 * 
 * The panel is divided into a top section (with player info and controls)
 * and a bottom section (with guess and result display).
 * 
 * Authors: Shaurya Saxena, Logan Matthews  
 * Date: August 4, 2025
 */

public class GameControlPanel extends JPanel {

    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextField currentTurnField;
    private JTextField dieRollField;
    private JTextField guessField;
    private JTextField resultField;
    private JButton accusationButton;  // <-- class field

    public GameControlPanel() {
        Font boldFont = new Font("SansSerif", Font.BOLD, 12);
        setLayout(new GridLayout(2, 1)); // Top half + bottom half

        // === TOP PANEL ===
        topPanel = new JPanel(new GridLayout(1, 4)); // 1 row, 4 columns

        // Column 1: First JPanel
        JPanel leftPanel1 = new JPanel(new GridLayout(3,1));
        
        // Top: JLabel
        JLabel turnLabel = new JLabel("Whose turn?");
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Middle
        currentTurnField = new JTextField();
        currentTurnField.setEditable(false);
        currentTurnField.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Bottom
        JPanel emptyPanel1 = new JPanel();
        
        // Add to leftPanel1
        leftPanel1.add(turnLabel);
        leftPanel1.add(currentTurnField);
        leftPanel1.add(emptyPanel1);
        
        // ----- Column 2: leftPanel2 -----
        JPanel leftPanel2 = new JPanel(new GridBagLayout()); // Centers content

        // Panel containing "Roll:" + text field side-by-side
        JPanel rollSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel rollLabel = new JLabel("Roll:");
        dieRollField = new JTextField(5);
        dieRollField.setEditable(false);

        rollSubPanel.add(rollLabel);
        rollSubPanel.add(dieRollField);

        // Add centered panel to leftPanel2
        leftPanel2.add(rollSubPanel);

        // Column 3: Accusation button (fixed to use field)
        accusationButton = new JButton("Make Accusation");
        accusationButton.addActionListener(e -> {
            handleAccusationButtonPressed();
        });

        // Column 4: Next Player button
        JButton nextButton = new JButton("Next Player");
        nextButton.addActionListener(e -> {Board.getInstance().handleNextTurn();});

        // Add to top panel
        topPanel.add(leftPanel1);
        topPanel.add(leftPanel2);
        topPanel.add(accusationButton);
        topPanel.add(nextButton);

        // === BOTTOM PANEL ===
        bottomPanel = new JPanel(new GridLayout(0, 2)); // 0 rows, 2 columns

        // LEFT PANEL: "Guess"
        JPanel leftGuessPanel = new JPanel(new GridLayout(1, 0)); // 1 row, flexible columns
        javax.swing.border.TitledBorder guessBorder = BorderFactory.createTitledBorder("Guess");
        guessBorder.setTitleFont(boldFont);
        leftGuessPanel.setBorder(guessBorder);
        guessField = new JTextField();
        guessField.setEditable(false);
        leftGuessPanel.add(guessField);

        // RIGHT PANEL: "Guess Result"
        JPanel rightResultPanel = new JPanel(new GridLayout(1, 0)); // 1 row, flexible columns
        javax.swing.border.TitledBorder resultBorder = BorderFactory.createTitledBorder("Guess Result");
        resultBorder.setTitleFont(boldFont);
        rightResultPanel.setBorder(resultBorder);
        resultField = new JTextField();
        resultField.setEditable(false);
        rightResultPanel.add(resultField);

        // Add to bottom panel
        bottomPanel.add(leftGuessPanel);
        bottomPanel.add(rightResultPanel);

        turnLabel.setFont(boldFont);
        rollLabel.setFont(boldFont);
        accusationButton.setFont(boldFont);
        nextButton.setFont(boldFont);
        
        // Add top and bottom panels to main panel
        add(topPanel);
        add(bottomPanel);
    }
    
    // Enable or disable the accusation button
    public void enableAccusationButton(boolean enable) {
        accusationButton.setEnabled(enable);
    }

    // Setters for UI fields
    public void setTurn(String playerName) {
        currentTurnField.setText(playerName);
    }
    
    public void setRoll(int roll) {
        dieRollField.setText(Integer.toString(roll));
    }
    
    public void setGuess(String guess) {
        guessField.setText(guess);
    }
    
    public void setGuessResult(String guessResult) {
        resultField.setText(guessResult);
    }

    // Placeholder for accusation button click handler
    private void handleAccusationButtonPressed() {
        Board board = Board.getInstance();
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);

        AccusationDialog accusationDialog = new AccusationDialog(
            owner,
            board.getRoomNames(),   // all rooms for accusation
            board.getPeopleNames(),
            board.getWeaponNames()
        );

        accusationDialog.setVisible(true);

        if (accusationDialog.isSubmitted()) {
            Solution accusation = board.createSolutionFromStrings(
                accusationDialog.getSelectedPerson(),
                accusationDialog.getSelectedWeapon(),
                accusationDialog.getSelectedRoom()
            );

            if (accusation != null) {
                boolean correct = board.checkAccusation(accusation);

                if (correct) {
                    JOptionPane.showMessageDialog(this, "Correct! You solved the mystery!");
                    // Implement game win logic here
                } else {
                	    JOptionPane.showMessageDialog(this, "Wrong accusation! You are out of the game.");
                	    Player currentPlayer = board.getCurrentPlayer();
                	    currentPlayer.setEliminated(true);

                	    // Disable the accusation button so player cannot accuse again
                	    enableAccusationButton(false);

                	    // Optionally, disable controls or mark the human turn as finished
                	    board.setHumanMustFinish(false);
                	}

                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid accusation selection.");
            }
        }
    


    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Control Panel");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameControlPanel panel = new GameControlPanel();
        frame.setContentPane(panel);

        frame.setSize(750, 180);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        panel.setTurn("Darth Vader");
        panel.setRoll(5);
        panel.setGuess("I have no guess!");
        panel.setGuessResult("So you have nothing?");
    }
}
