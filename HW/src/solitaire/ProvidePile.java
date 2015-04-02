package solitaire;

//import solitaire.Card;
import solitaire.Solitaire;

//generic class for piles, providing cards to be put on the table or suit piles
public class ProvidePile extends CardPile{

	ProvidePile(int xl, int yl) {
		super(xl, yl);
	}
	public boolean selected = false;
	public void toggleSelect() {
		if (selected) {
			Solitaire.sender = null;
		}
		else {
			Solitaire.sender = this;
		}
		selected = !selected;
		top().selected = !top().selected;
//		System.out.println("toggle! "+top().getRank()+" "+top().selected);
	}
	
//	public void select(final int tx, final int ty) {
//		if (!empty())
//			toggleSelect();
//	}
	
	
	public void select2(final int tx, final int ty) {
			if (empty()) {
				return;
			}
//			Card topCard = pop();
			CardPile[][] cardPiles = {Solitaire.suitPile, Solitaire.tableau};
			for (CardPile[] piles: cardPiles)
				for (CardPile pile : piles) {
//					if (pile.canTake(topCard)) {
//						pile.addCard(topCard);
					if (sendCard(pile)) {
						return;
					}
				}
//			addCard(topCard);
		}
}
