package solitaire;

class SuitPile extends CardPile {

	SuitPile(final int x, final int y) {
		super(x, y);
	}

	public boolean canTake(final Card aCard) {
		if (empty()) {
			return aCard.isAce();
		}
		Card topCard = top();
		return (aCard.getSuit() == topCard.getSuit())
				&& (aCard.getRank() == 1 + topCard.getRank());
	}
	public boolean addCards(final Card loCard, final Card topCard) {
		if (loCard!=topCard)
			return false;
		else return super.addCards(loCard, topCard);
	}
	public void select(final int tx, final int ty) {
		if (Solitaire.sender!=null) {
			ProvidePile sender = Solitaire.sender;
			Solitaire.sender.toggleSelect();
			sender.sendCard(this);
			return;
		}
	}
}