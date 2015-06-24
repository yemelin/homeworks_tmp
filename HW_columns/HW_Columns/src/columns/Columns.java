package columns;

import java.applet.*;
import java.awt.*;
import java.util.*;


public class Columns extends Applet  implements ModelListener {
    static final int
    SL=25,			//box size in pixels
    Depth=15,		//field height in boxes
    Width=7,		//field width in boxes
    MaxLevel=7,
    TimeShift=250,
    FigToDrop=33,	//max number of lines built for level
    MinTimeShift=200,	//time delay on the highest level
    LeftBorder=2,
    TopBorder=2;
    
    
    Color MyStyles[] = {Color.black,Color.cyan,Color.blue,Color.red,Color.green,
    Color.yellow,Color.pink,Color.magenta,Color.black};
    
    
    int	i, j, ii, 
    	ch;	//saves the key pressed
    long tc;		//time counter
    Font fCourier;
    boolean KeyPressed = false;
    boolean isPaused = false;
    Graphics _gr;
    
    Thread thr = null;
    
//VVY
    Model model = new Model();
    Logic logic = model._logic;
    Figure Fig = logic.getFigure();

	private int saveX;
	private int saveY;

//if (a,b),(c,d),(j,i) boxes are of the same color, replace them with a
// replacement dummy (empty box with thick white border), set the flag
// to signal the necessity to call PackField
    void CheckNeighbours(int a, int b, int c, int d, int i, int j) {
//    	System.out.println("checkneighbours");
        if (logic.processNeighbours(a, b, c, d, i, j)) {
        	DrawBox(a,b,8);
        	DrawBox(j,i,8);
        	DrawBox(c,d,8);
        }
    }
    void Delay(long t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {};
    }
    
    void DrawBox(int x, int y, int c) {
        if (c==0) {		//empty box
            _gr.setColor(Color.black);
            _gr.fillRect(LeftBorder+x*SL-SL, TopBorder+y*SL-SL, SL, SL);
            _gr.drawRect(LeftBorder+x*SL-SL, TopBorder+y*SL-SL, SL, SL);
        }
        else if (c==8) {	//replacement dummy box
            _gr.setColor(Color.white);
            _gr.drawRect(LeftBorder+x*SL-SL+1, TopBorder+y*SL-SL+1, SL-2, SL-2);
            _gr.drawRect(LeftBorder+x*SL-SL+2, TopBorder+y*SL-SL+2, SL-4, SL-4);
            _gr.setColor(Color.black);
            _gr.fillRect(LeftBorder+x*SL-SL+3, TopBorder+y*SL-SL+3, SL-6, SL-6);
        }
        else {	//colored box
            _gr.setColor(MyStyles[c]);
            _gr.fillRect(LeftBorder+x*SL-SL, TopBorder+y*SL-SL, SL, SL);
            _gr.setColor(Color.black);
            _gr.drawRect(LeftBorder+x*SL-SL, TopBorder+y*SL-SL, SL, SL);
        }
        //		g.setColor (Color.black);
    }
    
    void DrawField(Graphics g) {
        int i,j;
        for (i=1; i<=Depth; i++) {
            for (j=1; j<=Width; j++) {
                DrawBox(j,i,logic.Fnew[j][i]);
            }
        }
    }
    
    void DrawFigure(Figure f) {
    	saveX = f.x;
    	saveY = f.y;
        DrawBox(f.x,f.y,f.c[1]);
        DrawBox(f.x,f.y+1,f.c[2]);
        DrawBox(f.x,f.y+2,f.c[3]);
    }
    
    //  Essentially a game over check. Check if any of the top boxes if colored.
    boolean FullField() {
        int i;
        for (i=1; i<=Width; i++) {
            if (logic.Fnew[i][3]>0)
                return true;
        }
        return false;
    }
//  temporary, unchecked
    void myHideFigure(int saveX, int saveY) {
        DrawBox(saveX,saveY,0);
        DrawBox(saveX,saveY+1,0);
        DrawBox(saveX,saveY+2,0);   	
    }
    
