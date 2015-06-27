package columns;

public class Field {
	
	private int[][] _data;
	private int [][] _oldData;
	
	public Field(final int rows, final int cols) {
		_data = new int[rows+2][cols+2];
		_oldData = new int[rows+2][cols+2];
	}

	public int getWidth() {
		return _data[0].length;
	}

	public int getHeight() {
		return _data.length;
	}

	public int[][] getData() {
		return _data;
	}

	public int[][] getOldData() {
		return _oldData;
	}
}
