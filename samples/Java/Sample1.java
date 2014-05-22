/*
 * This enum represents mods of the car (Sample1).
 * These modes correspond to states of FSM (Finite State Machine).
 */
enum S1Mode {
    DEVELOP_STRATEGY1,
    GO_TO_TARGET_JEWEL,
    DEVELOP_STRATEGY2,
    GO_TO_VIA_POINT,
    GET_ON_ELEVATOR,
    WAIT_UNTIL_TOP,
    GO_TO_GOAL,
    BACK_TO_ELEVATOR_TOP,
    WAIT_UNTIL_BOTTOM,
    GO_TO_VIA_POINT2,
    END
}

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */
class StrategyDevelopedEvent extends Event {}
class HoldingJewelEvent extends Event {}
class NotHoldingJewelEvent extends Event {}
class ArrivalViaPoint1Event extends Event {}
class ArrivalElevatorBottomEvent extends Event {}
class ArrivalElevatorTopEvent extends Event {}
class ArrivalGoalEvent extends Event {}
class ArrivalViaPoint2Event extends Event {}

/*
 * Sample1 is a program which controls the red car in the
 * simulation environment. This car carries all jewels.
 * Basic functions are implemented in the SampleBase class
 * which is extended by this Sample1 class.
 */
public class Sample1 extends SampleBase {
    //For convenience, this car goes by way of via points.
    static final Vector viaPointA = new Vector( 30,0, 62.5);
    static final Vector viaPointB = new Vector(-30,0, 62.5);

    // The mode of this car.
    S1Mode mode;
    // The following variables are targets.
    String targetJewel;
    Vector targetJewelLoc;
    Vector targetViaPoint1;
    Vector targetViaPoint2;
    Vector targetGoal;


    /*
     * The constructor of Sample1. super(10000) means
     * that the red car is controled through port 10000
     * (computer networking).
     */
    public Sample1() {
        super(10000);
        mode = S1Mode.DEVELOP_STRATEGY1;
        targetJewel = null;
        targetJewelLoc = null;
        targetViaPoint1 = null;
        targetViaPoint2 = null;
        targetGoal = null;
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

        // car has arrived at the elevator top?
        tmpV.sub(elevatorTop,loc);
        if (tmpV.length()<1.0)
            processEvent(new ArrivalElevatorTopEvent());

        // car has arrived at the goal?
        if (targetGoal!=null) {
            tmpV.sub(targetGoal,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalGoalEvent());
        }

        // car has arrived at the via point?
        if (targetViaPoint2!=null) {
            tmpV.sub(targetViaPoint2,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalViaPoint2Event());
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
        if ((mode==S1Mode.DEVELOP_STRATEGY1)
          &&(e instanceof StrategyDevelopedEvent)) {
            mode = S1Mode.GO_TO_TARGET_JEWEL;
        } else if ((mode==S1Mode.GO_TO_TARGET_JEWEL)
                 &&(e instanceof HoldingJewelEvent)) {
            mode = S1Mode.DEVELOP_STRATEGY2;
        } else if ((mode==S1Mode.DEVELOP_STRATEGY2)
                 &&(e instanceof StrategyDevelopedEvent)) {
            mode = S1Mode.GO_TO_VIA_POINT;
        } else if ((mode==S1Mode.GO_TO_VIA_POINT)
                &&(e instanceof ArrivalViaPoint1Event)) {
           mode = S1Mode.GET_ON_ELEVATOR;
        } else if ((mode==S1Mode.GO_TO_VIA_POINT)
                &&(e instanceof NotHoldingJewelEvent)) {
           mode = S1Mode.DEVELOP_STRATEGY1;
        } else if ((mode==S1Mode.GET_ON_ELEVATOR)
                &&(e instanceof ArrivalElevatorBottomEvent)) {
           mode = S1Mode.WAIT_UNTIL_TOP;
           s = socket.send("sendMessage pushSwitch");
System.out.println("Sample1:sendMessage(pushSwitch):"+s);
        } else if ((mode==S1Mode.GET_ON_ELEVATOR)
                &&(e instanceof NotHoldingJewelEvent)) {
           mode = S1Mode.DEVELOP_STRATEGY1;
        } else if ((mode==S1Mode.WAIT_UNTIL_TOP)
                 &&(e instanceof ArrivalElevatorTopEvent)) {
            mode = S1Mode.GO_TO_GOAL;
        } else if ((mode==S1Mode.GO_TO_GOAL)
                 &&(e instanceof ArrivalGoalEvent)) {
            mode = S1Mode.BACK_TO_ELEVATOR_TOP;
        } else if ((mode==S1Mode.BACK_TO_ELEVATOR_TOP)
                 &&(e instanceof ArrivalElevatorTopEvent)) {
            mode = S1Mode.WAIT_UNTIL_BOTTOM;
            s = socket.send("sendMessage wait");
System.out.println("Sample1:sendMessage(wait):"+s);
        } else if ((mode==S1Mode.BACK_TO_ELEVATOR_TOP)
                 &&(e instanceof ClearedEvent)) {
            mode = S1Mode.END;
        } else if ((mode==S1Mode.WAIT_UNTIL_BOTTOM)
                 &&(e instanceof ArrivalElevatorBottomEvent)) {
            mode = S1Mode.GO_TO_VIA_POINT2;
        } else if ((mode==S1Mode.GO_TO_VIA_POINT2)
                 &&(e instanceof ArrivalViaPoint2Event)) {
            mode = S1Mode.DEVELOP_STRATEGY1;
        } else if ((true)&&(e instanceof MessageEvent)) {
            String message = ((MessageEvent)e).message;
            System.out.println("Sample1: Message received?: "+message);
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    /*
     * Control the car in accordance with the mode of the car.
     */
    @Override
    protected void move() {
//System.out.println("Sample1:mode="+mode);
        switch(mode) {
        case DEVELOP_STRATEGY1: developStrategy1(); break;
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
        case DEVELOP_STRATEGY2: developStrategy2(); break;
        case GO_TO_VIA_POINT: goToViaPoint1(); break;
        case GET_ON_ELEVATOR: getOnElevator(); break;
        case WAIT_UNTIL_TOP: waitUntilTop(); break;
        case GO_TO_GOAL: goToGoal(); break;
        case BACK_TO_ELEVATOR_TOP: backToElevatorTop(); break;
        case WAIT_UNTIL_BOTTOM: waitUntilBottom(); break;
        case GO_TO_VIA_POINT2: goToViaPoint2(); break;
        case END: end(); break;
        default: System.out.println("Sample1: Unknown mode?"+mode);
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
            targetGoal = goal1;
            targetViaPoint2 = viaPointB;
        } else {
            targetViaPoint1 = viaPointB;
            targetGoal = goal2;
            targetViaPoint2 = viaPointA;
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
            goToDestination(v);
        } else {
            goToDestinationWithJewel(targetViaPoint1);
        }
    }

    void getOnElevator() {
        goToDestinationWithJewel(elevatorBottom);
    }

    void waitUntilTop() {
        stopCar();
    }

    void goToGoal() {
        goToDestinationWithJewel(targetGoal);
    }

    void backToElevatorTop() {
        backToDestination(elevatorTop);
    }

    void waitUntilBottom() {
        stopCar();
    }

    void goToViaPoint2() {
        goToDestination(targetViaPoint2);
    }

    void end() {
        stopCar();
    }

    /*
     * The start point of Sample1.
     */
    public static void main(String args[]) {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
