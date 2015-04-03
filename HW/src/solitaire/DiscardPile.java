package solitaire;

class DiscardPile extends ProvidePile {

	DiscardPile(final int x, final int y) {
		super(x, y);
	}
	{
		nselect = 0;
	}
	public void addCard(final Card aCard) {
		if (!aCard.isFaceUp()) {
			aCard.flip();
		}
		super.addCard(aCard);
	}
	public void select (final int x, final int y) {
		if (Solitaire.sender!=null) {
			Solitaire.sender.toggleSelect();
		}
		else if (!empty())
			toggleSelect();
	}
}