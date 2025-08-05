package clueGame;

import javax.swing.*;
import java.awt.*;

public class ClueGame {

    public static void main(String[] args) {
        Board board = Board.getInstance();
        board.setConfigFiles("data/ClueLayout.csv", "data/ClueSetup.txt");
        board.initialize();

        JFrame frame = new JFrame("Clue Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main board panel (center)
        BoardGUI boardGUI = new BoardGUI();

        // Sidebar on the right
        GameControlSideBar sideBar = new GameControlSideBar();

        // Control panel at the bottom
        GameControlPanel controlPanel = new GameControlPanel();

        // Example sample data for sidebar
        java.util.List<String> inHandPeople = java.util.Arrays.asList("Luke Skywalker", "Han Solo");
        java.util.List<String> inHandRooms = java.util.Arrays.asList("Jedi Council Chamber");
        java.util.List<String> inHandWeapons = java.util.Arrays.asList("Lightsaber");

        java.util.Map<String, String> seenPeopleBy = new java.util.HashMap<>();
        seenPeopleBy.put("Yoda", "Princess Leia");
        seenPeopleBy.put("Darth Vader", "Luke Skywalker");

        java.util.Map<String, String> seenRoomsBy = new java.util.HashMap<>();
        seenRoomsBy.put("Trash Compactor", "Han Solo");
        seenRoomsBy.put("Sith Temple", "Yoda");

        java.util.Map<String, String> seenWeaponsBy = new java.util.HashMap<>();
        seenWeaponsBy.put("Thermal Detonator", "Obi-Wan Kenobi");
        seenWeaponsBy.put("Bowcaster", "Princess Leia");

        sideBar.updatePanels(
            inHandPeople, seenPeopleBy,
            inHandRooms, seenRoomsBy,
            inHandWeapons, seenWeaponsBy);

        // Add components to the frame
        frame.add(boardGUI, BorderLayout.CENTER);
        frame.add(sideBar, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Example setting control panel fields
        controlPanel.setTurn("Darth Vader");
        controlPanel.setRoll(5);
        controlPanel.setGuess("I have no guess!");
        controlPanel.setGuessResult("So you have nothing?");
    }
}
