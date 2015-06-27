package columns;

import java.util.ArrayList;
import java.util.List;

public class Model {
	Logic _logic;
	
	public Model() {
		State state = new State();
		state.setField(new Field());
		state.setFigure(new Figure());
		state.setScore(new Score());
		_logic = new Logic(state);
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
		fireNewFigureEvent();
	}

	void scrollColorsUp() {
		_logic.scrollColorsUp();
		fireNewFigureEvent();
	}

	void scrollColorsDown() {
		_logic.scrollColorsDown();
		fireNewFigureEvent();
	}
		
	void dropFigure() {
		_logic.DropFigure();
		fireMovedEvent();
		moveDown();
	}

	void packField() {
		_logic.PackField();
		fireFieldPackedEvent();
		fireScoreChangedEvent();
	}
	
	private void fireScoreChangedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onScoreChanged(_logic.getState());
		}		
	}
	private void fireMovedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onMove(_logic.getState());
		}
	}
	private void fireFieldChangedEvent() {
		System.out.println("fire field changed");
		for (ModelListener modelListener : _listeners) {
			modelListener.onFieldChange(_logic.getState());
		}
	}

	private void fireFieldPackedEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onFieldPack(_logic.getState());
		}
	}
	
	private void fireNewFigureEvent() {
		for (ModelListener modelListener : _listeners) {
			modelListener.onNewFigure(_logic.getState());
		}		
	}
	
	public void sendRepaintEvent(ModelListener ml) {
		ml.onFieldPack(_logic.getState());
		ml.onNewFigure(_logic.getState());
		ml.onScoreChanged(_logic.getState());
	}

	public long getDelay() {
		return _logic.getState().getScore().calculateDelay();
	}

	public void levelDown() {
		_logic.levelDown();
		fireScoreChangedEvent();
	}

	public void levelUp() {
		_logic.levelUp();
		fireScoreChangedEvent();		
	}

    //  Essentially a game over check. Check if any of the top boxes if colored.
    boolean FullField() {
    	int[][] Fnew = _logic.getState().getField().getData();
        int i;
        for (i=1; i<=Field.Width; i++) {
            if (Fnew[i][3]>0)
                return true;
        }
        return false;
    }
}
