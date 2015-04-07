package solitaire;

//generic class for piles, providing cards to be put on the table or suit piles
public class ProvidePile extends CardPile{

	public ProvidePile(int xl, int yl) {
		super(xl, yl);
	}
	
	public boolean selected = false;
	public int nselect;

	@Override
//	pop selected cards and fill the msg pile with them
//	it's in this class and uses x,y args, because TablePile provides y
//	slightly in a different way than DiscardPile
	public void popMsg(int x, int y){
		Solitaire.msg.x = x;
		Solitaire.msg.y = y;
		Solitaire.msg.push(pop(nselect));
		Solitaire.msg.selectCards(true);
		Solitaire.setSender(this);

	}	
}
