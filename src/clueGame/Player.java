/**
 * Class: Player
 * Abstract base class for all players in the Clue game, including common attributes and methods.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 1, 2025
 */


package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private String name;
    private Color color;
    private int row;
    private int column;
    private List<Card> hand;

    public Player(String name, Color color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setLocation(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return name + " @ (" + row + ", " + column + ")";
    }
}
