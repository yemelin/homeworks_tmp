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
	
	void fireMovedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onMove();
		}
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
		
	}

}
