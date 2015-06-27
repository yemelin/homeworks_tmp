package columns;

import java.applet.*;
import java.awt.*;
import java.util.*;


public class Columns2 extends Applet {
    static final int
    SL=25,			//box size in pixels
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

    View view;
    Controller controller;
    Thread thr = null;
    Model model;
    Logic logic;
    
    Figure Fig;

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
        
    //  Essentially a game over check. Check if any of the top boxes if colored.
    boolean FullField() {
        int i;
        for (i=1; i<=Field.Width; i++) {
            if (logic.Fnew[i][3]>0)
                return true;
        }
        return false;
    }
    
    public void init() {
        _gr = getGraphics();
        setSize(500, 800);

        model = new Model();
        logic = model._logic;
        Fig = logic.getFigure();
        controller = new Controller();
        
        view = new View();
        Columns2 self = this;
        view.setGraphics(new MyGraphics() {

			@Override
			public void DrawBox(int x, int y, int c) {
				self.DrawBox(x, y, c);				
			}
        	
        });
        
        controller.setModel(model);
        controller.setView(view);
        
        model.addListener(controller);       
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!FullField()) {
					Fig = logic.getFigure();
					view.DrawFigure(logic); //needed because of side effect. FIX!
					Utils.Delay(getDelay());
					model.moveDown();
				}
			}
        });
        
		thread.setDaemon(true);
		thread.start();
    }
//  Event is obsolete, replace with AWTEvent
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
    		view.DrawFigure(logic);
            break;
        case Event.DOWN:
        	logic.scrollColorsDown();
    		view.DrawFigure(logic);
            break;
        case ' ':
//        	Delay(5000);
        	model.dropFigure();
            break;
        case 80://'P':
        case 112://'p':
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
        view.DrawField(logic.Fnew);        
        view.DrawFigure(logic);
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
            view.DrawFigure(logic);
            while (logic.canMoveDown()) {
                if ((int)(System.currentTimeMillis()-tc)>getDelay()) {
                    tc = System.currentTimeMillis();
                    view.HideFigure(Fig);
                    logic.moveDown();
                    view.DrawFigure(logic);
                }
                logic.DScore = 0;
                do {
                    Utils.Delay(50);//perhaps unneeded
//                    if (KeyPressed) {
//                        KeyPressed = false;
//                        switch (ch) {
//                            case Event.LEFT:
//                        		if (logic.canMoveLeft()) {
//                        		    HideFigure(Fig);
//                        		    logic.moveLeft();
//                        		    DrawFigure(logic);
//                        		}
//                                break;
//                            case Event.RIGHT:
//                        		if (logic.canMoveRight()) {
//                        		    HideFigure(Fig);
//                        		    logic.moveRight();
//                        		    DrawFigure(logic);
//                        		}
//                                break;
//                            case Event.UP:
//                            	logic.scrollColorsUp();
//                        		DrawFigure(logic);
//                                break;
//                            case Event.DOWN:
//                            	logic.scrollColorsDown();
//                        		DrawFigure(logic);
//                                break;
//                            case ' ':
//                                HideFigure(Fig);
//                                logic.DropFigure();
//                                DrawFigure(logic);
//                                tc = 0;
//                                break;
//                            case 'P':
//                            case 'p':
//                                while (!KeyPressed) {
//                                    HideFigure(Fig);
//                                    Delay(500);
//                                    DrawFigure(logic);
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
                        view.HideFigure(Fig);
                        Utils.Delay(500);
                        view.DrawFigure(logic);
                        Utils.Delay(500);
                    }
                } while ( (int)(System.currentTimeMillis()-tc) <= getDelay() );
            };
            logic.PasteFigure();
            do {
                logic.NoChanges = true;
                TestField();
                if (!logic.NoChanges) {
                    Utils.Delay(500);
                    logic.PackField();
                    view.DrawField(logic.Fnew);
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
        for (i=1; i<=Field.Depth; i++) {
            for (j=1; j<=Field.Width; j++) {
                logic.Fold[j][i] = logic.Fnew [j][i];
            }
        }
        for (i=1; i<=Field.Depth; i++) {
            for (j=1; j<=Field.Width; j++) {
                if (logic.Fnew[j][i]>0) {
                    CheckNeighbours(j,i-1,j,i+1,i,j);	//horizontal
                    CheckNeighbours(j-1,i,j+1,i,i,j);	//vertical
                    CheckNeighbours(j-1,i-1,j+1,i+1,i,j);//diagonal
                    CheckNeighbours(j+1,i-1,j-1,i+1,i,j);//diagonal
                }
            }
        }
    }
}