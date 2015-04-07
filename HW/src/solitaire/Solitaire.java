package solitaire;

/*
 Simple Solitaire Card Game in Java
 Written by Tim Budd, Oregon State University, 1996
 */

import java.awt.*;
import java.applet.*;

public class Solitaire extends Applet {
	static CardPile allPiles[];
	static DeckPile deckPile;
	static DiscardPile discardPile;
	static SuitPile suitPile[];
	static TablePile tableau[];
	
	static TablePile msg; //"message" - cards to be moved
	static private ProvidePile sender;//link to the pile msg was taken from
	
	public void init() {
		setSize(400, 400);
		// first allocate the arrays
		allPiles = new CardPile[13];
		suitPile = new SuitPile[4];
		tableau = new TablePile[7];
		// then fill them in
		allPiles[0] = deckPile = new DeckPile(335, 5);
		allPiles[1] = discardPile = new DiscardPile(268, 5);
		
		msg = new TablePile(-1,-1);
		sender = null;
		
		for (int i = 0; i < 4; i++) {
			allPiles[2 + i] = suitPile[i] = new SuitPile(15 + 60 * i, 5);
		}
		for (int i = 0; i < 7; i++) {
			allPiles[6 + i] = tableau[i] = new TablePile(5 + 55 * i, 80, i + 1);
		}
	}
	
	public final static void setSender(ProvidePile pile) {
		sender = pile;
	}
	public final static ProvidePile getSender() {
		return sender;
	}


	public CardPile findTakingPile(Card card) {
		for (int i = 0; i < 4; i++) {
			if (Solitaire.suitPile[i].canTake(card)) {
				return suitPile[i];
			}
		}
		// else see if any other table pile can take card
		for (int i = 0; i < 7; i++) {
			if (Solitaire.tableau[i].canTake(card)) {
				return tableau[i];
			}
		}
		return null;
	}

	
	public void tryTakeFromTable() {
		Card card = Solitaire.msg.top();
		CardPile pile = findTakingPile(card);
		if (pile!=null)
			pile.addMsg(); //addCard(card) + clear message
	}
	
	public boolean isGameOver() {
//		int i;
		for (int i=0; i<deckPile.size(); i++)
			if (findTakingPile(deckPile.popCopy(i))!=null)
				return false;

		for (int i=0; i<discardPile.size(); i++)
			if (findTakingPile(discardPile.popCopy(i))!=null)
				return false;

		Card card;
		for (int j=0; j<tableau.length; j++) {
			for (int i=0; i<tableau[j].getFlipped(); i++) { 
				if (findTakingPile(card = tableau[j].pop(i))!=null) {
					tableau[j].push(card);
					return false;
				}
			}
		}
		return true;
	}
	
	public void clickAction(int x, int y) {
		for (int i = 0; i < 13; i++) {
			if (allPiles[i].includes(x, y)) {
				allPiles[i].select(x, y);
				repaint();
				return;
			}
		}
		if (!msg.isEmpty()/*sender!=null*/) {
			Solitaire.msg.select(x, y);
			repaint();
		}		
	}
	
	public boolean mouseDown (final Event evt, final int x, final int y) {
/*		Double click is handled as follows:
 * it seems, mousedown is called first with evt.clickCount==1, then again with
 * ==2 Therefore, first time it is called as normal click, and if it resulted
 * in creation a "message", the second call with clickCount==2 tries to send it.
 */
		if (evt.clickCount==2) {
			if (!msg.isEmpty()) {
				tryTakeFromTable();
				repaint();
			}
		}
		else {
			clickAction(x, y);
		}
//		------ Game over handling
		if (isGameOver())
			getAppletContext().showStatus("Game over!");
//			System.out.println("GameOver!");
		else
			getAppletContext().showStatus("there are moves");
//			System.out.println("has moves!");
//		-----------
		return true;
	}

	public boolean mouseUp(final Event evt, final int x, final int y) {
		if (isdragged && !msg.isEmpty()) {
			isdragged = false;
			savex = -1; savey=-1;
			mouseDown(evt, x, y);
		}
		return true;
	}
	
	int savex = -1;
	int savey = -1;
	boolean isdragged = false;
	public boolean mouseDrag(final Event evt, final int x, final int y) {
		isdragged = true;
		if (Solitaire.msg.includes(x, y)) {
			if (savex!=-1) {
				Solitaire.msg.x+=(x-savex);
				Solitaire.msg.y+=(y-savey);
			}
			savex = x;
			savey = y;
			repaint();
		}
		return true;
	}

	public void paint(final Graphics g) {
		for (int i = 0; i < 13; i++) {
			allPiles[i].display(g);
		}
//	msg pile should be the last to check
		if (!msg.isEmpty())
			msg.display(g);
	}
}