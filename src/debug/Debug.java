package debug;

import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import javax.swing.*;
import jp.sourceforge.acerola3d.a3.*;

public class Debug {
    public static void main(String args[]) throws Exception {
	    JFrame f = new JFrame("Debug");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    PVEWorld w = new PVEWorld(PVEWorld.A3CANVAS);
	    A3Canvas c = (A3Canvas)w.getMainCanvas();
	    c.setCameraLocImmediately(0,5,30);
	    c.setCameraLookAtPointImmediately(0,0,0);
	    c.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,30.0);
	    c.setSize(300,300);
	    f.add(c);
	    f.pack();
	    f.setVisible(true);
	    
	    Ground g = new Ground();
	    w.add(g);
	    //BoxObj b = new BoxObj();
	    SimpleCarObj b = new SimpleCarObj("x-res:///res/stk_tux.a3");
	    w.add(b);
	    b.setLoc(0,10,0);
	    w.resume();
	    double x=0;
	    while(true) {
	    	//b.setLoc(x,0,0);
	    	x=x+0.1;
	    	//System.out.println(b.getLoc());
	    	Thread.sleep(100);
	    }
    }
}
