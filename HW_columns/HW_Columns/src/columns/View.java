package columns;

public class View {
//	void moveFigure(Figure f);
	private MyGraphics _graphics;
	private int saveX;
	private int saveY;

	static final int  Width=7;		//field width in boxes
	static final int Depth=15;		//field height in boxes
    static final int
    SL=25,			//box size in pixels
    LeftBorder=2,
    TopBorder=2;
    
	void setGraphics(final MyGraphics graphics) {
		_graphics = graphics;
	}
	
   void DrawBox(int col, int row, int c) {
    	int x = LeftBorder+col*SL-SL;
    	int y= TopBorder+row*SL-SL;
    	int mwidth = (c==8) ? 2:0;
    	_graphics.fillBox(x,y,SL,SL,c,mwidth);
    }
	 	
    void DrawField(int[][] data) {
    	
        int i,j;
        for (i=1; i<=View.Depth; i++) {
            for (j=1; j<=View.Width; j++) {
                DrawBox(j,i,data[i][j]);
            }
        }
        System.out.println("Field drawn");
//        Delay(1000);
    }
    void DrawFigure(Figure f, int x, int y) {
    	System.out.println("View.drawFigure from "+Thread.currentThread().getName());
    	saveX = x;
    	saveY = y;
        DrawBox(x,y,f.c[1]);
        DrawBox(x,y+1,f.c[2]);
        DrawBox(x,y+2,f.c[3]);
    }
    
//  temporary, unchecked
    void myHideFigure(int saveX, int saveY) {
        DrawBox(saveX,saveY,0);
        DrawBox(saveX,saveY+1,0);
        DrawBox(saveX,saveY+2,0);   	
    }

    void moveFigure(Figure fig, int x, int y) {
    	Utils.Delay(200);
    	System.out.println("View.moveFigure "+saveX+","+saveY);
    	myHideFigure(saveX, saveY);
    	DrawFigure(fig, x, y);		
    }

    void DrawFigure (Logic logic) {
    	State state = logic.getState();
    	DrawFigure(state.getFigure(), state.col, state.row);
    }
    void moveFigure(Logic logic) {
    	State state = logic.getState();
    	moveFigure(state.getFigure(), state.col, state.row);
    	
    }

	void HideFigure(Figure f) {
//	    DrawBox(f.col,f.row,0);
//	    DrawBox(f.col,f.row+1,0);
//	    DrawBox(f.col,f.row+2,0);
        DrawBox(saveX,saveY,0);
        DrawBox(saveX,saveY+1,0);
        DrawBox(saveX,saveY+2,0);   	

	}
    

}
