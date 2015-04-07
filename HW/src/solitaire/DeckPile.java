package solitaire;

import solitaire.Solitaire;

class DeckPile extends CardPile {

	public DeckPile(final int x, final int y) {
		// first initialize parent
		super(x, y);
		// then create the new deck
		// first put them into a local pile
		CardPile pileOne = new CardPile(0, 0);
		CardPile pileTwo = new CardPile(0, 0);
		int count = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j <= 12; j++) {
				pileOne.push(new Card(i, j));
				count++;
			}
		}
		// then pull them out randomly
		for (; count > 0; count--) {
			int limit = ((int) (Math.random() * 1000)) % count;
			// move down to a random location
			for (int i = 0; i < limit; i++) {
				pileTwo.push(pileOne.pop());
			}
			// then add the card found there
			push(pileOne.pop());
			// then put the decks back together
			while (!pileTwo.isEmpty()) {
				pileOne.push(pileTwo.pop());
			}
		}
	}

	public void select(final int tx, final int ty) {
		if (/*Solitaire.sender!=null*/!Solitaire.msg.isEmpty()) {
//			super.select(tx, ty);
			Solitaire.getSender().addMsg();
		}
		else if (isEmpty()) {
				while (!Solitaire.discardPile.isEmpty()) {
					push(Solitaire.discardPile.pop());
					top().flip();
				}
			}
		else
			Solitaire.discardPile.push(pop());
	}
}