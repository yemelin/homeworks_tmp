package solitaire;

import java.awt.Color;
import java.awt.Graphics;

import solitaire.Card;

class CardPile extends CardStack {

	// coordinates of the card pile
	protected int x;
	protected int y;
	
	CardPile(final int xl, final int yl) {
		x = xl;
		y = yl;
//		firstCard = null;
		clear();
	}

	public boolean canTake(final Card aCard) {
		return false;
	}
	
	public void display(final Graphics g) {
		g.setColor(Color.black);
		if (top() == null) {
			g.drawRect(x, y, Card.width, Card.height);
		} else {
//			(firstCard).draw(g, x, y);
			top().draw(g, x, y);
		}
	}

	public boolean includes(final int tx, final int ty) {
		return x <= tx && tx <= x + Card.width && y <= ty
				&& ty <= y + Card.height;
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
	
	public void popMsg(int x, int y){}
//	deselect cards in msg, either take them or return to sender, clear msg
	public void addMsg(){
		Solitaire.msg.selectCards(false);
		if (canTake(Solitaire.msg.top()))
			push(Solitaire.msg.top());
		else
			Solitaire.getSender().push(Solitaire.msg.top());
		Solitaire.msg.clear();
//		Solitaire.sender = null; //unnecessary

	}

	public void select(final int tx, final int ty) {
		//	do nothing
	}
}