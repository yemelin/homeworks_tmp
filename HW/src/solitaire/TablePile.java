package solitaire;

import java.awt.Graphics;
import solitaire.Card;

class TablePile extends ProvidePile {

	public int nselect;

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
	private int getCardNByY(final int ty) {
		int topCardUpperY = y+pileHeightY()-Card.height;
		if (topCardUpperY<=ty && ty<=topCardUpperY+Card.height)
			return 0;
		else
			return (topCardUpperY-ty)/VSHIFT+1;
	}
	
	@Override
	public boolean sendCard(CardPile pile) {
		Card card = top();
		Card scard = getCard(nselect);
		System.out.println("nselect = "+nselect+" "+top().getRank());		
		if (!pile.canTake(getCard(nselect)))
			return false;

		if	(!pile.addCards( getCard(nselect), pop(nselect) ))
//			System.out.println(pop());
			addCards(scard, card);
		return true;
	}
	
	private int getFlipped() {
		int i=0;
		Card card=top();
		while (card!=null && card.isFaceUp()) {
			i++;
			card = card.link;
		}
		return i;
	}
	
	public boolean includes(final int tx, final int ty) {		
		int pileLowY = y+pileHeightY();
		int flipped = getFlipped();
		if (flipped == 0 ) flipped++;//if top card is face down
		return x <= tx && tx <= x + Card.width && 
			pileLowY-Card.height-flipped*VSHIFT <= ty 
			&& ty<=pileLowY;//if ty is within topCard's y
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
				else {
					nselect = getCardNByY(ty);
					toggleSelect();
				}
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