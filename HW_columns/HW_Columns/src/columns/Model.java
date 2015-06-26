package columns;

import java.util.ArrayList;
import java.util.List;

public class Model {
	Logic _logic;
	
	public Model() {
		Field field = new Field(View.Depth,View.Width);
		State state = new State();
		state.setField(field);
		_logic = new Logic(state);
		_logic.newFigure();
		state.setFigure(_logic.getFigure());
	}


	List<ModelListener> _listeners = new ArrayList<>();
	
	public void addListener(final ModelListener listener) {
		_listeners.add(listener);
	}
	
	public void removeListener(final ModelListener listener) {
		_listeners.remove(listener);
	}
	

	void moveLeft() {
		if (_logic.moveLeft()) {
			fireMovedEvent();
		}
	}
	
	void moveRight() {
		if (_logic.moveRight()) {
			fireMovedEvent();
		}
	}
	void moveDown() {
		if (_logic.moveDown()) {
			fireMovedEvent();
		}
		else while (_logic.processField()) {//while there are changes
			fireFieldChangedEvent();
			packField();
		}
		firenewFigureEvent();
	}

	void dropFigure() {
		_logic.DropFigure();
		fireMovedEvent();
		moveDown();
	}

	void packField() {
		_logic.PackField();
		fireFieldPackedEvent();
	}
	
	private void fireMovedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onMove();
		}
	}
	private void fireFieldChangedEvent() {
		System.out.println("fire field changed");
		for (ModelListener modelListener : _listeners) {
			modelListener.onFieldChange();
		}
	}


	private void fireFieldPackedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onFieldPack();
		}
	}
	
	private void firenewFigureEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onNewFigure();
		}		
	}
}
