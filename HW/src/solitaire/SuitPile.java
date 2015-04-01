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
	public void select(final int tx, final int ty) {
		if (Solitaire.sender!=null) {
			ProvidePile sender = Solitaire.sender;
//			Solitaire.sender.sendCard(this);
			Solitaire.sender.toggleSelect();
			sender.sendCard(this);
			return;
		}
	}
}