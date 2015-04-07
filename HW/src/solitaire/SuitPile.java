package solitaire;

class SuitPile extends CardPile {

	public SuitPile(final int x, final int y) {
		super(x, y);
	}
		
	@Override
	public boolean canTake(final Card aCard) {
		if (aCard.next()!=null) //take single cards only
			return false;

		if (isEmpty()) {
			return aCard.isAce();
		}
		Card topCard = top();
		return (aCard.getSuit() == topCard.getSuit())
				&& (aCard.getRank() == 1 + topCard.getRank());
	}

	@Override
	public void select(final int tx, final int ty) {
		if (!Solitaire.msg.isEmpty()) {
			addMsg();
		}
	}
}