package solitaire;

public class SimpleStack {
	public SimpleStack(){}
	public SimpleStack(Card node){
		firstCard = node;
		if (node!=null) {
			sz++;
			while((node=node.next())!=null)
				sz++;
		}
	}

	private int sz = 0;
	public Card firstCard = null; //change later to private

//	public abstract static class Card {
////		Object o;
////		public Node(){};
//		public Card next()=null;//change later to private
//		public Card next() {
//			return next();
//		}
//	}
	
	public boolean isEmpty(){
		return (null==firstCard);
	}
	
	public int size(){
		return sz;
	}

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
	
	public void push (SimpleStack s) {
		if (!s.isEmpty()) {
			sz+=s.size();
			s.get(s.size()).setNext(firstCard);
			firstCard = s.top();
		}
	}

//	void push (Object o) {
//		Node node = new Node();
//		node.o=o;
//		push(node);
//	}
	
	public Card top() {
		return firstCard;
	}
	
	public Card pop(int i) {
		if (i>sz) return null;
		Card node = get(i-1);
		Card ret = firstCard;
		firstCard = node.next();
		node.setNext(null);
		sz-=i;
		return ret;
	}
	
	public Card pop() {
		return pop(0);
	}
	
	public void display() {
		Card node = firstCard;
		while (node!=null) {
			System.out.print(node.toString()+" ");
			node = node.next();
		}
		System.out.println("\n"+"size="+size());
	}
}
