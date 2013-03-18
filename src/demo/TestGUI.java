package demo;

import javax.swing.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

public class TestGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	A3Canvas canvas;
    public TestGUI(A3CanvasInterface c) {
    	super("Test");
    	canvas = (A3Canvas)c;
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	canvas.setCameraLocImmediately(0,5,20);
    	canvas.setCameraLookAtPointImmediately(0,0,0);
    	Vector3d lookAt = new Vector3d(0,0,6);
    	Vector3d camera = new Vector3d(0,3,-6);
    	Vector3d up = new Vector3d(0,1,0);
    	canvas.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,lookAt,camera,up,1.0);
    	canvas.setSize(500,300);
    	add(canvas);
    	pack();
    	setVisible(true);
    }
    public void setCar(KeyboardForkLift forkLift) {
    	canvas.setAvatar(forkLift.getMainA3());
    	this.addKeyListener(forkLift);
    	canvas.addKeyListener(forkLift);
    }
}
