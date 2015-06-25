package columns;

import java.util.ArrayList;
import java.util.List;

public class Model {
	Logic _logic = new Logic();

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
	}
	void dropFigure() {
		_logic.DropFigure();
		fireMovedEvent();
		while (_logic.processField()) {//while there are changes
			fireFieldChangedEvent();
			packField();
		}
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
}
