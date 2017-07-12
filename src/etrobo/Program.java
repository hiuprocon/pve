package etrobo;

import java.awt.event.*;

public abstract class Program implements KeyListener {
    private SimEnv env = null;
    protected boolean keyUp = false;
    protected boolean keyDown = false;
    protected boolean keyRight = false;
    protected boolean keyLeft = false;
    protected boolean keyZ = false;
    protected double brightness;//0-100
    protected double distance;//センチメートル
    protected int rightWheelCount;//deg
    protected int leftWheelCount;//deg
    protected int tailCount;//deg
    protected double angleVelocity;//deg/sec

    public void setSimEnv(SimEnv env) {
        this.env = env;
    }

    public void drive(double right,double left,double tail) {
        if (env!=null && env.robo!=null) {
            env.robo.drive(right,left,tail);
        } else {
            System.out.println("GAHA1!");
        }
    }

    public abstract void exec4ms();

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
    public void keyTyped(KeyEvent ke) {;}
}
