package clueGame;

public class Card {
	private String cardName;
	private CardType type;
	
	public Card(String name, CardType type) {
		this.cardName = name;
		this.type = type;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType() {
		return type;
	}
	
	public boolean equals(Card target) {	
		if (target == null) {
			return false;
		}
		return this.cardName.equals(target.cardName) && this.type == target.type;
	}

	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", type=" + type + "]";
	}
}
