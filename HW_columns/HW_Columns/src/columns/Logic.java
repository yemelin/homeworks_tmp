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
	
	Logic() {
        Fnew = new int[Columns.Width+2][Columns.Depth+2];//move it!
        Fold = new int[Columns.Width+2][Columns.Depth+2];
        for (int i=0; i<Columns.Width+1; i++){
            for (int j=0; j<Columns.Depth+1; j++) {
                Fnew[i][j] = 0;
                Fold[i][j] = 0;
            }
        }
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

	void moveLeft() {
	    Fig.x--;
	}

	void moveRight() {
	    Fig.x++;
	}

	void moveDown() {
		Fig.y++;
	}
	//------------------------------------------------    
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
	    Fnew[Fig.x][Fig.y] = Fig.c[1];
	    Fnew[Fig.x][Fig.y+1] = Fig.c[2];
	    Fnew[Fig.x][Fig.y+2] = Fig.c[3];
	}

	boolean canMoveLeft() {
		return (Fig.x>1) && (Fnew[Fig.x-1][Fig.y+2]==0);
	}
	
	boolean canMoveRight() {
		return (Fig.x<Columns.Width) && (Fnew[Fig.x+1][Fig.y+2]==0);
	}
	boolean canMoveDown() {
		return (Fig.y<Columns.Depth-2) && (Fnew[Fig.x][Fig.y+3]==0);
	}

	void DropFigure() {
	    int zz;
	    if (Fig.y < Columns.Depth-2) {//if the figure is not at the bottom
	        zz = Columns.Depth;	//find the first colored box under it (or the bottom)
	        while (Fnew[Fig.x][zz]>0) 
	        	zz--;
	        DScore = (((Level+1)*(Columns.Depth*2-Fig.y-zz) * 2) % 5) * 5;
	        Fig.y = zz-2;		//drop
	    }
	}

	boolean processNeighbours(int a, int b, int c, int d, int i, int j) {
		if ((Fnew[j][i]==Fnew[a][b]) && (Fnew[j][i]==Fnew[c][d])) {
		    Fold[a][b] = 0;
		    Fold[j][i] = 0;
		    Fold[c][d] = 0;
		    NoChanges = false;
		    Score += (Level+1)*10;
		    k++;
		    return true;
		};
		return false;
	}

	void PackField() {
	    int i,j,n;
	    for (i=1; i<=Columns.Width; i++) {
	        n = Columns.Depth;
	        for (j=Columns.Depth; j>0; j--) {
	            if (Fold[i][j]>0) {
	                Fnew[i][n] = Fold[i][j];
	                n--;
	            }
	        };
	        for (j=n; j>0; j--) Fnew[i][j] = 0;
	    }
	}

	
}
