package solitaire;

//generic class for piles, providing cards to be put on the table or suit piles
public class ProvidePile extends CardPile{

	ProvidePile(int xl, int yl) {
		super(xl, yl);
	}
	
	public boolean selected = false;
	public int nselect;

}
