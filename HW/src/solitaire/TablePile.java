package solitaire;

import java.awt.Graphics;

import solitaire.Card;

class TablePile extends ProvidePile {
	
	private final static int VSHIFT = 35;//size of the seen part of non-top cards
	
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
	
//	by VVY
	TablePile(final int x, final int y) {
		super(x, y);
	}
	
	private int getCardNByY(final int ty) {

		int topCardUpperY = y+pileHeightY()-Card.height;
		if (topCardUpperY<=ty && ty<=topCardUpperY+Card.height)
			return 0;
		else {
//			System.out.println("Counting: ty="+ty+"nselect = "+nselect);
			return (topCardUpperY-ty)/VSHIFT+1;
		}
	}
		
	private int getFlipped() {
		int i=0;
		Card card=top();
		while (card!=null && card.isFaceUp()) {
			i++;
			card = card.next();
		}
		return i;
	}

	private int pileHeightY() {
		return VSHIFT*(size()-1) + Card.height;
	}

	@Override
	public boolean isTakingFromTable() {
		return true;
	}

	@Override
	public boolean canTake(final Card aCard) {
		Card card = aCard;
		while (card.next()!=null)
			card = card.next();
		
//		System.out.println("take "+card.getRank()+"?");
		if (empty()) {
			return card.isKing();
		}
		Card topCard = top();
//		System.out.println("Top is:"+ topCard.getRank()+", take "+card.getRank()+"?");
		return (card.color() != topCard.color())
				&& (card.getRank() == topCard.getRank() - 1);
	}


	public boolean includes(final int tx, final int ty) {		
		int pileLowY = y+pileHeightY();
//		System.out.println("testing "+x+", "+y);
		if (x <= tx && tx <= x + Card.width) {
//			System.out.println("Click on card "+getCardNByY(ty)+" size:"+size());
//			printStack();
		}
		int flipped = getFlipped();
		if (flipped == 0 ) flipped++;//if top card is face down
		return x <= tx && tx <= x + Card.width && 
			pileLowY-Card.height-(flipped-1)*VSHIFT <= ty 
			&& ty<=pileLowY;//if ty is within topCard's y
	}

	
	public void select(final int tx, final int ty) {
		nselect = getCardNByY(ty);
//		System.out.println("nselect = "+nselect);
		if (Solitaire.sender!=null) {
			addMsg();
//			Solitaire.msg.selectCards(false);
//			if (canTake(Solitaire.msg.top()))
//				addCard(Solitaire.msg.top());
//			else
//				Solitaire.sender.addCard(Solitaire.msg.top());
//			Solitaire.msg.clear();
//			Solitaire.sender = null;
//			printStack();
		}
		else {
			if (!empty()) {
				if (!top().isFaceUp())
					top().flip();
				else {
					popMsg(x,y+VSHIFT*(size()-1-nselect));
//					Solitaire.msg.x = x;
//					Solitaire.msg.y = y+VSHIFT*(size()-1-nselect);
////					System.out.println("y = "+y+"nselect="+nselect+" msg's y = " + Solitaire.msg.y);
//					Solitaire.msg.addCard(pop(nselect));
//					Solitaire.msg.selectCards(true);
//					Solitaire.sender = this;
//					Solitaire.msg.printStack();
				}
			}
		}
	}

	public void select2(final int tx, final int ty) {
		nselect = getCardNByY(ty);
		if (Solitaire.sender!=null) {
			super.select(tx, ty);
		}
		else {
			if (!empty()) {
				if (!top().isFaceUp())
					top().flip();
				else {
					Solitaire.sender = this;
				/*	Solitaire.sender.*/selectCards(nselect+1, true);
				}
			}
		}
	}

	public void display(final Graphics g) {
		stackDisplay(g, (Card)firstCard);
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