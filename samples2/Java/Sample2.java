/*
 * This enum represents mods of the car (Sample2).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S2Mode {
    DEVELOP_STRATEGY1,
    GO_TO_TARGET_BURDEN,
    DEVELOP_STRATEGY2,
    GO_TO_VIA_POINT,
    GET_ON_ELEVATOR,
    BACK_TO_VIA_POINT2,
    GO_TO_SWITCH,
    GO_TO_VIA_POINT3,
    END
}

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class ArrivalViaPoint3Event extends Event {}
class ArrivalSwitchEvent extends Event {}

/*
 * Sample2 is a program which controls the blue car in the
 * simulation environment. This car carries all burdens.
 * Basic functions are implemented in the SampleBase class
 * which is extended by this Sample2 class.
 */
public class Sample2 extends SampleBase {
    //For convenience, this car goes by way of via points.
    static final Vector viaPointA = new Vector( 30,0, 62.5);
    static final Vector viaPointB = new Vector(-30,0, 62.5);

    // The mode of this car.
    S2Mode mode;
    // The following variables are targets.
    String targetBurden;
    Vector targetBurdenLoc;
    Vector targetViaPoint1;
    Vector targetViaPoint2;
    Vector targetViaPoint3;
    String lastMessage;

    /*
     * The constructor of Sample2. super(20000) means
     * that the blue car is controled through port 20000
     * (computer networking).
     */
    public Sample2() {
        super(20000);
        mode = S2Mode.DEVELOP_STRATEGY1;
        targetBurden = null;
        targetBurdenLoc = null;
        targetViaPoint1 = null;
        targetViaPoint2 = null;
        targetViaPoint3 = null;
        lastMessage = "dummy";
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

        // the car holds the burden?
        if (targetBurden!=null)
            targetBurdenLoc = burdenSet.get(targetBurden);
        if (targetBurdenLoc!=null) {
            boolean hold = true;
            tmpV.sub(targetBurdenLoc,loc);
            if (tmpV.length()>2.0) {
                hold = false;
            } else {
                tmpV.normalize();
                if (tmpV.dot(front)<0.6)
                    hold = false;
            }
            if (hold==true)
                processEvent(new HoldingBurdenEvent());
            else
                processEvent(new NotHoldingBurdenEvent());
        }

        // car has arrived at the via point?
        if (targetViaPoint1!=null) {
            tmpV.sub(targetViaPoint1,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalViaPoint1Event());
        }

        // car has arrived at the elevator bottom?
        tmpV.sub(elevatorBottom,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalElevatorBottomEvent());

        // car has arrived at the via point2?
        if (targetViaPoint2!=null) {
            tmpV.sub(targetViaPoint2,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalViaPoint2Event());
        }

        // car has arrived at the switch?
        tmpV.sub(switch1,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalSwitchEvent());

        // car has arrived at the via point3?
        if (targetViaPoint3!=null) {
            tmpV.sub(targetViaPoint3,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalViaPoint3Event());
        }
    }

