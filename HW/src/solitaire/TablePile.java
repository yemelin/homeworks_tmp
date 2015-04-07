package solitaire;

import java.awt.Graphics;

import solitaire.Card;

class TablePile extends ProvidePile {
	
	private final static int VSHIFT = 35;//size of the seen part of non-top cards
	
	public TablePile(final int x, final int y, final int c) {
		// initialize the parent class
		super(x, y);
		// then initialize our pile of cards
		for (int i = 0; i < c; i++) {
			push(Solitaire.deckPile.pop());
		}
		// flip topmost card face up
		top().flip();
	}
	
//	this constructor is used to initialize msg pile as an empty pile
	public TablePile(final int x, final int y) {
		super(x, y);
	}
	
//	get clicked card's number in the pile 
	private int getCardNByY(final int ty) {
		int topCardUpperY = y+pileHeightY()-Card.height;
		if (topCardUpperY<=ty && ty<=topCardUpperY+Card.height)
			return 0;
		else 
			return (topCardUpperY-ty)/VSHIFT+1;
	}
		
//	number of flipped card in the pile. Using private variable instead
//	would be clearly better, but handling its update is a bit complicated
	int getFlipped() {
		int i=0;
		Card card=top();
		while (card!=null && card.isFaceUp()) {
			i++;
			card = card.next();
		}
		return i;
	}

//	size of the displayed TablePile in pixels
	private int pileHeightY() {
		return VSHIFT*(size()-1) + Card.height;
	}

	@Override
//	original meaning changed. Tests if the pile can take the whole linked
//	list topped with the Card. Therefore the last link should be to null,
//	and the whole list should be popped
	public boolean canTake(final Card aCard) {
		Card card = aCard;
		while (card.next()!=null)
			card = card.next();		
		if (isEmpty()) {
			return card.isKing();
		}
		Card topCard = top();
		return (card.color() != topCard.color())
				&& (card.getRank() == topCard.getRank() - 1);
	}

// updated includes(). Tests the bottom of the pile and the upper bound
//	of the flipped cards
	public boolean includes(final int tx, final int ty) {		
		int pileLowY = y+pileHeightY();
		int flipped = getFlipped();
		if (flipped == 0 ) flipped++;//if top card is face down
		return x <= tx && tx <= x + Card.width && 
			pileLowY-Card.height-(flipped-1)*VSHIFT <= ty 
			&& ty<=pileLowY;//if ty is within topCard's y
	}

	
	public void select(final int tx, final int ty) {
		nselect = getCardNByY(ty);
		if (!Solitaire.msg.isEmpty()) {
			addMsg();
		}
		else {
			if (!isEmpty()) {
				if (!top().isFaceUp())
					top().flip();
				else {
					popMsg(x,y+VSHIFT*(size()-1-nselect));
				}
			}
		}
	}

	public void display(final Graphics g) {
		stackDisplay(g, top());
	}

	private int stackDisplay(final Graphics g, final Card aCard) {
		int localy;
		if (aCard == null) {
			return y;
		}
		localy = stackDisplay(g, aCard.next());
		aCard.draw(g, x, localy);
		return localy + VSHIFT;
	}

}