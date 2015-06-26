package columns;

import java.util.Arrays;

public class Logic {

	Figure Fig;
	long DScore;  //drop bonus
	long Score;
	int Level;
	boolean NoChanges = true;
	int Fnew[][];
	int Fold[][]; //two field copies. Needed for deletions of lines
	int k;		  //triplet counter
	int saveDelRow;
	int saveDelCol;
	private State _state;
	
	Logic() {
        Fnew = new int[View.Depth+2][View.Width+2];
        Fold = new int[View.Depth+2][View.Width+2];
//        for (int i=0; i<Columns.Width+1; i++){
//            for (int j=0; j<Columns.Depth+1; j++) {
//                Fnew[i][j] = 0;
//                Fold[i][j] = 0;
//            }
//        }
        Level = 0;
        Score = 0;
        k = 0;
	}

	public Logic(final State state) {
		this();
		_state = state;
	}

	State getState() {
		return _state;
	}
	public Figure getFigure() {
		return Fig;
	}

	public void newFigure() {
		Fig = new Figure();		
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
			Fig = new Figure();
			_state.setFigure(Fig);
			return false;
		}
	}
    
	void scrollColorsDown() {
		int i = Fig.c[1];
		Fig.c[1] = Fig.c[3];
		Fig.c[3] = Fig.c[2];
		Fig.c[2] = i;
	}

	void scrollColorsUp() {
		int i = Fig.c[1];
		Fig.c[1] = Fig.c[2];
		Fig.c[2] = Fig.c[3];
		Fig.c[3] = i;
	}

	void PasteFigure() {
		System.out.println("pasted");
		int row = _state.row;
		int col = _state.col;
	    Fnew[row][col] = Fig.c[1];
	    Fnew[row+1][col] = Fig.c[2];
	    Fnew[row+2][col] = Fig.c[3];
	}

	boolean canMoveLeft() {
		return (_state.col>1) && (Fnew[_state.row+2][_state.col-1]==0);
	}
	
	boolean canMoveRight() {
		return (_state.col<View.Width) && (Fnew[_state.row+2][_state.col+1]==0);
	}
	boolean canMoveDown() {
		return (_state.row<View.Depth-2) && (Fnew[_state.row+3][_state.col]==0);
	}

	void DropFigure() {
		System.out.println("DropFigure from "+Thread.currentThread().getName());
	    int zz;
	    if (_state.row < View.Depth-2) {//if the figure is not at the bottom
	        zz = View.Depth;	//find the first colored box under it (or the bottom)
	        while (Fnew[zz][_state.col]>0) 
	        	zz--;
	        DScore = (((Level+1)*(View.Depth*2-_state.row-zz) * 2) % 5) * 5;
	        _state.row = zz-2;		//drop
	    }
	}

	boolean processNeighbours(int row1, int col1, int row2, int col2, int row3, int col3) {
		if ((Fnew[row3][col3]==Fnew[row1][col1]) && 
				(Fnew[row3][col3]==Fnew[row2][col2])) {
		    Fold[row1][col1] = 8; 
		    System.out.print(row1+","+col1+" ");
		    Fold[row3][col3] = 8;
		    System.out.print(row3+","+col3+" ");
		    Fold[row2][col2] = 8;
		    saveDelRow=row2; 
		    saveDelCol=col2;
		    System.out.println(row2+","+col2);
		    NoChanges = false;
		    Score += (Level+1)*10;
		    k++;
		    return true;
		};
		return false;
	}

	void PackField() {
	    int col,row,n;
	    for (col=1; col<=View.Width; col++) {
	        n = View.Depth;
	        for (row=View.Depth; row>0; row--) {
	            if (Fold[row][col]>0 && Fold[row][col]!=8) {
	                Fnew[n][col] = Fold[row][col];
	                n--;
	            }
	        };
	        for (row=n; row>0; row--) Fnew[row][col] = 0;
	    }
	}

	void copyField () {
		for (int row=1;row<Fnew.length;row++)
			System.arraycopy(Fnew[row], 0, Fold[row], 0, Fnew[row].length);
	}
	
	public boolean processField() {		
		NoChanges=true;
//		for (int row=View.Depth-2; row<=View.Depth; row++)
//			System.out.println(Arrays.toString(Fnew[row]));
		copyField(); //a side effect used by packField, fix later!
        for (int row=1; row<=View.Depth; row++) {
            for (int col=1; col<=View.Width; col++) {
                if (Fnew[row][col]>0) {
                    processNeighbours(row-1,col,row+1,col, row,col);	//horizontal
                    processNeighbours(row, col-1, row,col+1, row,col);	//vertical
                    processNeighbours(row-1,col-1, row+1,col+1, row,col);//diagonal
                    processNeighbours(row-1,col+1, row+1,col-1, row,col);//diagonal
                }
            }
        }
        System.out.println("processField, "+NoChanges);
		return !NoChanges;
	}
}
