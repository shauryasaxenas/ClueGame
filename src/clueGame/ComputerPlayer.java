/**
 * Class: ComputerPlayer
 * Represents an AI-controlled player in the Clue game, with logic for automated suggestions.
 * 
 * Authors: Shaurya Saxena, Logan Matthews
 * Date: August 1, 2025
 */


package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.List;


public class ComputerPlayer extends Player {
	private List<Card> seenCards = new ArrayList<>();
	private boolean readyToAccuse = false;
	private Solution storedAccusation = null;
	


    public ComputerPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
    }

    public void addSeenCard(Card c) {
        if (c != null && !seenCards.contains(c)) {
            seenCards.add(c);
        }
    }

    public List<Card> getSeenCards() {
        return new ArrayList<>(seenCards);
    }
    
    public void clearSeenCards() {
        seenCards.clear();
    }

    public Solution createSuggestion(Board board) {
        // Get room initial at player's location
        BoardCell currentCell = board.getCell(getRow(), getColumn());
        char roomInitial = currentCell.getInitial();
        Room currentRoom = board.getRoom(roomInitial);

        // The room card to suggest
        Card roomCard = null;
        for (Card c : board.getDeck()) {
            if (c.getName().equals(currentRoom.getName()) && c.getType() == CardType.ROOM) {
                roomCard = c;
                break;
            }
        }

        // Get unseen persons and weapons
        List<Card> unseenPersons = new ArrayList<>();
        List<Card> unseenWeapons = new ArrayList<>();

        for (Card c : board.getDeck()) {
            if (!seenCards.contains(c)) {
                if (c.getType() == CardType.PERSON) unseenPersons.add(c);
                if (c.getType() == CardType.WEAPON) unseenWeapons.add(c);
            }
        }

        if (unseenPersons.isEmpty()) {
            for (Card c : board.getDeck()) {
                if (c.getType() == CardType.PERSON) unseenPersons.add(c);
            }
        }
        if (unseenWeapons.isEmpty()) {
            for (Card c : board.getDeck()) {
                if (c.getType() == CardType.WEAPON) unseenWeapons.add(c);
            }
        }

        // Randomly pick one from each list
        Random rand = new Random();
        Card personCard = unseenPersons.get(rand.nextInt(unseenPersons.size()));
        Card weaponCard = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));

        return new Solution(personCard, roomCard, weaponCard);
    }
    
    @Override
    public BoardCell selectTarget(Set<BoardCell> targets) {
        Random rand = new Random();
        List<BoardCell> unseenRooms = new ArrayList<>();

        for (BoardCell cell : targets) {
            Room room = Board.getInstance().getRoom(cell.getInitial());
            if (room != null && room.getName() != null) {
                // check if we've seen the corresponding room card
                boolean seen = false;
                for (Card seenCard : seenCards) {
                    if (seenCard.getType() == CardType.ROOM && seenCard.getName().equals(room.getName())) {
                        seen = true;
                        break;
                    }
                }
                if (!seen) {
                    unseenRooms.add(cell);
                }
            }
        }

        if (!unseenRooms.isEmpty()) {
            return unseenRooms.get(rand.nextInt(unseenRooms.size()));
        }

        // No unseen room? Pick randomly
        List<BoardCell> targetList = new ArrayList<>(targets);
        return targetList.get(rand.nextInt(targetList.size()));
    }
    
    public void setReadyToAccuse(boolean ready) {
        this.readyToAccuse = ready;
    }

    public boolean isReadyToAccuse() {
        return readyToAccuse;
    }

    public void setStoredAccusation(Solution accusation) {
        this.storedAccusation = accusation;
    }

    public Solution getStoredAccusation() {
        return storedAccusation;
    }
}
