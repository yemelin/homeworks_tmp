package columns;

public class Controller implements ModelListener {
	private View _view;
	private Model _model;
	private Logic logic; //a hack. fix!!
	
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
		logic = _model._logic;
	}
	
    @Override
    public void onMove() {
    	_view.moveFigure(logic);
    }    
    
	@Override
	public void onFieldChange() {
		System.out.println("onFieldChange");
		for (int i=1; i<=View.Depth; i++) {
			for (int j=1; j<=View.Width; j++) {
				if (logic.Fold[i][j]==8) {
					System.out.println("Deleted box found at "+j+","+i);
					_view.DrawBox(j, i, 8);
				}
			}
		}
		Utils.Delay(2500);
	}
	@Override
	public void onFieldPack() {
		System.out.println("onFieldPack");
		_view.DrawField(logic.Fnew);
//		ShowLevel(_gr);
//		ShowScore(_gr);
//		repaint(); //should probably replace the above calls
	}
}
