/**
 * Class: SuggestionDialog
 * Handles suggestions GUI
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 12, 2025
 */


package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SuggestionDialog extends JDialog {
    private JComboBox<String> personDropdown;
    private JComboBox<String> weaponDropdown;
    private final String currentRoomName; // store the room player is in

    private boolean submitted = false;  // Tracks if user clicked Submit

    public SuggestionDialog(Frame owner, String currentRoomName, List<String> people, List<String> weapons) {
        super(owner, "Make a Suggestion", true);
        this.currentRoomName = currentRoomName; // assign to field

        setLayout(new GridLayout(4, 2, 5, 5));

        // Room label (fixed, not selectable)
        add(new JLabel("Room:"));
        JLabel roomLabel = new JLabel(currentRoomName);
        roomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(roomLabel);

        // Person dropdown
        add(new JLabel("Person:"));
        personDropdown = new JComboBox<>(people.toArray(new String[0]));
        add(personDropdown);

        // Weapon dropdown
        add(new JLabel("Weapon:"));
        weaponDropdown = new JComboBox<>(weapons.toArray(new String[0]));
        add(weaponDropdown);

        // Buttons
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        add(submitButton);
        add(cancelButton);

        submitButton.addActionListener(e -> {
            submitted = true;
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            submitted = false;
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String getSelectedRoom() {
        return currentRoomName; // always return the fixed room
    }

    public String getSelectedPerson() {
        return (String) personDropdown.getSelectedItem();
    }

    public String getSelectedWeapon() {
        return (String) weaponDropdown.getSelectedItem();
    }
}
