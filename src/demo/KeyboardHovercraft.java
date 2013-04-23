package demo;

import java.awt.event.*;
import com.github.hiuprocon.pve.obj.Hovercraft;

public class KeyboardHovercraft extends Hovercraft implements KeyListener {
    boolean keyUp;
    boolean keyDown;
    boolean keyRight;
    boolean keyLeft;

	public KeyboardHovercraft() {
		super();
	}

    @Override
    public void keyPressed(KeyEvent ke) {
        switch(ke.getKeyCode()) {
        case KeyEvent.VK_UP:    keyUp=true;    break;
        case KeyEvent.VK_DOWN:  keyDown=true;  break;
        case KeyEvent.VK_RIGHT: keyRight=true; break;
        case KeyEvent.VK_LEFT:  keyLeft=true;  break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch(ke.getKeyCode()) {
        case KeyEvent.VK_UP:    keyUp=false;    break;
        case KeyEvent.VK_DOWN:  keyDown=false;  break;
        case KeyEvent.VK_RIGHT: keyRight=false; break;
        case KeyEvent.VK_LEFT:  keyLeft=false;  break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {;}

	@Override
	protected void postSimulation() {
        double engine = 0.0;
        double steering = 0.0;

        if (keyUp) engine += 15.0;
        if (keyDown) engine -= 15.0;

        if (keyRight) steering -= 0.5;
        if (keyLeft)  steering += 0.5;

        drive(engine,steering);
	}
}
