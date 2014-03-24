package prototype;

import java.util.ArrayList;
import java.util.Random;
import javax.vecmath.Vector3d;
import jp.sourceforge.acerola3d.a3.A3Background;
import jp.sourceforge.acerola3d.a3.A3CanvasInterface;
import jp.sourceforge.acerola3d.a3.Action3D;
import jp.sourceforge.acerola3d.a3.VRML;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;

public class Simulator3 implements CollisionListener, Elevator3Holder {
    PVEWorld w;
    PVEObject ground1;
    PVEObject ground2;
    PVEObject ground3;
    PVEObject ground4;
    PVEObject ground5;
    PVEObject wall1;
    PVEObject wall2;
    PVEObject wall3;
    PVEObject wall4;
    PVEObject ceiling;
    Elevator3 elevator1;
    Elevator3 elevator2;
    CarInterface car1;
    CarInterface car2;
    ArrayList<Jewel> jewels = new ArrayList<Jewel>();
    Simulator3GUI gui;
    int waitTime = 33;
    Random random;

    public Simulator3() {
        w = new PVEWorld(PVEWorld.A3CANVAS,PVEWorld.MANUAL_STEP);
        //w = new PVEWorld(PVEWorld.A3CANVAS,PVEWorld.AUTO_STEP);
        gui = new Simulator3GUI(this);
        w.addCollisionListener(this);
        initWorld();
    }
    void initWorld() {
        if (car1!=null) {
            car1.dispose();
            car2.dispose();
            try{Thread.sleep(1000);}catch(Exception e) {;}
        }

        noOfActivated = 3; //simulator + cars
        noOfWaiting = 0;

        w.resume();
        synchronized(waitingRoom) {
            waitingRoom.notifyAll();
        }
        try{Thread.sleep(1000);}catch(Exception e) {;}

        w.pause();
        w.clear();
        jewels.clear();

//jp.sourceforge.acerola3d.A23.clearZipCache();
//VRML.clearCash("x-res:///res/prototype/Jewel.wrl");
VRML.clearCash("x-res:///res/prototype/Jewel.a3");

        double z1 = 96.25;
        double z2 = 62.5;

        ground1 = new BoxObj(Type.STATIC, 0, new Vector3d(120, 1, 250),"x-res:///res/prototype/Ground2.wrl");
        ground1.setLocRev(-65, -0.5, 0, 0, 0, 0);
        w.add(ground1);

        ground2 = new BoxObj(Type.STATIC, 0, new Vector3d(120, 1, 250),"x-res:///res/prototype/Ground2.wrl");
        ground2.setLocRev( 65, -0.5, 0, 0, 0, 0);
        w.add(ground2);

        ground3 = new BoxObj(Type.STATIC, 0, new Vector3d( 10, 1, 57.5),"x-res:///res/prototype/Ground2.wrl");
        ground3.setLocRev(0, -0.5, -z1, 0, 0, 0);
        w.add(ground3);

        ground4 = new BoxObj(Type.STATIC, 0, new Vector3d( 10, 1, 115),"x-res:///res/prototype/Ground2.wrl");
        ground4.setLocRev(0, -0.5, 0, 0, 0, 0);
        w.add(ground4);

        ground5 = new BoxObj(Type.STATIC, 0, new Vector3d( 10, 1, 57.5),"x-res:///res/prototype/Ground2.wrl");
        ground5.setLocRev(0, -0.5, z1, 0, 0, 0);
        w.add(ground5);

        wall1 = new BoxObj(Type.STATIC,0,new Vector3d(1,52,250),"x-res:///res/ClearBox.wrl");
        wall1.setLocRev(-125.5,25.5,0, 0,0,0);
        w.add(wall1);

        wall2 = new BoxObj(Type.STATIC,0,new Vector3d(1,52,250),"x-res:///res/ClearBox.wrl");
        wall2.setLocRev(125.5,25.5,0, 0,0,0);
        w.add(wall2);

        wall3 = new BoxObj(Type.STATIC,0,new Vector3d(252,52,1),"x-res:///res/ClearBox.wrl");
        wall3.setLocRev(0,25.5,-125.5, 0,0,0);
        w.add(wall3);

        wall4 = new BoxObj(Type.STATIC,0,new Vector3d(252,52,1),"x-res:///res/ClearBox.wrl");
        wall4.setLocRev(0,25.5,125.5, 0,0,0);
        w.add(wall4);

        ceiling = new BoxObj(Type.STATIC,0,new Vector3d(250,1,250),"x-res:///res/ClearBox.wrl");
        ceiling.setLocRev(0,50.5,0, 0,0,0);
        w.add(ceiling);

        elevator1 = new Elevator3(this,w);
        elevator1.setLocRev(0, 0, 0+z2, 0, 0, 0);
        w.add(elevator1);

        elevator2 = new Elevator3(this,w);
        elevator2.setLocRev(0, 0, 0-z2, 0, 0, 0);
        w.add(elevator2);

        car1 = new CarE(this, 10000);
        car1.setLocRev(-80, 0, 0, 0, 90, 0);
        w.add((PVEObject)car1);
        gui.setCar1(car1);

        car2 = new CarE(this, 20000);
        car2.setLocRev(80, 0, 0, 0, -90, 0);
        w.add((PVEObject)car2);
        gui.setCar2(car2);

        ((CarE)car1).setAnotherCar((CarE)car2);
        ((CarE)car2).setAnotherCar((CarE)car1);

        String seedS = gui.getSeed();
        if (seedS==null||seedS.equals("")) {
            random = new Random();
        } else {
            long seed = 0;
            try {
                seed = Long.parseLong(seedS);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                gui.setSeed(0);
            }
            random = new Random(seed);
        }
        for (int i = 0; i < 10; i++) {
            Jewel2 j = new Jewel2();
            j.setUserData("jA1." + i);
            double x = 60 * random.nextDouble() - 30 - 50;
            double z = 80 * random.nextDouble() - 40;
            j.setLocRev(x, 2, z, 0, 0, 0);
            w.add(j);
            jewels.add(j);
        }
        for (int i = 0; i < 10; i++) {
            Jewel2 j = new Jewel2();
            j.setUserData("jA2." + i);
            double x = 60 * random.nextDouble() - 30 + 50;
            double z = 80 * random.nextDouble() - 40;
            j.setLocRev(x, 2, z, 0, 0, 0);
            w.add(j);
            jewels.add(j);
        }
        w.resume();
        w.stepForward();

        A3CanvasInterface mainCanvas = w.getMainCanvas();
        mainCanvas.setHeadLightEnable(false);
        Action3D light = null;
        Action3D grid1 = null;
        Action3D grid2 = null;
        Action3D grid3 = null;
        VRML jewelField1 = null;
        VRML jewelField2 = null;
        try {
            light = new Action3D("x-res:///res/DirectionalLightSet.a3");
            grid1 = new Action3D("x-res:///res/prototype/PCGrid.a3");
            grid2 = new Action3D("x-res:///res/prototype/PCGrid.a3");
            grid3 = new Action3D("x-res:///res/prototype/PCGrid.a3");
            jewelField1 = new VRML("x-res:///res/prototype/JewelField.wrl");
            jewelField2 = new VRML("x-res:///res/prototype/JewelField.wrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        light.change("dl1.0");
        light.setLoc(0,10,0);
        mainCanvas.add(light);
        grid1.setRev(-90,0,0);
        grid1.setLoc(0,-2,0);
        mainCanvas.add(grid1);
        grid2.setLoc(0,0,-150);
        mainCanvas.add(grid2);
        grid3.setRev(0,90,0);
        grid3.setLoc(-150,0,0);
        mainCanvas.add(grid3);
        jewelField1.setScaleX(60);jewelField1.setScaleY(0.1);jewelField1.setScaleZ(80);
        jewelField1.setLoc(50,0,0);
        mainCanvas.add(jewelField1);
        jewelField2.setScaleX(60);jewelField2.setScaleY(0.1);jewelField2.setScaleZ(80);
        jewelField2.setLoc(-50,0,0);
        mainCanvas.add(jewelField2);
        mainCanvas.setBackground(new A3Background(0.1f, 0.3f, 0.5f));

        gui.defaultView();
    }

    public void start() {
        //A3CanvasInterface mainCanvas = w.getMainCanvas();
        waitTime = 33;
        while (true) {
            stepForward();
            if (w.getTime()>=5000.0)
                timeUp();
            //mainCanvas.waitForUpdate(waitTime * 2);
            //Thread.sleep(waitTime/2);// 微妙
            gui.updateTime(w.getTime());
            try {Thread.sleep(waitTime);}catch(Exception e) {;}
        }
    }
    Object waitingRoom = new Object();
    int noOfActivated = 3; //simulator + cars
    volatile int noOfWaiting = 0;
    //ArrayList<Object> waitings = new ArrayList<Object>();
    void deactivateOneCar() {
        noOfActivated=2;
        noOfWaiting = 0;
        synchronized(waitingRoom) {
            waitingRoom.notifyAll();
        }
    }
    void activateTwoCars() {
        noOfActivated=3;
        noOfWaiting = 0;
        synchronized(waitingRoom) {
            waitingRoom.notifyAll();
        }
    }
    void stepForward() {
        synchronized (waitingRoom) {
            noOfWaiting++;
            if (noOfWaiting==noOfActivated) {
                w.stepForward();
                car1.swapMessageBuffer();
                car2.swapMessageBuffer();
                noOfWaiting = 0;
                waitingRoom.notifyAll();
            } else {
                try {
                    waitingRoom.wait();
                } catch(Exception e) {
                    ;
                }
            }
        }
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        ;
    }
    void goal(Jewel j) {
        gui.appendText("goal! "+j.getUserData());
        w.del(j);
        synchronized(jewels) {
            jewels.remove(j);
        }
        if (jewels.size()==0) {
            w.pause();
            try{Thread.sleep(1000);}catch(Exception e){;}
            gui.updateTime(w.getTime());
            gui.appendText(String.format("clear!!!!!  time=%9.2f",w.getTime()));
        }
    }
    void timeUp() {
        w.pause();
        try{Thread.sleep(1000);}catch(Exception e){;}
        gui.updateTime(5000.0);
        gui.appendText(String.format("time up!!!!!  time=%9.2f",5000.0));
    }
    String searchJewels() {
        synchronized(jewels) {
            String s = ""+jewels.size();
            for (Jewel j:jewels) {
                Vector3d v = j.getLoc();
                s = s +" "+j.getUserData().toString()+" "+v.x+" "+v.y+" "+v.z;
            }
            return s;
        }
    }
    void setWaitTime(int t) {
        waitTime=t;
    }
    void pause() {
        w.pause();
    }
    void resume() {
        w.resume();
    }

    public static void main(String args[]) {
        Simulator3 s2 = new Simulator3();
        s2.start();
    }
    @Override
    public ArrayList<Jewel> getJewelsCopy() {
        ArrayList<Jewel> alTmp = new ArrayList<Jewel>();
        synchronized (jewels) {
            alTmp.addAll(jewels);
        }
        return alTmp;
    }
    @Override
    public void processGoal(Jewel j) {
        goal(j);
    }
}
