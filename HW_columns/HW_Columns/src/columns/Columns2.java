package columns;

import java.applet.*;
import java.awt.*;
import java.util.*;


public class Columns2 extends Applet implements Runnable {
  
	static final int
	TimeShift=250,
	MinTimeShift=200;	//time delay on the highest level
    
    Color MyStyles[] = {Color.black,Color.cyan,Color.blue,Color.red,Color.green,
    Color.yellow,Color.pink,Color.magenta,Color.black};
    
//    int Level, i, j, ii, k; 
    int ch;
//    long Score, DScore;
    long tc;
    Font fCourier;
//    Figure Fig;
//    int Fnew[][],Fold[][];
    boolean NoChanges = true, KeyPressed = false;
    Graphics _gr;   
    Thread thr = null;
    Controller controller;
//--------------------------------------    

    public void init() {
        _gr = getGraphics();
        setSize(250, 500);

        Model model = new Model();
        controller = new Controller();
        
        View view = new View();
        view.setGraphics(new MyGraphics() {

			@Override
			public void fillBox(int x, int y, int width, int height,
					int colorIndex, int bwidth) {
			    	_gr.setColor(MyStyles[colorIndex]);
			    	_gr.fillRect(x, y, width, height);
			    	_gr.setColor(Color.BLACK);
			    	_gr.drawRect(x, y, width, height);
			    	_gr.setColor(Color.WHITE);
			    	for (int i=1; i<=bwidth; i++)
			    		_gr.drawRect(x+i, y+i, width-i*2, height-i*2);    	
			}

			@Override
			public void drawString(String s, int x, int y) {
				_gr.setColor(Color.BLACK);
		        _gr.clearRect(x,y,100,20);
		        _gr.drawString(s,x,y+10);				
			}        	
        });
        
        controller.setModel(model);
        controller.setView(view);
        model.addListener(controller);       
        
//        Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (!controller.FullField()) {
//					Utils.Delay(controller.getDelay());
//					model.moveDown();
//				}
//			}
//        }, "Timer");
//        
//		thread.setDaemon(true);
//		thread.start();
    }
    
    
    public boolean keyDown(Event e, int k) {
        KeyPressed = true;
        ch = e.key;
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
        controller.requestRepaintEvent();
        requestFocus();
    }

    public void run() {
        _gr.setColor(Color.black);
        requestFocus();
        
        tc = System.currentTimeMillis();
        do {
            	if ((int)(System.currentTimeMillis()-tc)>controller.getDelay()) { 
                    tc = System.currentTimeMillis();
                    controller.moveDown();
                }
                do {
                    Utils.Delay(50);
                    if (KeyPressed) {
                        KeyPressed = false;
                        switch (ch) {
                            case Event.LEFT:
                            	controller.moveLeft();
                                break;
                            case Event.RIGHT:
                                controller.moveRight();
                                break;
                            case Event.UP:
                            	controller.scrollColorsUp();
                                break;
                            case Event.DOWN:
                            	controller.scrollColorsDown();
                                break;
                            case ' ':
                            	tc = 0;
                            	controller.dropFigure();
                            	tc = System.currentTimeMillis();
                                break;
                            case 'P':
                            case 'p':
                                while (!KeyPressed) {
//                                    HideFigure(Fig);
                                	System.out.println("on pause...");
                                    Utils.Delay(500);
//                                    DrawFigure(Fig);
                                	System.out.println("on pause...");
                                    Utils.Delay(500);
                                }
                                tc = System.currentTimeMillis();
                                break;
                            case '-':
                            	controller.levelDown();
                                break;
                            case '+':
                            	controller.levelUp();
                                break;
                        }
                    }
                } while ( (int)(System.currentTimeMillis()-tc) <= controller.getDelay());
        }while (!controller.FullField());
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
    @Override
    public void start() {
        _gr.setColor(Color.black);
        
        //		setBackground (new Color(180,180,180));
        
        if (thr == null) {
            thr = new Thread(this);
            thr.start();
        }
    }
    public void stop() {
        if (thr != null) {
            thr.stop();
            thr = null;
        }
    }
}