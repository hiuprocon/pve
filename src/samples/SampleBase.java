package samples;

import java.util.HashMap;
import java.util.ArrayList;

/*
 * SampleBase
 */
public abstract class SampleBase {
    // Simulation step time
    public static final double dt = 1.0/30.0;
    // Location of the elevator
    public static final Vector elevator = new Vector(0,0.5,0);
    // Location of the switch1
    public static final Vector switch1 = new Vector(0,0.5,-11);
    // Location of the switch2
    public static final Vector switch2 = new Vector(0,0.5,11);
    // Location of the goal1
    public static final Vector goal1 = new Vector(-10,15.5,0);
    // Location of the goal2
    public static final Vector goal2 = new Vector( 10,15.5,0);

    // current simulation time
    protected double currentTime;
    // counter of simulation step
    protected int counter;
    // socket
    protected MySocket socket;
    // Location of this car
    protected Vector loc;
    // Rotation of this car
    protected Vector rot;
    // Front unit vector of this car
    protected Vector front;
    // Left unit vector of this car
    protected Vector left;
    // Old location of this car
    protected Vector oldLoc;
    // Velocity of this car
    protected Vector vel;
    // Manager of jewels
    protected JewelSet jewelSet;

    protected SampleBase(int port) throws Exception {
        currentTime = 0.0;
        counter = 0;
        socket = new MySocket(port);
        loc = new Vector();
        rot = new Vector();
        front = new Vector();
        left = new Vector();
        oldLoc = new Vector();
        vel = new Vector();
        jewelSet = new JewelSet();
    }

    protected void start() throws Exception {
        while (true) {
            stateCheck();
            move();
            socket.send("stepForward");
            currentTime += dt;
            counter++;
        }
    }

    // Check current status
    protected void stateCheck() throws Exception {
        oldLoc.set(loc);
        String ret = socket.send("getLoc");
        loc.set(ret);
        ret = socket.send("getRev");
        rot.set(ret);
        front = Vector.rotate(rot, new Vector(0,0,1));
        //front = Vector.simpleRotateY(rot.y, new Vector(0,0,1));
        left = Vector.rotate(rot, new Vector(1,0,0));
        //left = Vector.simpleRotateY(rot.y, new Vector(1,0,0));
        vel.sub(loc,oldLoc);
        vel.scale(1.0/dt);
        ret = socket.send("searchJewels");
        jewelSet.load(ret);
        ret = socket.send("receiveMessages");
        if (ret.length()>9) {
            ret = ret.substring(10);
            String[] ss = ret.split(",");
            for (String s:ss) {
                MessageEvent me = new MessageEvent(s);
                processEvent(me);
            }
        }
    }

    protected abstract void processEvent(Event e);
    protected abstract void move() throws Exception;

    protected void goToDestination(Vector v) throws Exception {
        double power = 0.0;
        double steering = 0.0;

        Vector tmpV = new Vector();
        tmpV.sub(v,loc);
        tmpV.normalize();

        if (tmpV.dot(front)<0.0)
            steering = 3.0;
        else
            steering = -3.0 * tmpV.dot(left);
        if (Math.abs(steering)<0.1)
            power = 1.0 * tmpV.dot(front);

        socket.send("drive "+power+" "+steering);
    }

    protected void goToDestinationWithJewel(Vector v) throws Exception {
        double power = 0.0;
        double steering = 0.0;

        Vector tmpV = new Vector();
        tmpV.sub(v,loc);
        tmpV.normalize();

        if (tmpV.dot(front)<0.0)
            steering = 1.0;
        else
            steering = -1.0 * tmpV.dot(left);
        power = 1.0 * tmpV.dot(front);

        socket.send("drive "+power+" "+steering);
    }
}
