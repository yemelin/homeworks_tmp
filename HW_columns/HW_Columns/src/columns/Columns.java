package columns;

import java.applet.*;
import java.awt.*;
import java.util.*;


public class Columns extends Applet {
    static final int
    MaxLevel=7,
    TimeShift=250,
    FigToDrop=33,	//max number of lines built for level
    MinTimeShift=200;	//time delay on the highest level
    
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
    Model model;
    Logic logic;

    
    void fillBox(int x, int y, int width, int height, int colorIndex, int bwidth){
    	_gr.setColor(MyStyles[colorIndex]);
    	_gr.fillRect(x, y, width, height);
    	_gr.setColor(Color.BLACK);
    	_gr.drawRect(x, y, width, height);
    	_gr.setColor(Color.WHITE);
    	for (int i=1; i<=bwidth; i++)
    		_gr.drawRect(x+i, y+i, width-i*2, height-i*2);    	
    }
    
    //  Essentially a game over check. Check if any of the top boxes if colored.
    boolean FullField() {
        int i;
        for (i=1; i<=View.Width; i++) {
            if (logic.Fnew[i][3]>0)
                return true;
        }
        return false;
    }
    
    public void init() {
        _gr = getGraphics();
        setSize(250, 500);

        model = new Model();
        logic = model._logic;
        controller = new Controller();
        
        view = new View();
        Columns self = this;
        view.setGraphics(new MyGraphics() {

			@Override
			public void fillBox(int x, int y, int width, int height,
					int colorIndex, int bwidth) {
				self.fillBox(x, y, width, height, colorIndex, bwidth);
				
			}
        	
        });
        
        controller.setModel(model);
        controller.setView(view);
        
        model.addListener(controller);       
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!FullField()) {
//					view.DrawFigure(logic); //needed because of side effect. FIX!
					Utils.Delay(getDelay());
					model.moveDown();
				}
			}
        }, "Timer");
        
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
    
	private int getDelay() {
		return (MaxLevel-logic.Level)*TimeShift+MinTimeShift;
	}
/*    
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
    */
    void ShowLevel(Graphics g) {
        g.setColor(Color.black);
        g.clearRect(View.LeftBorder+100,390,100,20);
        g.drawString("Level: "+logic.Level,View.LeftBorder+100,400);
    }
    void ShowScore(Graphics g) {
        g.setColor(Color.black);
        g.clearRect(View.LeftBorder,390,100,20);
        g.drawString("Score: "+logic.Score,View.LeftBorder,400);
    }
}