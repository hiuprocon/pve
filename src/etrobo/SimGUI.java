package etrobo;

import java.awt.event.KeyListener;
import javax.swing.*;
import javax.vecmath.*;
import com.github.hiuprocon.pve.core.PVEObject;
import jp.sourceforge.acerola3d.a3.*;

public class SimGUI extends JFrame {
	private static final long serialVersionUID = 1L;
    SimEnv env;
	A3Canvas canvas;
    public SimGUI(SimEnv env) {
    	super("SimEnv");
    	canvas = (A3Canvas)(env.w.getMainCanvas());
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyController c = new MyController();
        canvas.setA3Controller(c);

        /*
    	//canvas.setCameraLocImmediately(0,5,20);
    	//canvas.setCameraLookAtPointImmediately(0,0,0);
        //double l = Math.sqrt(0+25+400);
        //canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,l);
    	Vector3d lookAt = new Vector3d(0,0,6);
    	Vector3d camera = new Vector3d(0,3,-6);
    	Vector3d up = new Vector3d(0,1,0);
    	canvas.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,lookAt,camera,up,1.0);
        */
    	canvas.setSize(800,500);
    	add(canvas);
    	pack();
    	setVisible(true);
    }
    public void setETRobo(ETRobo robo) {
    	canvas.setAvatar(robo.getMainA3());
    	this.addKeyListener(robo.prog);
    	canvas.addKeyListener(robo.prog);
    }
}
