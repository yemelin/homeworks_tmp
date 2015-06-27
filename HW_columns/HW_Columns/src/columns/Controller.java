package columns;

public class Controller implements ModelListener {
	private View _view;
	private Model _model;
	
	public View getView() {
		return _view;
	}
	public void setView(View _view) {
		this._view = _view;
	}
	public Model getModel() {
		return _model;
	}
	public void setModel(Model _model) {
		this._model = _model;
	}
	
    @Override
    public void onMove(State state) {
    	_view.moveFigure(state);
    }    
    
	@Override
	public void onFieldChange(State state) {
		System.out.println("onFieldChange");
		int[][] Fold = state.getOldFieldData();
		for (int i=1; i<=Field.Depth; i++) {
			for (int j=1; j<=Field.Width; j++) {
				if (Fold[i][j]==8) {
					_view.DrawBox(j, i, 8);
				}
			}
		}
		Utils.Delay(500);
	}
	@Override
	public void onFieldPack(State state) {
		System.out.println("onFieldPack");
		_view.DrawField(state.getField().getData());
//		ShowLevel(_gr);
//		ShowScore(_gr);
//		repaint(); //should probably replace the above calls
	}
	
	@Override
	public void onNewFigure(State state) {
		_view.DrawFigure(state);
	}
	
//	@Override
//	public void onScroll(State state) {
//		_view.DrawFigure(state);		
//	}
	public void requestRepaintEvent() {
		_model.sendRepaintEvent(this);	
	}
	public long getDelay() {
		return _model.getDelay();
	}
	@Override
	public void onScoreChanged(State state) {
		_view.ShowLevel(state);
		_view.ShowScore(state);		
	}
	public boolean FullField() {
		return _model.FullField();
	}
	public void moveLeft() {
		_model.moveLeft();
		
	}
	public void moveRight() {
		_model.moveRight();
		
	}
	public void scrollColorsUp() {
		_model.scrollColorsUp();		
	}
	public void scrollColorsDown() {
		_model.scrollColorsDown();		
	}
	public void dropFigure() {
		_model.dropFigure();				
	}
	public void levelDown() {
		_model.levelDown();		
	}
	public void levelUp() {
		_model.levelUp();		
	}
	public void moveDown() {
		_model.moveDown();
		
	}
}
