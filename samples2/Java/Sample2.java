/*
 * This enum represents mods of the car (Sample2).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S2Mode {
    DEVELOP_STRATEGY1,
    GO_TO_TARGET_JEWEL,
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
 * simulation environment. This car carries all jewels.
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
    String targetJewel;
    Vector targetJewelLoc;
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
        targetJewel = null;
        targetJewelLoc = null;
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

        // the car holds the jewel?
        if (targetJewel!=null)
            targetJewelLoc = jewelSet.get(targetJewel);
        if (targetJewelLoc!=null) {
            boolean hold = true;
            tmpV.sub(targetJewelLoc,loc);
            if (tmpV.length()>2.0) {
                hold = false;
            } else {
                tmpV.normalize();
                if (tmpV.dot(front)<0.6)
                    hold = false;
            }
            if (hold==true)
                processEvent(new HoldingJewelEvent());
            else
                processEvent(new NotHoldingJewelEvent());
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
            mode = S2Mode.GO_TO_TARGET_JEWEL;
        } else if ((mode==S2Mode.GO_TO_TARGET_JEWEL)
                 &&(e instanceof HoldingJewelEvent)) {
            mode = S2Mode.DEVELOP_STRATEGY2;
        } else if ((mode==S2Mode.DEVELOP_STRATEGY2)
                 &&(e instanceof StrategyDevelopedEvent)) {
            mode = S2Mode.GO_TO_VIA_POINT;
        } else if ((mode==S2Mode.GO_TO_VIA_POINT)
                &&(e instanceof ArrivalViaPoint1Event)) {
           mode = S2Mode.GET_ON_ELEVATOR;
        } else if ((mode==S2Mode.GO_TO_VIA_POINT)
                &&(e instanceof NotHoldingJewelEvent)) {
           mode = S2Mode.DEVELOP_STRATEGY1;
        } else if ((mode==S2Mode.GET_ON_ELEVATOR)
                &&(e instanceof ArrivalElevatorBottomEvent)) {
           mode = S2Mode.BACK_TO_VIA_POINT2;
           s = socket.send("sendMessage READY");
System.out.println("Sample2:sendMessage(READY):"+s);
        } else if ((mode==S2Mode.GET_ON_ELEVATOR)
                &&(e instanceof NotHoldingJewelEvent)) {
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
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
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
        targetJewel = jewelSet.getNearest(loc);
        targetJewelLoc = jewelSet.get(targetJewel);
        //if (targetJewelLoc!=null)...
        processEvent(new StrategyDevelopedEvent());
    }

    void goToTargetJewel() {
        if (targetJewelLoc!=null) {
            if (checkAllConflict(loc,targetJewelLoc,null)) {
System.out.println("GAHA:CONFLICT1");
                Vector v = new Vector(targetJewelLoc);
                v.sub(loc);
                v = Vector.simpleRotateY(45,v);
                v.add(loc);
                goToDestination(v);
            } else {
                goToDestination(targetJewelLoc);
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
        if (checkAllConflict(loc,targetViaPoint1,targetJewelLoc)) {
System.out.println("GAHA:CONFLICT2");
            Vector v = new Vector(targetViaPoint1);
            v.sub(loc);
            v = Vector.simpleRotateY(45,v);
            v.add(loc);
            goToDestinationWithJewel(v);
        } else {
            goToDestinationWithJewel(targetViaPoint1);
        }
    }

    void getOnElevator() {
        goToDestinationWithJewel(elevatorBottom);
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
