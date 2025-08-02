package clueGame;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public abstract class Player {
	private String name;
	private Color color;
	private int row;
	private int column;
	private List<Card> hand = new ArrayList<>();
	
	public Player(String name, Color color, int row, int column) {
		super();
		this.name = name;
		this.color = color;
		this.row = row;
		this.column = column;
	}
	
	public void updateHand(Card card) {
		hand.add(card);
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
	
	public List<Card> getHand() {
		return hand;
	}
}
