package samples;

/*
 *   SampleBase is a super class of Sample1 and Sample2.
 * Sample1 and Sample2 have common functions. This SampleBase class
 * implements these common functions. At the first part of
 * this source file, location data of static objects,
 * general informations of the car are defined. At the end of 
 * this source file, methods are defined which control the
 * car in various situations.
 *   The point of this sample program is that the selection of
 * a car mode is based on "Finite State Machine (so called FSM)".
 * In an infinite loop, two methods are called repeatedly.
 * The methods are stateCheck() method and move() method.
 * In stateCheck() method, some events are created according to
 * the situation of the car. Then these events are passed to
 * processEvent() method. This triggers mode changes of
 * the car. move() method controls the car in accordance with
 * the mode of the car.
 */
public abstract class SampleBase {
    // Simulation step time
    public static final double dt = 1.0/30.0;
    // Location of the elevator bottom
    public static final Vector elevatorBottom = new Vector(0,1.0,0);
    // Location of the elevator top
    public static final Vector elevatorTop = new Vector(0,15.0,0);
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

    /*
     * The constructor of SampleBase.
     */
    protected SampleBase(int port) {
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

    /*
     * Main loop of this program.
     */
    protected void start() {
        while (true) {
            stateCheck();
            move();
            socket.send("stepForward");
            currentTime += dt;
            counter++;
        }
    }

    /*
     * Check current situations. In subclasses of SampleBase,
     * this method could be overrided to check another situations
     * and/or create some events then call processEvent() method.
     */
    protected void stateCheck() {
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
        if (jewelSet.size()==0)
            processEvent(new ClearedEvent());
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

    /*
     * Decide the next mode in consideration of the previous
     * mode and the given event. The process is based on FSM
     * (finite state machine). In subclasses of this SampleBase,
     * this method should be implemented suitably to achieve
     * the purpose of each subclasses. And the mode is defined at
     * each subclasses.
     */
    protected abstract void processEvent(Event e);

    /*
     * This method should be implemented to control
     * the car in accordance with the mode of the car.
     * As a typical implementation, this method calls
     * appropriate methods using switch-case statement
     * with the mode of the car.
     */
    protected abstract void move();

    /*
     * Drive this car to the given location (v).
     */
    protected void goToDestination(Vector v) {
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

    /*
     * Drive this car to the given location (v) with jewels.
     */
    protected void goToDestinationWithJewel(Vector v) {
        double power = 0.0;
        double steering = 0.0;

        Vector tmpV = new Vector();
        tmpV.sub(v,loc);
        tmpV.normalize();

        if (tmpV.dot(front)<0.0)
            steering = 3.0;
        else
            steering = -3.0 * tmpV.dot(left);
        power = 0.5;//1.0 * tmpV.dot(front);

        socket.send("drive "+power+" "+steering);
    }

    /*
     * Back this car to the given location (v).
     */
    protected void backToDestination(Vector v) {
        double power = 0.0;
        double steering = 0.0;

        Vector tmpV = new Vector();
        tmpV.sub(v,loc);
        tmpV.normalize();

        if (tmpV.dot(front)>0.0)
            steering = 3.0;
        else
            steering = 3.0 * tmpV.dot(left);
        if (Math.abs(steering)<0.1)
            power = 0.3 * tmpV.dot(front);

        socket.send("drive "+power+" "+steering);
    }

    /*
     * Stop this car!
     */
    protected void stopCar() {
        socket.send("drive 0 0");
    }
}
