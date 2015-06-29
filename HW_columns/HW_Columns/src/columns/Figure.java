package columns;
import java.util.*;

// singleton
class Figure {
	private Random r = new Random();
	private static Figure _fig = null;
	private Figure() {}
	
	int c[]=new int[4];//line colors. 0-slot only maintains 1-based numbering
	private void fillRandom() {
		System.out.println("new Figure from "+Thread.currentThread().getName());
		c[1] = (int)(Math.abs(r.nextInt())%7+1);
		c[2] = (int)(Math.abs(r.nextInt())%7+1);
		c[3] = (int)(Math.abs(r.nextInt())%7+1);	
	}
	public static Figure newFigure() {
		if (_fig==null)
			_fig = new Figure();
		_fig.fillRandom();
		return _fig;
	}
}