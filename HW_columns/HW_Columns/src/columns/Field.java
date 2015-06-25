package columns;

public class Field {
	
	private int[][] _data;
	
	public Field(final int rows, final int cols) {
		_data = new int[rows][cols];
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

}
