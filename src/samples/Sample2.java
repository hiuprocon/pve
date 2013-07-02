package samples;

enum S2Mode {
    GO_TO_WAITING_POINT,
    WAIT_UNTIL_MESSAGE,
    GO_TO_SWITCH
}

class ArrivalWaitingPointEvent extends Event {}
class ArrivalSwitchEvent extends Event {}

/*
 * Sample2
 */
public class Sample2 extends SampleBase {
    static final Vector waitingPoint = new Vector(0,0,20);

    S2Mode mode;
    Vector destination;

    public Sample2() {
        super(20000);
        destination = new Vector(waitingPoint);
        mode = S2Mode.GO_TO_WAITING_POINT;
    }

    @Override
    protected void stateCheck() {
        super.stateCheck();
        Vector tmpV = new Vector();

        // car has arrived at the waiting point?
        tmpV.sub(waitingPoint,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalWaitingPointEvent());

        // car has arrived at the switch?
        tmpV.sub(switch2,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalSwitchEvent());
    }

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
                mode = S2Mode.GO_TO_WAITING_POINT;
            } else if (message.equals("pushSwitch")) {
                mode = S2Mode.GO_TO_SWITCH;
            }
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    @Override
    protected void move() {
        switch(mode) {
        case GO_TO_WAITING_POINT: goToWaitingPoint(); break;
        case WAIT_UNTIL_MESSAGE: waitUntilMessage(); break;
        case GO_TO_SWITCH: goToSwitch(); break;
        default: System.out.println("Sample2: Unknown mode?"+mode);
        }
    }

    void goToWaitingPoint() {
        goToDestination(waitingPoint);
    }
    void waitUntilMessage() {
        stopCar();
    }
    void goToSwitch() {
        goToDestination(switch2);
    }

    public static void main(String args[]) {
        Sample2 s2 = new Sample2();
        s2.start();
    }
}
