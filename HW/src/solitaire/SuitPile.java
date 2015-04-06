package solitaire;

class SuitPile extends CardPile {

	SuitPile(final int x, final int y) {
		super(x, y);
	}
	
	@Override
	public boolean isTakingFromTable() {
		return true;
	}
	
	@Override
	public boolean canTake(final Card aCard) {
		if (aCard.next()!=null)
			return false;

		if (empty()) {
			return aCard.isAce();
		}
		Card topCard = top();
		return (aCard.getSuit() == topCard.getSuit())
				&& (aCard.getRank() == 1 + topCard.getRank());
	}

	@Override
	public void select(final int tx, final int ty) {
		if (Solitaire.sender!=null) {
//			super.select(tx, ty);
			addMsg();
		}
	}
}