package samples;

/*
 * Sample2
 */
public class Sample2 extends SampleBase {
    static final Vector waitingPoint = new Vector(0,0,20);

    Vector destination;

    public Sample2() {
        super(20000);
        destination = new Vector(waitingPoint);
    }

    @Override
    protected void processEvent(Event e) {
        if (e instanceof MessageEvent) {
            String message = ((MessageEvent)e).message;
System.out.println("Sample2:reciveMessage: "+message);
            if (message.equals("wait")) {
                destination.set(waitingPoint);
            } else if (message.equals("pushSwitch")) {
                destination.set(switch2);
            }
        } else {
            System.out.println("Unknown event: "+e.getClass().getName());
        }
    }

    @Override
    protected void move() {
        goToDestination(destination);
    }

    public static void main(String args[]) {
        Sample2 s2 = new Sample2();
        s2.start();
    }
}
