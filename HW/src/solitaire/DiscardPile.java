package solitaire;

class DiscardPile extends ProvidePile {

	DiscardPile(final int x, final int y) {
		super(x, y);
	}

	public void addCard(final Card aCard) {
		if (!aCard.isFaceUp()) {
			aCard.flip();
		}
		super.addCard(aCard);
	}
}