    void HideFigure(Figure f) {
        DrawBox(f.x,f.y,0);
        DrawBox(f.x,f.y+1,0);
        DrawBox(f.x,f.y+2,0);
    }
    public void init() {
        _gr = getGraphics();
        model.addListener(this);
        
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
	            logic.newFigure();
	            Fig = logic.getFigure();
				while (!FullField()) {
//					System.out.println("show");
					DrawFigure(Fig);
//					System.out.println("long delay "+getDelay()+1000);
					Delay(getDelay()+1000);
					if (logic.canMoveDown()) {
						HideFigure(Fig);
						logic.moveDown();
						DrawFigure(Fig);
					}
					else {
			            logic.PasteFigure();
			            do {
			            	logic.NoChanges = true;
			            	TestField();
			            	if (!logic.NoChanges) {
			            		System.out.println("Small delay");
			            		Delay(500);
			            		logic.PackField();
			            		DrawField(_gr);
			            		logic.Score += logic.DScore;
			            		ShowScore(_gr);
			            		if (logic.k>=FigToDrop) {
			            			logic.k = 0;
			            			if (logic.Level<MaxLevel) logic.Level++;
			            				ShowLevel(_gr);
			            		}
			            	}
			            } while (!logic.NoChanges);
			            System.out.println("new");
			            logic.newFigure();
			            Fig = logic.getFigure();
//			            DrawFigure(Fig);
					}				
				}
			}
        });
		thread.setDaemon(true);
		thread.start();
    }
    public boolean keyDown(Event e, int k) {
//        KeyPressed = true;
//        ch = e.key;
    	KeyPressed = false;
//    	if (isPaused)
    	isPaused = false;
        ch = k;
//        System.out.println(ch);
//------------------------------        
        switch (ch) {
        case Event.LEFT:
        	model.moveLeft();
            break;
        case Event.RIGHT:
        	model.moveRight();
            break;
        case Event.UP:
        	logic.scrollColorsUp();
    		DrawFigure(Fig);
            break;
        case Event.DOWN:
        	logic.scrollColorsDown();
    		DrawFigure(Fig);
            break;
        case ' ':
            HideFigure(Fig);
            logic.DropFigure();
            DrawFigure(Fig);
            tc = 0;
            break;
        case 80://'P':
        case 112://'p':
//            while (!KeyPressed) {
//                HideFigure(Fig);
//                Delay(500);
//                DrawFigure(Fig);
//                Delay(500);
//            }
        	isPaused=true; //fix later or move back to run()
            tc = System.currentTimeMillis();
            break;
        case '-':
            if (logic.Level > 0) logic.Level--;
            logic.k=0;
            ShowLevel(_gr);
            break;
        case '+':
            if (logic.Level < MaxLevel) logic.Level++;
            logic.k=0;
            ShowLevel(_gr);
            break;
    }
        return true;
    }
    public boolean lostFocus(Event e, Object w) {
        KeyPressed = true;
        ch = 'P';
        return true;
    }
    
    public void paint(Graphics g) {
        //		ShowHelp(g);
        
        g.setColor(Color.black);
        
        ShowLevel(g);
        ShowScore(g);
        DrawField(g);
        DrawFigure(logic.Fig); //FIX!!
        requestFocus();
//        System.out.println("paint called");
    }
    
    
    public void run2() {
        _gr.setColor(Color.black);
        requestFocus();
        
        do {
            tc = System.currentTimeMillis();
            logic.newFigure();
            Fig = logic.getFigure();
            DrawFigure(Fig);
            while (logic.canMoveDown()) {
                if ((int)(System.currentTimeMillis()-tc)>getDelay()) {
                    tc = System.currentTimeMillis();
                    HideFigure(Fig);
                    logic.moveDown();
                    DrawFigure(Fig);
                }
                logic.DScore = 0;
                do {
                    Delay(50);//perhaps unneeded
//                    if (KeyPressed) {
//                        KeyPressed = false;
//                        switch (ch) {
//                            case Event.LEFT:
//                        		if (logic.canMoveLeft()) {
//                        		    HideFigure(Fig);
//                        		    logic.moveLeft();
//                        		    DrawFigure(Fig);
//                        		}
//                                break;
//                            case Event.RIGHT:
//                        		if (logic.canMoveRight()) {
//                        		    HideFigure(Fig);
//                        		    logic.moveRight();
//                        		    DrawFigure(Fig);
//                        		}
//                                break;
//                            case Event.UP:
//                            	logic.scrollColorsUp();
//                        		DrawFigure(Fig);
//                                break;
//                            case Event.DOWN:
//                            	logic.scrollColorsDown();
//                        		DrawFigure(Fig);
//                                break;
//                            case ' ':
//                                HideFigure(Fig);
//                                logic.DropFigure();
//                                DrawFigure(Fig);
//                                tc = 0;
//                                break;
//                            case 'P':
//                            case 'p':
//                                while (!KeyPressed) {
//                                    HideFigure(Fig);
//                                    Delay(500);
//                                    DrawFigure(Fig);
//                                    Delay(500);
//                                }
//                                tc = System.currentTimeMillis();
//                                break;
//                            case '-':
//                                if (logic.Level > 0) logic.Level--;
//                                logic.k=0;
//                                ShowLevel(_gr);
//                                break;
//                            case '+':
//                                if (logic.Level < MaxLevel) logic.Level++;
//                                logic.k=0;
//                                ShowLevel(_gr);
//                                break;
//                        }
//                    }
                    while (isPaused) {
                        HideFigure(Fig);
                        Delay(500);
                        DrawFigure(Fig);
                        Delay(500);
                    }
                } while ( (int)(System.currentTimeMillis()-tc) <= getDelay() );
            };
            logic.PasteFigure();
            do {
                logic.NoChanges = true;
                TestField();
                if (!logic.NoChanges) {
                    Delay(500);
                    logic.PackField();
                    DrawField(_gr);
                    logic.Score += logic.DScore;
                    ShowScore(_gr);
                    if (logic.k>=FigToDrop) {
                        logic.k = 0;
                        if (logic.Level<MaxLevel) logic.Level++;
                        ShowLevel(_gr);
                    }
                }
            } while (!logic.NoChanges);
        }while (!FullField());
    }
	private int getDelay() {
		return (MaxLevel-logic.Level)*TimeShift+MinTimeShift;
	}
    
	void ShowHelp(Graphics g) {
        g.setColor(Color.black);
        
        g.drawString(" Keys available:",200-LeftBorder,102);
        g.drawString("Roll Box Up:     ",200-LeftBorder,118);
        g.drawString("Roll Box Down:   ",200-LeftBorder,128);
        g.drawString("Figure Left:     ",200-LeftBorder,138);
        g.drawString("Figure Right:    ",200-LeftBorder,148);
        g.drawString("Level High/Low: +/-",200-LeftBorder,158);
        g.drawString("Drop Figure:   space",200-LeftBorder,168);
        g.drawString("Pause:           P",200-LeftBorder,180);
        g.drawString("Quit:     Esc or Q",200-LeftBorder,190);
    }
    void ShowLevel(Graphics g) {
        g.setColor(Color.black);
        g.clearRect(LeftBorder+100,390,100,20);
        g.drawString("Level: "+logic.Level,LeftBorder+100,400);
    }
    void ShowScore(Graphics g) {
        g.setColor(Color.black);
        g.clearRect(LeftBorder,390,100,20);
        g.drawString("Score: "+logic.Score,LeftBorder,400);
    }
    public void start() {
        _gr.setColor(Color.black);
        
        //		setBackground (new Color(180,180,180));
        
        if (thr == null) {
//            thr = new Thread(this);
//            thr.start();
        }
    }
    public void stop() {
        if (thr != null) {
//            thr.stop();
            thr = null;
        }
    }

// 
    void TestField() {
        int i,j;
//   deep copy the field
        for (i=1; i<=Depth; i++) {
            for (j=1; j<=Width; j++) {
                logic.Fold[j][i] = logic.Fnew [j][i];
            }
        }
        for (i=1; i<=Depth; i++) {
            for (j=1; j<=Width; j++) {
                if (logic.Fnew[j][i]>0) {
                    CheckNeighbours(j,i-1,j,i+1,i,j);	//horizontal
                    CheckNeighbours(j-1,i,j+1,i,i,j);	//vertical
                    CheckNeighbours(j-1,i-1,j+1,i+1,i,j);//diagonal
                    CheckNeighbours(j+1,i-1,j-1,i+1,i,j);//diagonal
                }
            }
        }
    }
    @Override
    public void onMove() {
    	moveFigure(Fig);
    }
	private void moveFigure(Figure fig) {
		myHideFigure(saveX, saveY);
		DrawFigure(fig);		
	}
}