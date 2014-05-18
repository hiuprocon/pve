/*
 * This enum represents mods of the car (Sample2).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S2Mode {
    GO_TO_WAITING_POINT,
    WAIT_UNTIL_MESSAGE,
    GO_TO_SWITCH,
    BACK_TO_WAITING_POINT
}

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class ArrivalWaitingPointEvent extends Event {}
class ArrivalSwitchEvent extends Event {}

/*
 * Sample2 is a program which controls the blue car in the
 * simulation environment. This car operates the elevator
 * on demand of the red car. Basic functions are implemented
 * in the SampleBase class which is extended by this Sample2 class.
 */
public class Sample2 extends SampleBase {
    // Location of waiting point.
    static final Vector waitingPoint = new Vector(0,0,30);

    // The mode of this car
    S2Mode mode;

    /*
     * The constructor of Sample2. super(20000) means
     * that the blue car is controled through port 20000
     * (computer networking).
     */
    public Sample2() {
        super(20000);
        mode = S2Mode.GO_TO_WAITING_POINT;
    }

    /*
     * Check the situations of this car and create some
     * events then call processEvent() method. To process
     * general events, this method call super.stateCheck()
     * at first.
     */
    @Override
    protected void stateCheck() { 
        super.stateCheck();
        Vector tmpV = new Vector();

        // car has arrived at the waiting point?
        tmpV.sub(waitingPoint,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalWaitingPointEvent());

        // car has arrived at the switch?
        tmpV.sub(switch1,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalSwitchEvent());
    }

    /*
     * Decide the next mode in consideration of the previous
     * mode and the given event. The process is based on FSM
     * (finite state machine). This method implements a strategy
     * of this car.
     */
    @Override
    protected void processEvent(Event e) {
        if ((mode==S2Mode.GO_TO_WAITING_POINT)
          &&(e instanceof ArrivalWaitingPointEvent)) {
            mode = S2Mode.WAIT_UNTIL_MESSAGE;
        } else if ((mode==S2Mode.GO_TO_SWITCH)
          &&(e instanceof ArrivalSwitchEvent)) {
            mode = S2Mode.WAIT_UNTIL_MESSAGE;
        } else if ((mode==S2Mode.WAIT_UNTIL_MESSAGE)
                 &&(e instanceof MessageEvent)) {
            String message = ((MessageEvent)e).message;
System.out.println("Sample2:reciveMessage: "+message);
            if (message.equals("wait")) {
                mode = S2Mode.BACK_TO_WAITING_POINT;
            } else if (message.equals("pushSwitch")) {
                mode = S2Mode.GO_TO_SWITCH;
            }
        } else if ((mode==S2Mode.BACK_TO_WAITING_POINT)
                 &&(e instanceof ArrivalWaitingPointEvent)) {
            mode = S2Mode.WAIT_UNTIL_MESSAGE;
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    /*
     * Control the car in accordance with the mode of the car.
     */
    @Override
    protected void move() {
        switch(mode) {
        case GO_TO_WAITING_POINT: goToWaitingPoint(); break;
        case WAIT_UNTIL_MESSAGE: waitUntilMessage(); break;
        case GO_TO_SWITCH: goToSwitch(); break;
        case BACK_TO_WAITING_POINT: backToWaitingPoint(); break;
        default: System.out.println("Sample2: Unknown mode?"+mode);
        }
    }

    // The following methods implement processes for each mode.

    void goToWaitingPoint() {
        goToDestination(waitingPoint);
    }
    void waitUntilMessage() {
        stopCar();
    }
    void goToSwitch() {
        goToDestination(switch1);
    }

    void backToWaitingPoint() {
        backToDestination(waitingPoint);
    }

    /*
     * The start point of Sample2.
     */
    public static void main(String args[]) {
        Sample2 s2 = new Sample2();
        s2.start();
    }
}
