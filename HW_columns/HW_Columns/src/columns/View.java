package columns;

public class View {
//	void moveFigure(Figure f);
	private MyGraphics _graphics;
	private int saveX;
	private int saveY;

	static final int  Width=7;		//field width in boxes
	static final int Depth=15;		//field height in boxes
	
	void setGraphics(final MyGraphics graphics) {
		_graphics = graphics;
	}
	
	void DrawBox(int x, int y, int c) {
		_graphics.DrawBox(x, y, c);
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
    	myHideFigure(saveX, saveY);
    	DrawFigure(fig, x, y);		
    }

    //    temporary hack! fix!
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
