package clueGame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;

/**
 * GameControlSideBar.java
 * 
 * This class represents the sidebar panel in the Clue game GUI that displays
 * the known cards categorized by People, Rooms, and Weapons.
 * 
 * Each category shows two sub-panels:
 * - Cards currently in the player's hand
 * - Cards seen and who showed them (with color coding for players)
 * 
 * The panel updates dynamically based on game state to reflect knowledge
 * about cards.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 4, 2025
 */

public class GameControlSideBar extends JPanel {

    private JPanel peoplePanel;
    private JPanel roomsPanel;
    private JPanel weaponsPanel;

    private Font boldFont = new Font("SansSerif", Font.BOLD, 12);

    public GameControlSideBar() {
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Known Cards"));
        setPreferredSize(new Dimension(250,600));

        peoplePanel = buildCardPanel("People");
        roomsPanel = buildCardPanel("Rooms");
        weaponsPanel = buildCardPanel("Weapons");

        add(peoplePanel);
        add(roomsPanel);
        add(weaponsPanel);
    }

    private JPanel buildCardPanel(String title) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(boldFont);
        cardPanel.setBorder(border);
        return cardPanel;
    }

    public void updatePanels(
        List<String> inHandPeople, Map<String, String> seenPeopleBy,
        List<String> inHandRooms, Map<String, String> seenRoomsBy,
        List<String> inHandWeapons, Map<String, String> seenWeaponsBy
    ) {
        peoplePanel.removeAll();
        peoplePanel.add(buildSubPanel("In Hand:", inHandPeople, null));
        peoplePanel.add(buildSubPanel("Seen:", null, seenPeopleBy));

        roomsPanel.removeAll();
        roomsPanel.add(buildSubPanel("In Hand:", inHandRooms, null));
        roomsPanel.add(buildSubPanel("Seen:", null, seenRoomsBy));

        weaponsPanel.removeAll();
        weaponsPanel.add(buildSubPanel("In Hand:", inHandWeapons, null));
        weaponsPanel.add(buildSubPanel("Seen:", null, seenWeaponsBy));

        revalidate();
        repaint();
    }

    private JPanel buildSubPanel(String labelText, List<String> cards, Map<String, String> coloredCards) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(boldFont);
        panel.add(label);

        if (cards != null) {
            for (int i = 0; i < cards.size(); i++) {
                String card = cards.get(i);
                JTextField cardField = new JTextField(card);
                cardField.setEditable(false);
                cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardField.getPreferredSize().height));
                panel.add(cardField);
            }
        }

        if (coloredCards != null) {
            Iterator<Map.Entry<String, String>> it = coloredCards.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String card = entry.getKey();
                String player = entry.getValue();

                JTextField cardField = new JTextField(card);
                cardField.setEditable(false);
                cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardField.getPreferredSize().height));
                cardField.setBackground(getColorForPlayer(player));

                panel.add(cardField);
            }
        }

        return panel;
    }

    private Color getColorForPlayer(String name) {
        if (name == null) return null;

        name = name.toLowerCase();

        if (name.contains("luke")) return Color.BLUE;
        if (name.contains("vader")) return Color.BLACK;
        if (name.contains("han")) return Color.GREEN;
        if (name.contains("leia")) return Color.PINK;
        if (name.contains("obi")) return Color.GRAY;
        if (name.contains("yoda")) return Color.LIGHT_GRAY;

        return null;
    }

    // === MAIN FOR TESTING ===
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Control SideBar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 600);
        frame.setLocationRelativeTo(null);

        GameControlSideBar sideBar = new GameControlSideBar();

        // Sample "in hand" cards
        List<String> inHandPeople = Arrays.asList("Luke Skywalker", "Han Solo");
        List<String> inHandRooms = Arrays.asList("Jedi Council Chamber");
        List<String> inHandWeapons = Arrays.asList("Lightsaber");

        // Sample "seen" cards with who showed them
        Map<String, String> seenPeopleBy = new HashMap<String, String>();
        seenPeopleBy.put("Yoda", "Princess Leia");
        seenPeopleBy.put("Darth Vader", "Luke Skywalker");

        Map<String, String> seenRoomsBy = new HashMap<String, String>();
        seenRoomsBy.put("Trash Compactor", "Han Solo");
        seenRoomsBy.put("Sith Temple", "Yoda");

        Map<String, String> seenWeaponsBy = new HashMap<String, String>();
        seenWeaponsBy.put("Thermal Detonator", "Obi-Wan Kenobi");
        seenWeaponsBy.put("Bowcaster", "Princess Leia");

        sideBar.updatePanels(inHandPeople, seenPeopleBy, inHandRooms, seenRoomsBy, inHandWeapons, seenWeaponsBy);

        frame.setContentPane(sideBar);
        frame.setVisible(true);
    }
}
