package samples;

enum S2Mode {
    S0,
    S1,
    S2,
    S3,
    S4
}

/*
 * Sample2
 */
public class Sample2 extends SampleBase {
    static final Vector waitPoint = new Vector(0,0,20);

    S2Mode mode;
    Vector destination;

    public Sample2() throws Exception {
        super(20000);
        mode = S2Mode.S0;
    }

    @Override
    protected void move() throws Exception {
        goToDestination(waitPoint);
    }

    @Override
    protected void debug() {
        System.out.println("Sample2.debug();"+loc);
    }

    public static void main(String args[]) throws Exception {
        Sample2 s2 = new Sample2();
        s2.start();
    }
}
