package demo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.github.hiuprocon.pve.core.PVEWorld;
import com.github.hiuprocon.pve.obj.ForkLift;

public class KeyboardSimpleCar extends ForkLift implements KeyListener {
    boolean keyUp;
    boolean keyDown;
    boolean keyRight;
    boolean keyLeft;
    boolean keySpace;
    boolean keyShift;

	public KeyboardSimpleCar(PVEWorld world, String a3url) {
		super(world, a3url);
	}

    @Override
    public void keyPressed(KeyEvent ke) {
        switch(ke.getKeyCode()) {
        case KeyEvent.VK_UP:    keyUp=true;    break;
        case KeyEvent.VK_DOWN:  keyDown=true;  break;
        case KeyEvent.VK_RIGHT: keyRight=true; break;
        case KeyEvent.VK_LEFT:  keyLeft=true;  break;
        case KeyEvent.VK_SPACE: keySpace=true; break;
        case KeyEvent.VK_SHIFT: keyShift=true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        switch(ke.getKeyCode()) {
        case KeyEvent.VK_UP:    keyUp=false;    break;
        case KeyEvent.VK_DOWN:  keyDown=false;  break;
        case KeyEvent.VK_RIGHT: keyRight=false; break;
        case KeyEvent.VK_LEFT:  keyLeft=false;  break;
        case KeyEvent.VK_SPACE: keySpace=false; break;
        case KeyEvent.VK_SHIFT: keyShift=false; break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {;}

	@Override
	protected void postSimulation() {
        double engine = 0.0;
        double steering = 0.0;
        double breaking = 0.0;
        double drift = 0.0;

        if (keyUp) engine += 500.0;
        if (keyDown) engine -= 500.0;

        if (keyRight) steering -= 0.1;
        if (keyLeft)  steering += 0.1;

        if (keySpace) breaking = 10.0;

        if (keyShift) drift = 1.0;

        setForce(engine,steering,breaking,drift);
	}
}
