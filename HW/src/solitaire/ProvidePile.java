package solitaire;

import solitaire.Card;
import solitaire.Solitaire;

//generic class for piles, providing cards to be put on the table or suit piles
public class ProvidePile extends CardPile{

	ProvidePile(int xl, int yl) {
		super(xl, yl);
	}

	public void select(final int tx, final int ty) {
			if (empty()) {
				return;
			}
			Card topCard = pop();
			CardPile[][] cardPiles = {Solitaire.suitPile, Solitaire.tableau};
			for (CardPile[] piles: cardPiles)
				for (CardPile pile : piles) {
					if (pile.canTake(topCard)) {
						pile.addCard(topCard);
						return;
					}
				}
			addCard(topCard);
		}
}
