package samples;

import java.util.HashMap;
import java.util.ArrayList;

enum Mode {
    SEARCH,
    GOAL
}

class Jewels {
    HashMap<String,Vector> jewels;
    Jewels() {
        jewels = new HashMap<String,Vector>();
    }
    public void load(String info) {
        String[] ss = info.split("\\s");
        int n = Integer.parseInt(ss[0]);
        for (int i=0;i<n;i++) {
            int ii = 1 + 4*i;
            double x = Double.parseDouble(ss[ii+1]);
            double y = Double.parseDouble(ss[ii+2]);
            double z = Double.parseDouble(ss[ii+3]);
            Vector jv = new Vector(x,y,z);
            jewels.put(ss[ii],jv);
        }
    }
    public Vector get(String id) {
        return jewels.get(id);
    }
}

/*
 * Sample1
 */
public class Sample1 {
    // current simulation time
    double currentTime;
    // socket
    MySocket socket;
    // Location of this car
    Vector loc;
    // Rotation of this car
    Vector rot;
    // Front unit vector of this car
    Vector front;
    // Left unit vector of this car
    Vector left;
    // Old location of this car
    Vector oldLoc;
    // Velocity of this car
    Vector vel;
    // Manager of jewels
    Jewels jewels;
    // Messages
    ArrayList<String> messages;
    // Mode
    Mode mode;
    // Simulation step time
    final double dt = 1.0/30.0;

    public Sample1() throws Exception {
        currentTime = 0.0;
        socket = new MySocket(10000);
        loc = new Vector();
        rot = new Vector();
        front = new Vector();
        left = new Vector();
        oldLoc = new Vector();
        vel = new Vector();
        jewels = new Jewels();
        messages = new ArrayList<String>();
    }

    public void start() throws Exception {
        while (true) {
            stateCheck();
            selectMode();
            exec();
            debug();
            messages.clear();
            socket.send("stepForward");
            currentTime += dt;
        }
    }

    void debug() {
        Vector jv = jewels.get("jA1.0");
        System.out.println(jv);
        System.out.println(front);
    }

    // Check current status
    void stateCheck() throws Exception {
        oldLoc.set(loc);
        String ret = socket.send("getLoc");
        loc.set(ret);
        ret = socket.send("getRev");
        rot.set(ret);
        front = Vector.rotate(rot, new Vector(0,0,-1));
        //front = Vector.simpleRotateY(rot.y, new Vector(0,0,-1));
        left = Vector.rotate(rot, new Vector(-1,0,0));
        //left = Vector.simpleRotateY(rot.y, new Vector(-1,0,0));
        vel.sub(loc,oldLoc);
        vel.scale(1.0/dt);
        ret = socket.send("searchJewels");
        jewels.load(ret);
        ret = socket.send("receiveMessages");
        if (ret.length()>9) {
            ret = ret.substring(9);
            String[] ss = ret.split(",");
            for (String s:ss)
                messages.add(s);
        }
    }

    // Select a mode
    void selectMode() {
        mode = Mode.SEARCH;
    }

    void exec() throws Exception {
        switch(mode) {
        case SEARCH: execSearch(); break;
        case GOAL: execGoal(); break;
        }
    }

    void execSearch() throws Exception {
        System.out.println("execSearch().");
        socket.send("drive 1 1");
    }

    void execGoal() {
        System.out.println("execGoal().");
    }

    public static void main(String args[]) throws Exception {
        Sample1 s1 = new Sample1();
        s1.start();
    }
}
