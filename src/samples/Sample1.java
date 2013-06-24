package samples;

enum S1Mode {
    DETERMINE_TARGET_JEWEL,
    GO_TO_TARGET_JEWEL,
    DETERMINE_WITCH_VIA_POINT,
    GO_TO_VIA_POINT,
    GET_ON_ELEVATOR,
    WAIT_UNTIL_TOP,
    GO_TO_GOAL,
    BACK_TO_ELEVATOR_TOP,
    WAIT_UNTIL_BOTTOM,
    GO_TO_VIA_POINT2,
    END
}

class DetermineTargetJewelEvent extends Event {}
class HoldingJewelEvent extends Event {}
class NotHoldingJewelEvent extends Event {}
class DetermineViaPointEvent extends Event {}
class ArrivalViaPoint1Event extends Event {}
class ArrivalElevatorBottomEvent extends Event {}
class ArrivalElevatorTopEvent extends Event {}
class ArrivalGoalEvent extends Event {}
class ArrivalViaPoint2Event extends Event {}

/*
 * Sample1
 */
public class Sample1 extends SampleBase {
    static final Vector viaPointA = new Vector( 30,0, 0);
    static final Vector viaPointB = new Vector(-30,0, 0);

    S1Mode mode;
    String targetJewel;
    Vector targetJewelLoc;
    Vector targetViaPoint1;
    Vector targetViaPoint2;
    Vector targetGoal;

    public Sample1() {
        super(10000);
        mode = S1Mode.DETERMINE_TARGET_JEWEL;
        targetJewel = null;
        targetViaPoint1 = null;
        targetViaPoint2 = null;
        targetGoal = null;
    }

    @Override
    protected void stateCheck() {
        super.stateCheck();
        Vector tmpV = new Vector();

        // the car holds the jewel?
        if (targetJewel!=null) {
            targetJewelLoc = jewelSet.get(targetJewel);
            boolean hold = true;
            if (targetJewelLoc==null) {
                hold = false;
            } else {
                tmpV.sub(targetJewelLoc,loc);
                if (tmpV.length()>1.5) {
                    hold = false;
                } else {
                    tmpV.normalize();
                    if (tmpV.dot(front)<0.6)
                        hold = false;
                }
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

    // This method implements a Finite Automaton
    @Override
    protected void processEvent(Event e) {
//System.out.println(e.getClass().getName());
        String s;
        if ((mode==S1Mode.DETERMINE_TARGET_JEWEL)
          &&(e instanceof DetermineTargetJewelEvent)) {
            mode = S1Mode.GO_TO_TARGET_JEWEL;
        } else if ((mode==S1Mode.GO_TO_TARGET_JEWEL)
                 &&(e instanceof HoldingJewelEvent)) {
            mode = S1Mode.DETERMINE_WITCH_VIA_POINT;
            s = socket.send("sendMessage wait");
System.out.println("Sample1:sendMessage(wait):"+s);
        } else if ((mode==S1Mode.DETERMINE_WITCH_VIA_POINT)
                 &&(e instanceof DetermineViaPointEvent)) {
            mode = S1Mode.GO_TO_VIA_POINT;
        } else if ((mode==S1Mode.GO_TO_VIA_POINT)
                 &&(e instanceof ArrivalViaPoint1Event)) {
            mode = S1Mode.GET_ON_ELEVATOR;
        } else if ((mode==S1Mode.GET_ON_ELEVATOR)
                 &&(e instanceof ArrivalElevatorBottomEvent)) {
            mode = S1Mode.WAIT_UNTIL_TOP;
            s = socket.send("sendMessage pushSwitch");
System.out.println("Sample1:sendMessage(pushSwitch):"+s);
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
            mode = S1Mode.DETERMINE_TARGET_JEWEL;
        } else if ((true)&&(e instanceof MessageEvent)) {
            String message = ((MessageEvent)e).message;
            System.out.println("Sample1: Message received?: "+message);
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    @Override
    protected void move() {
//System.out.println("Sample1:mode="+mode);
        switch(mode) {
        case DETERMINE_TARGET_JEWEL: determineTargetJewel(); break;
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
        case DETERMINE_WITCH_VIA_POINT: determineWitchViaPoint(); break;
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

    void determineTargetJewel() {
        targetJewel = jewelSet.getNearest(loc);
        //if (targetJewel!=null)...
        processEvent(new DetermineTargetJewelEvent());
    }

    void goToTargetJewel() {
        if (targetJewelLoc!=null)
            goToDestination(targetJewelLoc);
    }

    void determineWitchViaPoint() {
        if (loc.x>0.0) {
            targetViaPoint1 = viaPointA;
            targetGoal = goal1;
            targetViaPoint2 = viaPointB;
        } else {
            targetViaPoint1 = viaPointB;
            targetGoal = goal2;
            targetViaPoint2 = viaPointA;
        }
        processEvent(new DetermineViaPointEvent());
    }

    void goToViaPoint1() {
        goToDestinationWithJewel(targetViaPoint1);
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

    public static void main(String args[]) {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
