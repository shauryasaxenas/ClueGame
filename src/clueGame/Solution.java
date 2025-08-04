/**
 * Class: Solution
 * Encapsulates the correct answer to the Clue game: one person, one weapon, and one room.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 1, 2025
 */


package clueGame;

public class Solution {
	public Card person;
    public Card weapon;
    public Card room;

    public Solution(Card person, Card room, Card weapon) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution other = (Solution) o;
        return java.util.Objects.equals(person, other.person) &&
               java.util.Objects.equals(room, other.room) &&
               java.util.Objects.equals(weapon, other.weapon);
    }

    public Card getPerson() {
        return person;
    }

    public Card getRoom() {
        return room;
    }

    public Card getWeapon() {
        return weapon;
    }
}
