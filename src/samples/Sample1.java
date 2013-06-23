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
    END
}

class DetermineTargetJewelEvent extends Event {}
class HoldingJewelEvent extends Event {}
class NotHoldingJewelEvent extends Event {}
class DetermineViaPointEvent extends Event {}
class ArrivalViaPointEvent extends Event {}
class ArrivalElevatorBottomEvent extends Event {}
class ArrivalElevatorTopEvent extends Event {}
class ArrivalGoalEvent extends Event {}

/*
 * Sample1
 */
public class Sample1 extends SampleBase {
    static final Vector[] viaPoints1 = {
        new Vector( 30,0, 0),
        new Vector( 25,0,-5),
        new Vector( 25,0, 5)
    };
    static final Vector[] viaPoints2 = {
        new Vector(-30,0, 0),
        new Vector(-25,0, 5),
        new Vector(-25,0,-5),
    };

    S1Mode mode;
    String targetJewel;
    Vector targetJewelLoc;
    Vector targetViaPoint;
    Vector targetGoal;

    public Sample1() {
        super(10000);
        mode = S1Mode.DETERMINE_TARGET_JEWEL;
        targetJewel = null;
        targetViaPoint = null;
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
            tmpV.sub(targetJewelLoc,loc);
            if (tmpV.length()>1.5) {
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
        if (targetViaPoint!=null) {
            tmpV.sub(targetViaPoint,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalViaPointEvent());
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
                 &&(e instanceof ArrivalViaPointEvent)) {
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
        } else if ((mode==S1Mode.WAIT_UNTIL_BOTTOM)
                 &&(e instanceof ArrivalElevatorBottomEvent)) {
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
System.out.println("Sample1:mode="+mode);
        switch(mode) {
        case DETERMINE_TARGET_JEWEL: determineTargetJewel(); break;
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
        case DETERMINE_WITCH_VIA_POINT: determineWitchViaPoint(); break;
        case GO_TO_VIA_POINT: goToViaPoint(); break;
        case GET_ON_ELEVATOR: getOnElevator(); break;
        case WAIT_UNTIL_TOP: waitUntilTop(); break;
        case GO_TO_GOAL: goToGoal(); break;
        case BACK_TO_ELEVATOR_TOP: backToElevatorTop(); break;
        case WAIT_UNTIL_BOTTOM: waitUntilBottom(); break;
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
        goToDestination(targetJewelLoc);
    }

    void determineWitchViaPoint() {
        Vector tmpV = new Vector();
        double min = Double.MAX_VALUE;
        for (Vector v : viaPoints1) {
            tmpV.sub(v,loc);
            double l = tmpV.length();
            if (l<min) {
                min = l;
                targetViaPoint = v;
                targetGoal = goal1;
            }
        }
        for (Vector v : viaPoints2) {
            tmpV.sub(v,loc);
            double l = tmpV.length();
            if (l<min) {
                min = l;
                targetViaPoint = v;
                targetGoal = goal2;
            }
        }
        processEvent(new DetermineViaPointEvent());
    }

    void goToViaPoint() {
        goToDestinationWithJewel(targetViaPoint);
        //goToDestination(targetViaPoint);
    }

    void getOnElevator() {
        goToDestinationWithJewel(elevatorBottom);
        //goToDestination(elevatoButtomr);
    }

    void waitUntilTop() {
        socket.send("drive 0 0");
    }

    void goToGoal() {
        goToDestinationWithJewel(targetGoal);
        //goToDestination(targetGoal);
    }

    void backToElevatorTop() {
        backToDestination(elevatorTop);
    }

    void waitUntilBottom() {
        socket.send("drive 0 0");
    }

    void end() {
        socket.send("drive 0 0");
    }

    public static void main(String args[]) {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
