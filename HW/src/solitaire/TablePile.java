package solitaire;

import java.awt.Graphics;
import solitaire.Card;

class TablePile extends ProvidePile {

	TablePile(final int x, final int y, final int c) {
		// initialize the parent class
		super(x, y);
		// then initialize our pile of cards
		for (int i = 0; i < c; i++) {
			addCard(Solitaire.deckPile.pop());
		}
		// flip topmost card face up
		top().flip();
	}
	public final static int VSHIFT = 35;//size of the seen part of non-top cards
	
	public boolean canTake(final Card aCard) {
		if (empty()) {
			return aCard.isKing();
		}
		Card topCard = top();
		return (aCard.color() != topCard.color())
				&& (aCard.getRank() == topCard.getRank() - 1);
	}

	public void display(final Graphics g) {
		stackDisplay(g, top());
	}
//	by VVY
	private int pileHeightY() {
		return VSHIFT*(size()-1) + Card.height;
	}
//	private boolean isClickedBelow (final int ty) {
//		return ty>y+pileHeightY();
//	}
	
	public boolean includes(final int tx, final int ty) {		
		int pileLowY = y+pileHeightY();
		return x <= tx && tx <= x + Card.width && 
			pileLowY-Card.height <= ty && ty<=pileLowY;//if ty is within topCard's y
	}

	public void select(final int tx, final int ty) {
		// if face down, then flip
//		Card topCard;
//		duplicated code with suitPile -------
		if (Solitaire.sender != null) {
			ProvidePile sender = Solitaire.sender;
			Solitaire.sender.toggleSelect();
			sender.sendCard(this);			
		} 
//		----------
		else {
			if (!empty()) {
				if (!top().isFaceUp())
					top().flip();
				else toggleSelect();
			}
		}
		return;
	}

	private int stackDisplay(final Graphics g, final Card aCard) {
		int localy;
		if (aCard == null) {
			return y;
		}
		localy = stackDisplay(g, aCard.link);
		aCard.draw(g, x, localy);
		return localy + VSHIFT;
	}

}