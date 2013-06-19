package samples;

enum S1Mode {
    DETERMINE_TARGET_JEWEL,
    GO_TO_TARGET_JEWEL,
    DETERMINE_WITCH_VIA_POINT,
    GO_TO_VIA_POINT,
    GET_ON_ELEVATOR,
    WAIT_UNTIL_TOP,
    GO_TO_GOAL,
    END
}

class DetermineTargetJewelEvent extends Event {}
class HoldingJewelEvent extends Event {}
class NotHoldingJewelEvent extends Event {}
class DetermineViaPointEvent extends Event {}
class ArrivalViaPointEvent extends Event {}
class ArrivalElevatorEvent extends Event {}
class ArrivalTopEvent extends Event {}
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

    public Sample1() throws Exception {
        super(10000);
        mode = S1Mode.DETERMINE_TARGET_JEWEL;
        targetJewel = null;
        targetViaPoint = null;
        targetGoal = null;
    }

    @Override
    protected void stateCheck() throws Exception {
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

        // car has got on the elevator?
        if (targetViaPoint!=null) {
            tmpV.sub(elevator,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalElevatorEvent());
        }

        // car has arrived at the top of the elevator?
        if (loc.y>14.5)
            processEvent(new ArrivalTopEvent());

        // car has arrived at the goal?
        if (targetGoal!=null) {
            tmpV.sub(targetGoal,loc);
            if (tmpV.length()<2.0)
                processEvent(new ArrivalGoalEvent());
        }
    }

    // This method implements a Finite Automaton
    @Override
    protected void processEvent(Event e) throws Exception {
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
                 &&(e instanceof ArrivalElevatorEvent)) {
            mode = S1Mode.WAIT_UNTIL_TOP;
            s = socket.send("sendMessage pushSwitch");
System.out.println("Sample1:sendMessage(pushSwitch):"+s);
        } else if ((mode==S1Mode.WAIT_UNTIL_TOP)
                 &&(e instanceof ArrivalTopEvent)) {
            mode = S1Mode.GO_TO_GOAL;
        } else if ((mode==S1Mode.GO_TO_GOAL)
                 &&(e instanceof ArrivalGoalEvent)) {
            mode = S1Mode.END;
            s = socket.send("sendMessage wait");
System.out.println("Sample1:sendMessage(wait):"+s);
        } else if ((true)&&(e instanceof MessageEvent)) {
            String message = ((MessageEvent)e).message;
            System.out.println("Sample1: Message received?: "+message);
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
        }
    }

    @Override
    protected void move() throws Exception {
//System.out.println("Sample1:mode="+mode);
        switch(mode) {
        case DETERMINE_TARGET_JEWEL: determineTargetJewel(); break;
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
        case DETERMINE_WITCH_VIA_POINT: determineWitchViaPoint(); break;
        case GO_TO_VIA_POINT: goToViaPoint(); break;
        case GET_ON_ELEVATOR: getOnElevator(); break;
        case WAIT_UNTIL_TOP: waitUntilTop(); break;
        case GO_TO_GOAL: goToGoal(); break;
        case END: end(); break;
        default: System.out.println("Sample1: Unknown mode?"+mode);
        }
    }

    void determineTargetJewel() throws Exception {
        targetJewel = jewelSet.getNearest(loc);
        //if (targetJewel!=null)...
        processEvent(new DetermineTargetJewelEvent());
    }

    void goToTargetJewel() throws Exception {
        goToDestination(targetJewelLoc);
    }

    void determineWitchViaPoint() throws Exception {
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

    void goToViaPoint() throws Exception {
        //goToDestinationWithJewel(targetViaPoint);
        goToDestination(targetViaPoint);
    }

    void getOnElevator() throws Exception {
        //goToDestinationWithJewel(elevator);
        goToDestination(elevator);
    }

    void waitUntilTop() throws Exception {
        socket.send("drive 0 0");
    }

    void goToGoal() throws Exception {
        //goToDestinationWithJewel(targetGoal);
        goToDestination(targetGoal);
    }

    void end() throws Exception {
        socket.send("drive 0 0");
    }

    public static void main(String args[]) throws Exception {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
