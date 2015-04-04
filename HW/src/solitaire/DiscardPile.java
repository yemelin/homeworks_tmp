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

	public void select (final int tx, final int ty) {
		if (Solitaire.sender!=null) {
			super.select(tx, ty);
		}
		else if (!empty()) {
			Solitaire.sender = this;
		/*	Solitaire.sender.*/selectCards(nselect+1, true);
		}
	}
}