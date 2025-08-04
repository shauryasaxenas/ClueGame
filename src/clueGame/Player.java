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
import java.util.Random;
import java.util.Set;

import clueGame.*;

public abstract class Player {
    private String name;
    private String color;
    private int row;
    private int column;
    private List<Card> hand;
    private static final Random rand = new Random();

    public Player(String name, String color, int row, int column) {
        this.name = name;
        this.color = color;
        this.row = row;
        this.column = column;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card c) {
        if (c != null) {
            hand.add(c);
        } else {
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
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
    
    public Card disproveSuggestion(Solution suggestion) {
        List<Card> matchingCards = new ArrayList<>();

        for (Card card : hand) {
            if (card.equals(suggestion.getPerson()) ||
                card.equals(suggestion.getWeapon()) ||
                card.equals(suggestion.getRoom())) {
                matchingCards.add(card);
            }
        }

        if (matchingCards.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            return matchingCards.get(rand.nextInt(matchingCards.size()));
        }
    }

	public BoardCell selectTarget(Set<BoardCell> targets) {
		return null;
	}
	
	public Color getColorObject() {
	    switch (color.toLowerCase()) {
	        case "red": return Color.RED;
	        case "blue": return Color.BLUE;
	        case "green": return Color.GREEN;
	        case "yellow": return Color.YELLOW;
	        case "black": return Color.BLACK;
	        case "white": return Color.WHITE;
	        case "orange": return Color.ORANGE;
	        case "pink": return Color.PINK;
	        case "gray": return Color.GRAY;
	        default: throw new IllegalArgumentException("Unknown color: " + color);
	    }
	}

	
	





}
