package columns;

public class State {

	private Field _field;
	private Figure _figure;
	int col=View.Width/2+1;
	int row=1;
	public Field getField() {
		return _field;
	}
	public void setField(Field _field) {
		this._field = _field;
	}
	public Figure getFigure() {
		return _figure;
	}
	public void setFigure(Figure _figure) {
		this._figure = _figure;
		col = View.Width/2+1;
		row = 1;
	}
}
