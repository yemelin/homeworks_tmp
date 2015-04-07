package solitaire;

class DiscardPile extends ProvidePile {

	DiscardPile(final int x, final int y) {
		super(x, y);
	}
	
	@Override
	public void push(final Card aCard) {
		if (!aCard.isFaceUp()) {
			aCard.flip();
		}
		super.push(aCard);
	}

	@Override
	public void select (final int tx, final int ty) {
		if (!Solitaire.msg.isEmpty()) {
			Solitaire.getSender().addMsg();
		}
		else if (!isEmpty()) {
			popMsg(x,y);
		}
	}
}