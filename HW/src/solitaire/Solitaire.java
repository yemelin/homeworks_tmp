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
	
	static ProvidePile sender = null;
//	static Card msg = null;

	static TablePile msg = new TablePile(0,0);
	
	public void init() {
		// first allocate the arrays
		allPiles = new CardPile[13];
		suitPile = new SuitPile[4];
		tableau = new TablePile[7];
		// then fill them in
		allPiles[0] = deckPile = new DeckPile(335, 5);
		allPiles[1] = discardPile = new DiscardPile(268, 5);
		for (int i = 0; i < 4; i++) {
			allPiles[2 + i] = suitPile[i] = new SuitPile(15 + 60 * i, 5);
		}
		for (int i = 0; i < 7; i++) {
			allPiles[6 + i] = tableau[i] = new TablePile(5 + 55 * i, 80, i + 1);
		}
	}
	
	public void tryTakeFromTable() {
		Card topCard = Solitaire.msg.top();
		for (int i = 0; i < 4; i++) {
			if (Solitaire.suitPile[i].canTake(topCard)) {
				Solitaire.suitPile[i].addMsg();
				return;
			}
		}
		// else see if any other table pile can take card
		for (int i = 0; i < 7; i++) {
			if (Solitaire.tableau[i].canTake(topCard)) {
				Solitaire.tableau[i].addMsg();
				return;
			}
		}
	}
	public void clickAction(int x, int y) {
		for (int i = 0; i < 13; i++) {
			if (allPiles[i].includes(x, y)) {
//				System.out.println("column "+i);
				allPiles[i].select(x, y);
				repaint();
//				System.out.println(Solitaire.msg.top().getRank());
				return;
			}
		}
		if (Solitaire.sender!=null) {
			Solitaire.msg.select(x, y);
			repaint();
		}		
	}
	
	public boolean mouseDown (final Event evt, final int x, final int y) {
		System.out.println("Down at "+ x+", "+y+" "+evt.clickCount);
//		for (int i = 0; i < 13; i++) {
//			if (allPiles[i].includes(x, y)) {
//				System.out.println("column "+i);
//				allPiles[i].select(x, y);
//				repaint();
//				return true;
//			}
//		}
//		if (Solitaire.sender!=null) {
//			Solitaire.msg.select(x, y);
//			repaint();
//		}
//		clickAction(x,y);
		if (evt.clickCount==2) {
			System.out.println(Solitaire.sender+" "+Solitaire.msg);
			if (Solitaire.sender!=null) {
				tryTakeFromTable();
				repaint();
				System.out.println("Auto!");
			}
		}
		else {
			System.out.println("1click");
			clickAction(x, y);
		}
		return true;
	}

	public boolean mouseUp(final Event evt, final int x, final int y) {
//		System.out.println("Up!");
		if (isdragged &&Solitaire.sender!=null) {
			isdragged = false;
			savex = -1; savey=-1;
			mouseDown(evt, x, y);
//			for (int i = 0; i < 13; i++) {
////				System.out.println("column "+i);
//				if (allPiles[i].includes(x, y)) {
//					System.out.println("acceptor found");
//					allPiles[i].select(x, y);
//					repaint();
//					return true;
//				}
//			}
//
//			if (Solitaire.sender!=null) {
//				Solitaire.msg.select(x,y);//return to sender!
//				repaint();
//				return true;
//			}
		}
		return true;
	}
	
	int savex = -1;
	int savey = -1;
	boolean isdragged = false;
	public boolean mouseDrag(final Event evt, final int x, final int y) {
//		System.out.println("Drag!"+x+","+y);
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
//		by VVY
		if (!msg.empty())
			msg.display(g);
	}
}