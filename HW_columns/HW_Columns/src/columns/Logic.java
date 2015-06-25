package columns;

public class Logic {

	Figure Fig = new Figure();
	long DScore;  //drop bonus
	long Score;
	int Level;
	boolean NoChanges = true;
	int Fnew[][];
	int Fold[][]; //two field copies. Needed for deletions of lines
	int k;		  //triplet counter
	int saveDelRow;
	int saveDelCol;
	
	Logic() {
        Fnew = new int[Columns.Depth+2][Columns.Width+2];
        Fold = new int[Columns.Depth+2][Columns.Width+2];
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

	public Figure getFigure() {
		return Fig;
	}

	public void newFigure() {
		Fig = new Figure();		
	}

	boolean moveLeft() {
		if (canMoveLeft()) {
			Fig.col--;
			return true;
		}
		else return false;
	}

	boolean moveRight() {
		if (canMoveRight()) {
			Fig.col++;
			return true;
		}
		else return false;
	}

	boolean moveDown() {
		if (canMoveDown()) {
			Fig.row++;
			return true;
		}
		else {
			PasteFigure();			
			Fig = new Figure();
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
	    Fnew[Fig.row][Fig.col] = Fig.c[1];
	    Fnew[Fig.row+1][Fig.col] = Fig.c[2];
	    Fnew[Fig.row+2][Fig.col] = Fig.c[3];
	}

	boolean canMoveLeft() {
		return (Fig.col>1) && (Fnew[Fig.row+2][Fig.col-1]==0);
	}
	
	boolean canMoveRight() {
		return (Fig.col<Columns.Width) && (Fnew[Fig.row+2][Fig.col+1]==0);
	}
	boolean canMoveDown() {
		return (Fig.row<Columns.Depth-2) && (Fnew[Fig.row+3][Fig.col]==0);
	}

	void DropFigure() {
	    int zz;
	    if (Fig.row < Columns.Depth-2) {//if the figure is not at the bottom
	        zz = Columns.Depth;	//find the first colored box under it (or the bottom)
	        while (Fnew[zz][Fig.col]>0) 
	        	zz--;
	        DScore = (((Level+1)*(Columns.Depth*2-Fig.row-zz) * 2) % 5) * 5;
	        Fig.row = zz-2;		//drop
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
	    for (col=1; col<=Columns.Width; col++) {
	        n = Columns.Depth;
	        for (row=Columns.Depth; row>0; row--) {
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
		copyField(); //a side effect used by packField, fix later!
        for (int row=1; row<=Columns.Depth; row++) {
            for (int col=1; col<=Columns.Width; col++) {
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
