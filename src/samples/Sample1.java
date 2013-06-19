package samples;

enum S1Mode {
    DETERMINE_TARGET_JEWEL,
    GO_TO_TARGET_JEWEL,
    DETERMINE_WITCH_VIA_POINT,
    GO_TO_VIA_POINT,
    GO_ON_ELEVATOR,
    WAIT_UNTIL_TOP,
    GO_TO_GOAL,
    END
}

class DetermineTargetJewelEvent extends Event {}
class CatchTargetJewelEvent extends Event {}
class DetermineViaPointEvent extends Event {}
class ArrivalViaPointEvent extends Event {}
class ArrivalElevatorEvent extends Event {}
class ArrivalTopEvent extends Event {}
class ArrivalGoalEvent extends Event {}

/*
 * Sample1
 */
public class Sample1 extends SampleBase {
    static final Vector[] viaPoints = {
        new Vector(-30,0, 0),
        new Vector(-25,0, 5),
        new Vector(-25,0,-5),
        new Vector( 30,0, 0),
        new Vector( 25,0,-5),
        new Vector( 25,0, 5)
    };

    S1Mode mode;
    Vector targetJewel;
    Vector targetViaPoint;
    Vector targetGoal;

    public Sample1() throws Exception {
        super(10000);
        mode = S1Mode.DETERMINE_TARGET_JEWEL;
        targetJewel = null;
        targetViaPoint = null;
        targetGoal = null;
    }

    // This method implements a Finite Automaton
    @Override
    protected void processEvent(Event e) {
        if ((mode==S1Mode.DETERMINE_TARGET_JEWEL)
          &&(e instanceof DetermineTargetJewelEvent)) {
            mode = S1Mode.GO_TO_TARGET_JEWEL;
        } else if ((mode==S1Mode.GO_TO_TARGET_JEWEL)
                 &&(e instanceof CatchTargetJewelEvent)) {
            mode = S1Mode.DETERMINE_WITCH_VIA_POINT;
        } else if ((mode==S1Mode.DETERMINE_WITCH_VIA_POINT)
                 &&(e instanceof DetermineViaPointEvent)) {
            mode = S1Mode.GO_TO_VIA_POINT;
        } else if ((mode==S1Mode.GO_TO_VIA_POINT)
                 &&(e instanceof ArrivalViaPointEvent)) {
            mode = S1Mode.GO_ON_ELEVATOR;
        } else if ((mode==S1Mode.GO_ON_ELEVATOR)
                 &&(e instanceof ArrivalElevatorEvent)) {
            mode = S1Mode.WAIT_UNTIL_TOP;
        } else if ((mode==S1Mode.WAIT_UNTIL_TOP)
                 &&(e instanceof ArrivalTopEvent)) {
            mode = S1Mode.GO_TO_GOAL;
        } else if ((mode==S1Mode.GO_TO_GOAL)
                 &&(e instanceof ArrivalGoalEvent)) {
            mode = S1Mode.END;
	} else if ((true)&&(e instanceof MessageEvent)) {
            String message = ((MessageEvent)e).message;
            System.out.println("Sample1: Message received?: "+message);
        } else {
            //System.out.println("Unprocessed event: "+e.getClass().getName());
            System.out.println(".");
        }
    }

    @Override
    protected void move() throws Exception {
        switch(mode) {
        case DETERMINE_TARGET_JEWEL: determineTargetJewel(); break;
        case GO_TO_TARGET_JEWEL: goToTargetJewel(); break;
        case DETERMINE_WITCH_VIA_POINT: determineWitchViaPoint(); break;
        case GO_TO_VIA_POINT: goToViaPoint(); break;
        case GO_ON_ELEVATOR: goOnElevator(); break;
        case WAIT_UNTIL_TOP: waitUntilTop(); break;
        case GO_TO_GOAL: goToGoal(); break;
        case END: end(); break;
        default: System.out.println("Sample1: Unknown mode?"+mode);
	}
        String s;
        if (counter % 2000 == 0) {
            s = socket.send("sendMessage wait");
System.out.println("Sample1:sendMessage(wait):"+s);
        } else if ((counter+1000) % 2000 == 0) {
            s = socket.send("sendMessage pushSwitch");
System.out.println("Sample1:sendMessage(pushSwitch):"+s);
	}
    }

    void determineTargetJewel() {
        targetJewel = jewelSet.getNearest(loc);
        //if (targetJewel!=null)...
        processEvent(new DetermineTargetJewelEvent());
    }

    void goToTargetJewel() {}
    void determineWitchViaPoint() {}
    void goToViaPoint() {}
    void goOnElevator() {}
    void waitUntilTop() {}
    void goToGoal() {}
    void end() {}

    public static void main(String args[]) throws Exception {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
