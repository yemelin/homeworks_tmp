package solitaire;

import java.awt.Color;
import java.awt.Graphics;

import solitaire.Card;

class CardPile extends SimpleStack {

//	private Card firstCard;

	// coordinates of the card pile
	protected int x;
	protected int y;
	
	CardPile(final int xl, final int yl) {
		x = xl;
		y = yl;
		firstCard = null;
	}

	// the following are sometimes overridden

	public boolean canTake(final Card aCard) {
		return false;
	}

	public void addCard(final Card aCard) {
		push(aCard);
	}

	public boolean sendCard(CardPile pile) {
		Card card = pop();
		if (!pile.canTake(card)) {
			addCard(card);
			return false;
		}
		pile.addCard(card);
		return true;
		
	}

	public Card getCard (int n) {
		return (Card)get(n); //cast... hmm... Makes me worried
	}
	
	public void display(final Graphics g) {
		g.setColor(Color.black);
		if (firstCard == null) {
			g.drawRect(x, y, Card.width, Card.height);
		} else {
			((Card)firstCard).draw(g, x, y);
			top().draw(g, x, y);
		}
	}

	public boolean empty() {
		return firstCard == null;
	}

	public boolean includes(final int tx, final int ty) {
		return x <= tx && tx <= x + Card.width && y <= ty
				&& ty <= y + Card.height;
	}

	public Card pop() {
		return (Card)super.pop();
	}
	public Card pop(int i) {
		return (Card)super.pop(i);
	}

//	temporary card selection method
	public void selectCards(int n, boolean val) {
		System.out.println("Select cards");
		Card card = top();
		int i=0;
		while (card!=null && i++<n) {
			card.selected = val;
			card = card.next();
		}
	}
	public void selectCards(boolean val) {
		Card card = top();
		while (card!=null) {
			card.selected = val;
			card = card.next();
		}
	}
	public void takeFromTable(Card card){
		if (canTake(card))
			addCard(card);
		else
			Solitaire.sender.addCard(card);
	}
	
	public void popMsg(){}
	public void addMsg(){
		Solitaire.msg.selectCards(false);
		if (canTake(Solitaire.msg.top()))
			addCard(Solitaire.msg.top());
		else
			Solitaire.sender.addCard(Solitaire.msg.top());
		Solitaire.msg.clear();
		Solitaire.sender = null;

	}
//	------------------------------
//	change it to "do nothing"
	public void select(final int tx, final int ty) {
		ProvidePile sender = Solitaire.sender; //just to make name shorter
		if (sender!=null) {
			sender.selectCards(sender.nselect+1, false);
			if (isTakingFromTable())
				takeFromTable(sender.pop(sender.nselect));
			Solitaire.sender = null;
		}
	}

	public boolean isTakingFromTable() {
		return false;
	}

	public Card top() {
		return (Card)super.top();
	}
}