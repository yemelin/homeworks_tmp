package columns;

public interface ModelListener {
	void onMove(State state);
//	void onScroll(State state);
	void onFieldChange(State state);
	void onFieldPack(State state);
	void onNewFigure(State state);
	void onScoreChanged(State state);
	void onBlink(State state);
}
