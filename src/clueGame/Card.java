/**
 * Class: Card
 * Represents a Clue game card, which can be a person, room, or weapon.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 1, 2025
 */



package clueGame;

import java.util.Objects;

public class Card {
	
	
    private String name;
    private CardType type;

    public Card(String name, CardType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.name.equals(other.name) && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
