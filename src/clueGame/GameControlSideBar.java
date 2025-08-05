package clueGame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameControlSideBar extends JPanel {

    private JPanel peoplePanel;
    private JPanel roomsPanel;
    private JPanel weaponsPanel;

    // For demo, simple text areas or lists can go inside each panel
    private JTextArea peopleArea;
    private JTextArea roomsArea;
    private JTextArea weaponsArea;

    public GameControlSideBar() {
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Known Cards"));

        Font boldFont = new Font("SansSerif", Font.BOLD, 12);

        // People Panel
        peoplePanel = new JPanel(new GridLayout(4, 1));  // 4 rows, 1 column
        TitledBorder peopleBorder = BorderFactory.createTitledBorder("People");
        peopleBorder.setTitleFont(boldFont);
        peoplePanel.setBorder(peopleBorder);

        JLabel label1 = new JLabel("In Hand:");
        JTextField peopleInHandTextField = new JTextField();
        peopleInHandTextField.setEditable(false);

        JLabel label2 = new JLabel("Seen:");
        JTextField peopleSeenTextField = new JTextField();
        peopleSeenTextField.setEditable(false);

        peoplePanel.add(label1);
        peoplePanel.add(peopleInHandTextField);
        peoplePanel.add(label2);
        peoplePanel.add(peopleSeenTextField);

        // Rooms panel
        roomsPanel = new JPanel(new BorderLayout());
        TitledBorder roomsBorder = BorderFactory.createTitledBorder("Rooms");
        roomsBorder.setTitleFont(boldFont);
        roomsPanel.setBorder(roomsBorder);
        roomsArea = new JTextArea();
        roomsArea.setEditable(false);
        roomsPanel.add(new JScrollPane(roomsArea), BorderLayout.CENTER);

        // Weapons panel
        weaponsPanel = new JPanel(new BorderLayout());
        TitledBorder weaponsBorder = BorderFactory.createTitledBorder("Weapons");
        weaponsBorder.setTitleFont(boldFont);
        weaponsPanel.setBorder(weaponsBorder);
        weaponsArea = new JTextArea();
        weaponsArea.setEditable(false);
        weaponsPanel.add(new JScrollPane(weaponsArea), BorderLayout.CENTER);

        // Add panels to main sidebar panel
        add(peoplePanel);
        add(roomsPanel);
        add(weaponsPanel);
    }

    // Example setters to update text
    public void setPeopleText(String text) {
        peopleArea.setText(text);
    }

    public void setRoomsText(String text) {
        roomsArea.setText(text);
    }

    public void setWeaponsText(String text) {
        weaponsArea.setText(text);
    }


    // Main to test the panel
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Control SideBar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameControlSideBar panel = new GameControlSideBar();
        frame.setContentPane(panel);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
