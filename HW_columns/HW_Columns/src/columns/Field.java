package columns;

public class Field {
	static final int Depth=15;		//field height in boxes
	static final int  Width=7;		//field width in boxes
	
	private int[][] _data;
	private int [][] _oldData;
	
	public Field() {
		_data = new int[Depth+2][Width+2];
		_oldData = new int[Depth+2][Width+2];
	}
	
	public int[][] getData() {
		return _data;
	}

	public int[][] getOldData() {
		return _oldData;
	}
}
