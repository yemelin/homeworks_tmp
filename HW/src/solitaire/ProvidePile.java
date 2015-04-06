package solitaire;

//generic class for piles, providing cards to be put on the table or suit piles
public class ProvidePile extends CardPile{

	ProvidePile(int xl, int yl) {
		super(xl, yl);
	}
	
	public boolean selected = false;
	public int nselect;

	public void popMsg(int x, int y){
		Solitaire.msg.x = x;
		Solitaire.msg.y = y;
//		System.out.println("y = "+y+"nselect="+nselect+" msg's y = " + Solitaire.msg.y);
		Solitaire.msg.addCard(pop(nselect));
		Solitaire.msg.selectCards(true);
		Solitaire.sender = this;

	}
	
	public void addMsg(){
		Solitaire.msg.selectCards(false);
		if (canTake(Solitaire.msg.top()))
			addCard(Solitaire.msg.top());
		else
			Solitaire.sender.addCard(Solitaire.msg.top());
		Solitaire.msg.clear();
		Solitaire.sender = null;

	}
	
}
