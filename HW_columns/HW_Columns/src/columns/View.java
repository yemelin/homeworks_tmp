package columns;

public class View {
	private MyGraphics _graphics;
	private int saveX;
	private int saveY;

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
    	int mwidth = (c==8) ? 2:0;	//white margin's width
    	_graphics.fillBox(x,y,SL,SL,c,mwidth);
    }
	 	
    void DrawField(int[][] data) {
    	
        int i,j;
        for (i=1; i<=Field.Depth; i++) {
            for (j=1; j<=Field.Width; j++) {
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
    
    private void HideFigureAt(int saveX, int saveY) {
        DrawBox(saveX,saveY,0);
        DrawBox(saveX,saveY+1,0);
        DrawBox(saveX,saveY+2,0);   	
    }

    void moveFigure(Figure fig, int x, int y) {
    	HideFigureAt(saveX, saveY);
    	DrawFigure(fig, x, y);		
    }

    void DrawFigure (State state) {
    	DrawFigure(state.getFigure(), state.col, state.row);
    }
    
    void moveFigure(State state) {
    	moveFigure(state.getFigure(), state.col, state.row);    	
    }

	void HideFigure() {
		HideFigureAt(saveX,saveY);
	}
    
    void ShowLevel(State state) {
        _graphics.drawString("Level: "+state.getScore().Level,View.LeftBorder+100,400);
    }

    void ShowScore(State state) {
        _graphics.drawString("Score: "+state.getScore().Score,View.LeftBorder,400);
    }

}