    /*
     * Decide the next mode in consideration of the previous
     * mode and the given event. The process is based on FSM
     * (finite state machine). This method implements a strategy
     * of this car.
     */
    @Override
    protected void processEvent(Event e) {
        String s;
        if ((mode==S2Mode.DEVELOP_STRATEGY1)
          &&(e instanceof StrategyDevelopedEvent)) {
            mode = S2Mode.GO_TO_TARGET_BURDEN;
        } else if ((mode==S2Mode.GO_TO_TARGET_BURDEN)
                 &&(e instanceof HoldingBurdenEvent)) {
            mode = S2Mode.DEVELOP_STRATEGY2;
        } else if ((mode==S2Mode.DEVELOP_STRATEGY2)
                 &&(e instanceof StrategyDevelopedEvent)) {
            mode = S2Mode.GO_TO_VIA_POINT;
        } else if ((mode==S2Mode.GO_TO_VIA_POINT)
                &&(e instanceof ArrivalViaPoint1Event)) {
           mode = S2Mode.GET_ON_ELEVATOR;
        } else if ((mode==S2Mode.GO_TO_VIA_POINT)
                &&(e instanceof NotHoldingBurdenEvent)) {
           mode = S2Mode.DEVELOP_STRATEGY1;
        } else if ((mode==S2Mode.GET_ON_ELEVATOR)
                &&(e instanceof ArrivalElevatorBottomEvent)) {
           mode = S2Mode.BACK_TO_VIA_POINT2;
           s = socket.send("sendMessage READY");
System.out.println("Sample2:sendMessage(READY):"+s);
        } else if ((mode==S2Mode.GET_ON_ELEVATOR)
                &&(e instanceof NotHoldingBurdenEvent)) {
           mode = S2Mode.DEVELOP_STRATEGY1;
        } else if ((mode==S2Mode.BACK_TO_VIA_POINT2)
                 &&(e instanceof ArrivalViaPoint2Event)) {
           mode = S2Mode.GO_TO_SWITCH;
        } else if ((mode==S2Mode.GO_TO_SWITCH)
                 &&(e instanceof ArrivalSwitchEvent)) {
           mode = S2Mode.GO_TO_VIA_POINT3;
           s = socket.send("sendMessage NOT_READY");
System.out.println("Sample2:sendMessage(NOT_READY):"+s);
        } else if ((mode==S2Mode.GO_TO_VIA_POINT3)
                 &&(e instanceof ArrivalViaPoint3Event)) {
           mode = S2Mode.DEVELOP_STRATEGY1;
        } else if ((true)&&(e instanceof MessageEvent)) {
           lastMessage = ((MessageEvent)e).message;
           System.out.println("Sample2: Message received: "+lastMessage);
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    /*
     * Control the car in accordance with the mode of the car.
     */
    @Override
    protected void move() {
//System.out.println("Sample2:mode="+mode);
        switch(mode) {
        case DEVELOP_STRATEGY1: developStrategy1(); break;
        case GO_TO_TARGET_BURDEN: goToTargetBurden(); break;
        case DEVELOP_STRATEGY2: developStrategy2(); break;
        case GO_TO_VIA_POINT: goToViaPoint1(); break;
        case GET_ON_ELEVATOR: getOnElevator(); break;
        case BACK_TO_VIA_POINT2: backToViaPoint2(); break;
        case GO_TO_SWITCH: goToSwitch(); break;
        case GO_TO_VIA_POINT3: goToViaPoint3(); break;
        case END: end(); break;
        default: System.out.println("Sample2: Unknown mode?"+mode);
        }
    }

    // The following methods implement processes for each mode.

    void developStrategy1() {
        targetBurden = burdenSet.getNearest(loc);
        targetBurdenLoc = burdenSet.get(targetBurden);
        //if (targetBurdenLoc!=null)...
        processEvent(new StrategyDevelopedEvent());
    }

    void goToTargetBurden() {
        if (targetBurdenLoc!=null) {
            if (checkAllConflict(loc,targetBurdenLoc,null)) {
System.out.println("GAHA:CONFLICT1");
                Vector v = new Vector(targetBurdenLoc);
                v.sub(loc);
                v = Vector.simpleRotateY(45,v);
                v.add(loc);
                goToDestination(v);
            } else {
                goToDestination(targetBurdenLoc);
            }
        }
    }

    void developStrategy2() {
        if (loc.x>0.0) {
            targetViaPoint1 = viaPointA;
            targetViaPoint2 = viaPointA;
            targetViaPoint3 = viaPointB;
        } else {
            targetViaPoint1 = viaPointB;
            targetViaPoint2 = viaPointB;
            targetViaPoint3 = viaPointA;
        }
        processEvent(new StrategyDevelopedEvent());
    }

    void goToViaPoint1() {
        if (checkAllConflict(loc,targetViaPoint1,targetBurdenLoc)) {
System.out.println("GAHA:CONFLICT2");
            Vector v = new Vector(targetViaPoint1);
            v.sub(loc);
            v = Vector.simpleRotateY(45,v);
            v.add(loc);
            goToDestinationWithBurden(v);
        } else {
            goToDestinationWithBurden(targetViaPoint1);
        }
    }

    void getOnElevator() {
        goToDestinationWithBurden(elevatorBottom);
    }

    void backToViaPoint2() {
        backToDestination(targetViaPoint2);
    }

    void goToSwitch() {
        if (lastMessage.equals("READY"))
            goToDestination(switch1);
        else
            stopCar();
    }

    void goToViaPoint3() {
        if (lastMessage.equals("GOAL"))
            goToDestination(targetViaPoint3);
        else
            stopCar();
    }

    void end() {
        stopCar();
    }

    /*
     * The start point of Sample2.
     */
    public static void main(String args[]) {
        Sample2 s2 = new Sample2();
        s2.start();
    }
}
