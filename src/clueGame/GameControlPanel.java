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


        // Column 3: JButton
        JButton accuseButton = new JButton("Make Accusation");

        // Column 4: JButton
        JButton nextButton = new JButton("Next Player");

        // Add to top panel
        topPanel.add(leftPanel1);
        topPanel.add(leftPanel2);
        topPanel.add(accuseButton);
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
        accuseButton.setFont(boldFont);
        nextButton.setFont(boldFont);
        
        // Add top and bottom panels to main panel
        add(topPanel);
        add(bottomPanel);
    }
    
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
