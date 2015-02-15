package prototype;

import java.util.ArrayList;
import java.util.Random;
import javax.vecmath.Vector3d;
import jp.sourceforge.acerola3d.a3.*;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;

public class Simulator3 implements SimulatorInterface, CollisionListener, Elevator3Holder {
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
    Obstacle3 obstacle1;
    Obstacle3 obstacle2;
    CarInterface car1;
    CarInterface car2;
    ArrayList<Jewel> jewels = new ArrayList<Jewel>();
    Simulator3GUI gui;
    int waitTime = 33;
    Random random;
    Action3D bgm = null;
    boolean enableBgm = false;
    Action3D se = null;
    A3CanvasInterface mainCanvas;
    ArrayList<Long> seeds = null;

    public Simulator3(String args[]) throws Exception {
        if (args.length==10) {
            try{
                seeds = new ArrayList<Long>();
                for (int i=0;i<10;i++) {
                    seeds.add(Long.parseLong(args[i]));
                }
            } catch(Exception e) {
                seeds = null;
            }
        }
        w = new PVEWorld(PVEWorld.A3CANVAS,PVEWorld.MANUAL_STEP);
        //w = new PVEWorld(PVEWorld.A3CANVAS,PVEWorld.AUTO_STEP);
        gui = new Simulator3GUI(this);
        w.addCollisionListener(this);
        initWorld();
    }
    void initWorld() throws Exception {
        if (car1!=null) {
            car1.dispose();
            car2.dispose();
            bgm.change("no_se");
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
VRML.clearCash("x-res:///res/prototype/gougui.a3");
Action3D.clearCash("x-res:///res/prototype/ChoroQred.a3");
Action3D.clearCash("x-res:///res/prototype/ChoroQblue.a3");

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

        wall1 = new BoxObj(Type.STATIC,0,new Vector3d(1,52,250),"x-res:///res/ClearBox2.wrl");
        wall1.setLocRev(-125.5,25.5,0, 0,0,0);
        w.add(wall1);

        wall2 = new BoxObj(Type.STATIC,0,new Vector3d(1,52,250),"x-res:///res/ClearBox2.wrl");
        wall2.setLocRev(125.5,25.5,0, 0,0,0);
        w.add(wall2);

        wall3 = new BoxObj(Type.STATIC,0,new Vector3d(252,52,1),"x-res:///res/ClearBox2.wrl");
        wall3.setLocRev(0,25.5,-125.5, 0,0,0);
        w.add(wall3);

        wall4 = new BoxObj(Type.STATIC,0,new Vector3d(252,52,1),"x-res:///res/ClearBox2.wrl");
        wall4.setLocRev(0,25.5,125.5, 0,0,0);
        w.add(wall4);

        ceiling = new BoxObj(Type.STATIC,0,new Vector3d(250,1,250),"x-res:///res/ClearBox2.wrl");
        ceiling.setLocRev(0,50.5,0, 0,0,0);
        w.add(ceiling);

        elevator1 = new Elevator3(this,w);
        elevator1.setLocRev(0, 0, 0+z2, 0, 0, 0);
        w.add(elevator1);

        elevator2 = new Elevator3(this,w);
        elevator2.setLocRev(0, 0, 0-z2, 0,90, 0);
        w.add(elevator2);

        //if (car1!=null)
        //    car1 = new CarA(this,car1.getServer());
        //else
            car1 = new CarA(this, 10000);
        car1.setLocRev(-90, 0, 0, 0, 90, 0);
        w.add((PVEObject)car1);
        gui.setCar1(car1);

        //if (car2!=null)
        //    car2 = new CarA(this,car2.getServer());
        //else
            car2 = new CarA(this, 20000);
        car2.setLocRev(90, 0, 0, 0, -90, 0);
        w.add((PVEObject)car2);
        gui.setCar2(car2);

        car1.setAnotherCar(car2);
        car2.setAnotherCar(car1);

        if (seeds!=null) {
            long l = seeds.remove(0);
            random = new Random(l);
            if (seeds.size()==0)
                seeds = null;
        } else {
            String seedString = gui.getSeed();
            seedString = seedString.trim();
            if (seedString==null||seedString.equals("")) {
                random = new Random();
            } else {
                long seed = 0;
                try {
                    seed = Long.parseLong(seedString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    gui.setSeed(0);
                }
                random = new Random(seed);
            }
        }

        obstacle1 = new Obstacle3(getRandomLocL());
        w.add(obstacle1);

        obstacle2 = new Obstacle3(getRandomLocR());
        w.add(obstacle2);

        Vector3d v;
        for (int i = 0; i < 10; i++) {
            Jewel2 j = new Jewel2();
            j.setUserData("jA1." + i);
            v = getRandomLocLJ();
            j.setLocRev(v.x, v.y, v.z, 0, 0, 0);
            w.add(j);
            jewels.add(j);
        }
        for (int i = 0; i < 10; i++) {
            Jewel2 j = new Jewel2();
            j.setUserData("jA2." + i);
            v = getRandomLocRJ();
            j.setLocRev(v.x, v.y, v.z, 0, 0, 0);
            w.add(j);
            jewels.add(j);
        }
        w.resume();
        w.stepForward();

        mainCanvas = w.getMainCanvas();
        //mainCanvas.setHeadLightEnable(false);
        Action3D light = new Action3D("x-res:///res/DirectionalLightSet.a3");
        Action3D grid1 = new Action3D("x-res:///res/prototype/PCGrid.a3");
        Action3D grid2 = new Action3D("x-res:///res/prototype/PCGrid.a3");
        Action3D grid3 = new Action3D("x-res:///res/prototype/PCGrid.a3");
        VRML jewelField1 = new VRML("x-res:///res/prototype/JewelField.wrl");
        VRML jewelField2 = new VRML("x-res:///res/prototype/JewelField.wrl");
        Action3D sb = new Action3D("x-res:///res/prototype/SkyBox02.a3");
        if (bgm==null) {
            bgm = new Action3D("x-res:///res/prototype/md_rock55.a3");
            bgm.setSoundGain("rock55",0.1);
            bgm.setSoundGain("jingle05",0.3);
            bgm.setSoundGain("jingle06",0.3);
            bgm.setSoundGain("jingle09",0.3);
            se = new Action3D("x-res:///res/prototype/SoundEffect001.a3");
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
        mainCanvas.setBackground(sb);
        bgmFlag = false;
        mainCanvas.addLockedA3(bgm);
        mainCanvas.addLockedA3(se);

        bgm.change("jingle05");

        gui.defaultView();
    }

    Vector3d getRandomLocL() {
        double x = 60 * random.nextDouble() - 30 - 50;
        double z = 80 * random.nextDouble() - 40;
        return new Vector3d(x,0,z);
    }
    Vector3d getRandomLocR() {
        Vector3d v = getRandomLocL();
        v.x += 100;
        return v;
    }
    Vector3d getRandomLocLJ() {
        Vector3d ol = obstacle1.loc;
        Vector3d l = null;
        Vector3d tmp = new Vector3d();
        while (true) {
            l = getRandomLocL();
            tmp.sub(ol,l);
            if (tmp.length()>10.0)
                break;
        }
        l.y = 2.0;
        return l;
    }
    Vector3d getRandomLocRJ() {
        Vector3d ol = obstacle2.loc;
        Vector3d l = null;
        Vector3d tmp = new Vector3d();
        while (true) {
            l = getRandomLocR();
            tmp.sub(ol,l);
            if (tmp.length()>10.0)
                break;
        }
        l.y = 2.0;
        return l;
    }

    boolean bgmFlag=false;
    public void start() {
        //A3CanvasInterface mainCanvas = w.getMainCanvas();
        waitTime = 33;
        while (true) {
            stepForward();
            if (bgmFlag==false) {
                if (enableBgm==true)
                    bgm.change("rock55");
                bgmFlag=true;
            }
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
    public String stepForward() {
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
        //TODO
        return "OK";
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        ;
    }
    void goal(Jewel j) {
        gui.appendText("goal! "+j.getUserData());
        gui.goal(j);
        w.del(j);
        se.change("power20");
        synchronized(jewels) {
            jewels.remove(j);
        }
        if (jewels.size()==0) {
            bgm.change("jingle06");
            w.pause();
            try{Thread.sleep(1000);}catch(Exception e){;}
            gui.updateTime(w.getTime());
            gui.appendText(String.format("clear!!!!!  time=%9.2f",w.getTime()));
        }
    }
    void timeUp() {
        w.pause();
        bgm.change("jingle09");
        try{Thread.sleep(1000);}catch(Exception e){;}
        gui.updateTime(5000.0);
        gui.appendText(String.format("time up!!!!!  time=%9.2f",5000.0));
    }
    public String searchJewels() {
        synchronized(jewels) {
            String s = ""+jewels.size();
            for (Jewel j:jewels) {
                Vector3d v = j.getLoc();
                s = s +" "+j.getUserData().toString()+" "+v.x+" "+v.y+" "+v.z;
            }
            return s;
        }
    }
    public String searchObstacles() {
        String s = "2";
        Vector3d v = obstacle1.loc;
        s = s +" o1 "+ v.x +" "+v.y+" "+v.z;
        v = obstacle2.loc;
        s = s +" o2 "+ v.x +" "+v.y+" "+v.z;
        return s;
    }
    public void setWaitTime(int t) {
        waitTime=t;
    }
    void pause() {
        w.pause();
    }
    void resume() {
        w.resume();
    }
    public PVEWorld getPVEWorld() {
        return w;
    }

    public static void main(String args[]) throws Exception {
        jp.sourceforge.acerola3d.A23.initA23();
        System.setProperty("j3d.viewFrustumCulling","false");
        Simulator3 s3 = new Simulator3(args);
        s3.start();
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
    @Override
    public String getElevator1Height() {
        return ""+this.elevator1.getHeight();
    }
    @Override
    public String getElevator2Height() {
        return ""+this.elevator2.getHeight();
    }
}
