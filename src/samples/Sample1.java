package samples;

/*
 * Sample1
 */
public class Sample1 extends SampleBase {
    public Sample1() throws Exception {
        super(10000);
    }

    @Override
    protected void move() {
    }

    @Override
    protected void debug() {
        System.out.println("Sample1.debug();");
    }

    public static void main(String args[]) throws Exception {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
