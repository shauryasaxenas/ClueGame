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

    public Solution(Card person, Card weapon, Card room) {
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }
}
