package solitaire;

import java.awt.Color;
import java.awt.Graphics;

import solitaire.Card;

class CardPile extends SimpleStack {

//	private Card firstCard;

	// coordinates of the card pile
	protected int x;
	protected int y;
	
//	public class Node extends Card {
//		Node(int tx, int ty) {
//			super(tx, ty);
//		}
//	}
	
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
//		aCard.link = firstCard;
//		firstCard = aCard;
		push(aCard);
	}
//	by VVY
//	wrong concept, cards may not be linked which will result in a mess
	public boolean addCards(final Card loCard, final Card topCard){
//		loCard.link = firstCard;
//		firstCard = topCard;
//		loCard.link = null;
//		loCard.setNext(null);
		push(topCard);
		return true;
	}

	public boolean sendCard(CardPile pile) {
		if (!pile.canTake(top()))
			return false;
		pile.addCard(pop());
		return true;
		
	}

	public Card getCard (int n) {
//		Card card = firstCard;
//		int i=0;
//		while (i<n && card!=null) {
//			i++;
//			card = card.link;
//		}
//		return card;
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
//		Card result = null;
//		if (firstCard != null) {
//			result = firstCard;
//			firstCard = firstCard.link;
//		}
		return (Card)super.pop();
	}
	public Card pop(int i) {
//		Card result = null;
//		if (getCard(i)!=null) {
//			result = firstCard;
//			firstCard = getCard(i).link;
//		}
		return (Card)super.pop(i);
	}
	public void select(final int tx, final int ty) {
		// do nothing
		if (Solitaire.sender!=null)
			Solitaire.sender.toggleSelect();
	}

	public Card top() {
		return (Card)super.top();
	}
//	by VVY
//	public int size(){
//		Card card = firstCard;
//		int i=0;
//		while (card!=null) {
//			card=card.link;
//			i++;
//		}		
//		return i;
//	}
}