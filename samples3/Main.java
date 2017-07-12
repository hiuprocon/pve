import etrobo.*;

public class Main {
    public static void main(String[] args) {
        SimEnv env = new SimEnv();

        Stage10 s = new Stage10();
        s.setLocRev(0,0,0, 0,0,0);
        env.add(s);

        MyProgram prog = new MyProgram();
        ETRobo robo = new ETRobo(prog);
        robo.setLocRev(0,0.1,0, 0,0,0);
        env.add(robo);

        Kaidan10 k = new Kaidan10();
        k.setLocRev(5,0,0, 0,0,0);
        env.add(k);

        LookUpGate10 l = new LookUpGate10();
        l.setLocRev(-5,0,0, 0,0,0);
        env.add(l);

        Garage10 g1 = new Garage10();
        g1.setLocRev(0,0.25,5, 0,0,0);
        env.add(g1);

        Garage10 g2 = new Garage10();
        g2.setLocRev(0,0.25,-5, 0,0,0);
        env.add(g2);

        env.loop();
        //while (true) {
        //    env.stepForward();
        //    try{Thread.sleep(4);}catch(Exception e){;}
        //}
    }
}
