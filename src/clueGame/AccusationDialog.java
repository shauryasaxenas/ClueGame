package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccusationDialog extends JDialog {
    private JComboBox<String> roomDropdown;
    private JComboBox<String> personDropdown;
    private JComboBox<String> weaponDropdown;

    private boolean submitted = false;

    public AccusationDialog(Frame owner, List<String> rooms, List<String> people, List<String> weapons) {
        super(owner, "Make an Accusation", true);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Room:"));
        roomDropdown = new JComboBox<>(rooms.toArray(new String[0]));
        add(roomDropdown);

        add(new JLabel("Person:"));
        personDropdown = new JComboBox<>(people.toArray(new String[0]));
        add(personDropdown);

        add(new JLabel("Weapon:"));
        weaponDropdown = new JComboBox<>(weapons.toArray(new String[0]));
        add(weaponDropdown);

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
        return (String) roomDropdown.getSelectedItem();
    }

    public String getSelectedPerson() {
        return (String) personDropdown.getSelectedItem();
    }

    public String getSelectedWeapon() {
        return (String) weaponDropdown.getSelectedItem();
    }
}
