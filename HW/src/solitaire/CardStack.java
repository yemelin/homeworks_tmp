package solitaire;

public class CardStack {
//	constructors unused
	public CardStack(){}
	public CardStack(Card node){
		firstCard = node;
		if (node!=null) {
			sz++;
			while((node=node.next())!=null)
				sz++;
		}
	}

	private int sz = 0;
	private Card firstCard = null; //perhaps should be protected
	
	public void clear() {
		sz = 0;
		firstCard = null;
	}
	public boolean isEmpty(){
		return (null==firstCard);
	}
	
	public final int size(){
		return sz;
	}

//	return size starting from the Card. Should be a static method, as it's
//	not related to a particular stack
	public int size(Card node) {
		int i=0;
		while (node!=null) {
			node = node.next();
			i++;
		}
		return i;
	}
	
	public Card get(int n) {
		Card node = firstCard;
		while (node!=null && n-->0) 
			node = node.next();
		return node;
	}

//	pushes the whole linked list linked to the argument Card
	public void push(final Card node){
		if (node!=null) {
			Card nd = node;
			while (nd.next()!=null) {
				nd=nd.next();
				sz++;
			}
			sz++;
			nd.setNext(firstCard);
			firstCard = node;
		}
	}
	
	public void push (CardStack s) {
		if (!s.isEmpty()) {
			sz+=s.size();
			s.get(s.size()).setNext(firstCard);
			firstCard = s.top();
		}
	}

//	Commented out. Not needed, and also may create arg ambiguity. Delete later. 
//	void push (Object o) {
//		Node node = new Node();
//		node.o=o;
//		push(node);
//	}
	
	public final Card top() {
		return firstCard;
	}
	
	public final Card pop(int i) {
		if (i>sz) 
			return null;
		Card node = get(i);
		Card ret = firstCard;
		firstCard = node.next();
		node.setNext(null);
		sz-=i+1;//error fixed!!!
		return ret;
	}
	
//	return a copy of the card, with a link set to null
	public Card popCopy(int i) {
		Card card = get(i); 
		Card ret  = new Card(card.getSuit(), card.getRank());
		return ret;
	}
	
	public final Card pop() {
		return pop(0);
	}
	
	public void printStack() {
		Card node = firstCard;
		while (node!=null) {
			System.out.print(node.toString()+" ");
			node = node.next();
		}
		System.out.println("\n"+"size="+size());
	}
}
