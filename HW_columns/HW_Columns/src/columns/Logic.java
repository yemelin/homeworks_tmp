package columns;

//TODO: move score and level to a separate class
public class Logic {

	private int _Fnew[][];
	private int _Fold[][]; //two field copies. Needed for deletions of lines
	private State _state;

	private Figure _fig;
	
	boolean NoChanges = true;
	private boolean _fullField = false;
	
	public Logic(final State state) {
		_state = state;
		_Fnew = _state.getField().getData();
		_fig = state.getFigure();
	}

	State getState() {
		return _state;
	}

	boolean moveLeft() {
		if (canMoveLeft()) {
			_state.col--;
			return true;
		}
		else return false;
	}

	boolean moveRight() {
		if (canMoveRight()) {
			_state.col++;
			return true;
		}
		else return false;
	}

	boolean moveDown() {
		System.out.println("Move down from "+Thread.currentThread().getName());
		if (canMoveDown()) {
			_state.row++;
			return true;
		}
		else {
			PasteFigure();			
			_state.setFigure(_fig = new Figure());
			return false;
		}
	}
    
	void scrollColorsDown() {
		int i = _fig.c[1];
		_fig.c[1] = _fig.c[3];
		_fig.c[3] = _fig.c[2];
		_fig.c[2] = i;
	}

	void scrollColorsUp() {
		int i = _fig.c[1];
		_fig.c[1] = _fig.c[2];
		_fig.c[2] = _fig.c[3];
		_fig.c[3] = i;
	}

	void PasteFigure() {
		System.out.println("pasted");
		int row = _state.row;
		int col = _state.col;
		System.out.println(row+","+col+"  Fnew: "+_Fnew+"  Fig:"+_fig);
	    _Fnew[row][col] = _fig.c[1];
	    _Fnew[row+1][col] = _fig.c[2];
	    _Fnew[row+2][col] = _fig.c[3];
	}

	boolean canMoveLeft() {
		return (_state.col>1) && (_Fnew[_state.row+2][_state.col-1]==0);
	}
	
	boolean canMoveRight() {
		return (_state.col<Field.Width) && (_Fnew[_state.row+2][_state.col+1]==0);
	}
	boolean canMoveDown() {
		return (_state.row<Field.Depth-2) && (_Fnew[_state.row+3][_state.col]==0);
	}

	void DropFigure() {
		System.out.println("DropFigure from "+Thread.currentThread().getName());
	    int zz;
	    if (_state.row < Field.Depth-2) {//if the figure is not at the bottom
	        zz = Field.Depth;	//find the first colored box under it (or the bottom)
	        while (_Fnew[zz][_state.col]>0) 
	        	zz--;
	        _state.getScore().setDropBonus(zz -_state.row);
//	        DScore = (((Level+1)*(View.Depth*2-_state.row-zz) * 2) % 5) * 5;
	        _state.row = zz-2;		//drop
	    }
	}

	boolean processNeighbours(int row1, int col1, int row2, int col2, int row3, int col3) {
		
		if ((_Fnew[row3][col3]==_Fnew[row1][col1]) && 
				(_Fnew[row3][col3]==_Fnew[row2][col2])) {
		    _Fold[row1][col1] = 8; 
		    _Fold[row3][col3] = 8;
		    _Fold[row2][col2] = 8;
		    NoChanges = false;
		    _state.getScore().addLineBonus();
		    _state.getScore().updateLineCounter();
//		    Score += (Level+1)*10;
//		    k++;
// TODO: update level here!
		    System.out.print(row1+","+col1+" ");
		    System.out.print(row3+","+col3+" ");
		    System.out.println(row2+","+col2);
		    return true;
		};
		return false;
	}

	void PackField() {
	    int col,row,n;
	    for (col=1; col<=Field.Width; col++) {
	        n = Field.Depth;
	        for (row=Field.Depth; row>0; row--) {
	            if (_Fold[row][col]>0 && _Fold[row][col]!=8) {
	                _Fnew[n][col] = _Fold[row][col];
	                n--;
	            }
	        };
	        for (row=n; row>0; row--) _Fnew[row][col] = 0;
	    }
	}

	void copyField () {
		for (int row=1;row<_Fnew.length;row++)
			System.arraycopy(_Fnew[row], 0, _Fold[row], 0, _Fnew[row].length);
	}
	
	public boolean processField() {		
		NoChanges=true;
		_state.copyFieldData(); //a side effect used by packField
		_Fold = _state.getOldFieldData();
        for (int row=1; row<=Field.Depth; row++) {
            for (int col=1; col<=Field.Width; col++) {
                if (_Fnew[row][col]>0) {
                    processNeighbours(row-1,col,row+1,col, row,col);	//horizontal
                    processNeighbours(row, col-1, row,col+1, row,col);	//vertical
                    processNeighbours(row-1,col-1, row+1,col+1, row,col);//diagonal
                    processNeighbours(row-1,col+1, row+1,col-1, row,col);//diagonal
                }
            }
        }
        if (!NoChanges)
        	_state.getScore().addDropBonus();
//        	Score+=DScore;
        	
        System.out.println("processField, "+NoChanges);
		return !NoChanges;
	}

	public void levelDown() {
		Score score = _state.getScore();
		if (score.Level>0) {
			score.Level--;
			score.k=0;
		}
	}

	public void levelUp() {
		Score score = _state.getScore();
		if (score.Level<Score.MaxLevel) {
			score.Level++;
			score.k=0;
		}			
	}
	
	//  Essentially a game over check. Check if any of the top boxes if colored.
    boolean FullField() {
    	if (!_fullField) {
    		int[][] Fnew = getState().getField().getData();
    		int i;
    		for (i=1; i<=Field.Width; i++) {
    			if (Fnew[3][i]>0) {
    				System.out.println("Game over");
    				_fullField = true;
    				break;
    			}
    		}
    	}
        return _fullField;
    }

}
