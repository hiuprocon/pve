package prototype;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;

public class Simulator implements CollisionListener {
    PVEObject ground;
    PVEObject wall1;
    PVEObject wall2;
    PVEObject slope1;
    PVEObject slope2;
    Lifter elevator1;
    Lifter elevator2;
    PVEObject switch1;
    PVEObject switch2;
    PVEObject goal1;
    PVEObject goal2;
    Obstacle obstacle1;
    Obstacle obstacle2;
    CarA carA1;
    CarA carA2;
    CarB carB;

    public Simulator() throws Exception {
        PVEWorld w = new PVEWorld(PVEWorld.A3CANVAS);
        SimulatorGUI gui = new SimulatorGUI(w.getMainCanvas());
        w.addCollisionListener(this);
        w.resume();

        ground = new BoxObj(Type.STATIC, 0, new Vector3d(250, 1, 100),
                "x-res:///res/prototype/Ground.wrl");
        ground.setUserData("ground");
        ground.setLocRev(0, -0.5, 0, 0, 0, 0);
        w.add(ground);

        wall1 = new BoxObj(Type.STATIC, 0, new Vector3d(5, 5, 100),
                "x-res:///res/Box.wrl");
        wall1.setUserData("wall1");
        wall1.setLocRev(-25, 2.51, 0, 0, 0, 0);
        w.add(wall1);

        wall2 = new BoxObj(Type.STATIC, 0, new Vector3d(5, 5, 100),
                "x-res:///res/Box.wrl");
        wall2.setUserData("wall2");
        wall2.setLocRev(25, 2.51, 0, 0, 0, 0);
        w.add(wall2);

        slope1 = new SlopeObj(10, 1, 10);
        slope1.setUserData("slope1");
        slope1.setLocRev(-42.52, 0.51, 0, 0, -90, 0);
        w.add(slope1);

        slope2 = new SlopeObj(10, 1, 10);
        slope2.setUserData("slope2");
        slope2.setLocRev(42.52, 0.51, 0, 0, 90, 0);
        w.add(slope2);

        elevator1 = new Lifter();
        elevator1.setUserData("elevator1");
        elevator1.setLocRev(-32.51, 0.51, 0, 0, 0, 0);
        w.add(elevator1);

        elevator2 = new Lifter();
        elevator2.setUserData("elevator2");
        elevator2.setLocRev(32.51, 0.51, 0, 0, 0, 0);
        w.add(elevator2);

        switch1 = new BoxObj(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/ClearBox.wrl");
        switch1.setUserData("switch1");
        switch1.setLocRev(-17.49, 0.51, 10, 0, 0, 0);
        w.add(switch1);

        switch2 = new BoxObj(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/ClearBox.wrl");
        switch2.setUserData("switch2");
        switch2.setLocRev(17.49, 0.51, -10, 0, 0, 0);
        w.add(switch2);

        goal1 = new BoxObj(Type.GHOST, 1, new Vector3d(20, 1, 10),
                "x-res:///res/ClearBox.wrl");
        goal1.setUserData("goal1");
        goal1.setLocRev(0, 0.51, 45, 0, 0, 0);
        w.add(goal1);

        goal2 = new BoxObj(Type.GHOST, 1, new Vector3d(20, 1, 10),
                "x-res:///res/ClearBox.wrl");
        goal2.setUserData("goal2");
        goal2.setLocRev(0, 0.51, -45, 0, 0, 0);
        w.add(goal2);

        obstacle1 = new Obstacle(new Vector3d(0, 0.5, 10), new Vector3d(-0.3,
                0, 0.3));
        obstacle1.setUserData("obstacle1");
        w.add(obstacle1);

        obstacle2 = new Obstacle(new Vector3d(0, 0.5, -10), new Vector3d(0.4,
                0, -0.5));
        obstacle2.setUserData("obstacle2");
        w.add(obstacle2);

        carA1 = new CarA(w, 10000);
        carA1.setUserData("carA1");
        carA1.setLocRev(-80, 0, 0, 0, 90, 0);
        w.add(carA1);
        gui.setCarA1(carA1);

        carA2 = new CarA(w, 20000);
        carA2.setUserData("carA2");
        carA2.setLocRev(80, 0, 0, 0, -90, 0);
        w.add(carA2);
        gui.setCarA2(carA2);

        carB = new CarB(30000);
        carB.setUserData("carB");
        carB.setLocRev(0, 0, 0, 0, 90, 0);
        w.add(carB);
        gui.setCarB(carB);

        for (int i = 0; i < 10; i++) {
            Jewel j = new Jewel();
            j.setUserData("jwewlA1." + i);
            double x = 100 * Math.random() - 50 - 75;
            double z = 100 * Math.random() - 50;
            j.setLocRev(x, 2, z, 0, 0, 0);
            w.add(j);
        }
        for (int i = 0; i < 10; i++) {
            Jewel j = new Jewel();
            j.setUserData("jwewlA2." + i);
            double x = 100 * Math.random() - 50 + 75;
            double z = 100 * Math.random() - 50;
            j.setLocRev(x, 2, z, 0, 0, 0);
            w.add(j);
        }
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        PVEObject aa = a.getObject();
        PVEObject bb = b.getObject();
        if (aa == ground || bb == ground)
            return;
        // System.out.println("collided "+(String)(aa.getUserData())+":"+
        // ((String)bb.getUserData()));
        if (aa == switch1 && bb == carB)
            elevator1.setUp();
        if (aa == carB && bb == switch1)
            elevator1.setUp();
        if (aa == switch2 && bb == carB)
            elevator2.setUp();
        if (aa == carB && bb == switch2)
            elevator2.setUp();

    }

    public static void main(String args[]) throws Exception {
        new Simulator();
    }
}
