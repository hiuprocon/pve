package test_cr;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.car.*;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;

class CarRaceImpl implements Runnable, CollisionListener, CarSim {
    PVEWorld world;
    Preferences prefs;
    RaceCarBase car;
    String carClasspath;
    String workDir;
    String workDirURL;
    ArrayList<ActiveObject> activeObjects = new ArrayList<ActiveObject>();
    boolean battleRunning = false;//一時停止中でもtrue
    boolean simRunning = false;//一時停止中はfalse
    URLClassLoader classLoader;
    CarRaceGUI gui;
    int NUM=12;
    CheckPoint cps[];
    Deque<CheckPoint> checkPointStack;

    CarRaceImpl(String args[]) {
        prefs = Preferences.userNodeForPackage(this.getClass());
        String carClass = prefs.get("carClass","test.TestCar01");
        if (args.length>=1) {
            carClass = args[0];
            prefs.put("carClass",carClass);
        }
        carClasspath = prefs.get("carClasspath","System");
        workDir = prefs.get("workDir",null);
        workDirURL = prefs.get("workDirURL",null);
        checkPointStack = new ArrayDeque<CheckPoint>();

        world = new PVEWorld(PVEWorld.A3CANVAS);
        world.pause();
        simRunning = false;
        world.addTask(this);
        world.addCollisionListener(this);

        gui = new CarRaceGUI(this,carClass);
        gui.pack();
        gui.setVisible(true);

        world.addSubCanvas(gui.carCanvas);
    }
    //clearの処理
    void clearBattle() {
        if (simRunning)
            throw new IllegalStateException();
        if (battleRunning)
            throw new IllegalStateException();
        world.clear();
        activeObjects.clear();
        car = null;
        classLoader = null;
        System.gc();
        gui.clearCamera();
        gui.clearTA();
        checkPointStack.clear();
    }
    void initBattle() {
        if (simRunning)
            throw new IllegalStateException();
        if (battleRunning)
            throw new IllegalStateException();
        Ground g = new Ground("x-rzip:x-res:///res/stk_racetrack.a3!/racetrack.wrl");
        world.add(g);

        //CheckPoint配置
        cps = new CheckPoint[NUM];
        Vector3d loc = new Vector3d();
        Vector3d rot = new Vector3d();
        loc.set(  0,  4,-25);rot.set(0,0,0);
        cps[ 0] = new CheckPoint();
        cps[ 0].setLoc(loc);cps[ 0].setRot(rot);
        loc.set(-37,  4,-56);rot.set(0,1.57,0);
        cps[ 1] = new CheckPoint();
        cps[ 1].setLoc(loc);cps[ 1].setRot(rot);
        loc.set(-76,  4,  9);rot.set(0,0.78,0);
        cps[ 2] = new CheckPoint();
        cps[ 2].setLoc(loc);cps[ 2].setRot(rot);
        loc.set(-21,  4, 20);rot.set(0,0,0);
        cps[ 3] = new CheckPoint();
        cps[ 3].setLoc(loc);cps[ 3].setRot(rot);
        loc.set(-54,  4, 35);rot.set(0,0,0);
        cps[ 4] = new CheckPoint();
        cps[ 4].setLoc(loc);cps[ 4].setRot(rot);
        loc.set(-16,  4, 35);rot.set(0,-0.78,0);
        cps[ 5] = new CheckPoint();
        cps[ 5].setLoc(loc);cps[ 5].setRot(rot);
        loc.set(-23,  4,-39);rot.set(0,1.57,0);
        cps[ 6] = new CheckPoint();
        cps[ 6].setLoc(loc);cps[ 6].setRot(rot);
        loc.set(-43,  4,  0);rot.set(0,1.57,0);
        cps[ 7] = new CheckPoint();
        cps[ 7].setLoc(loc);cps[ 7].setRot(rot);
        loc.set(-53,  4,-38);rot.set(0,1.57,0);
        cps[ 8] = new CheckPoint();
        cps[ 8].setLoc(loc);cps[ 8].setRot(rot);
        loc.set(-65,  9, 11);rot.set(0,0,0);
        cps[ 9] = new CheckPoint();
        cps[ 9].setLoc(loc);cps[ 9].setRot(rot);
        loc.set(-40,  4, 55);rot.set(0,1.57,0);
        cps[10] = new CheckPoint();
        cps[10].setLoc(loc);cps[10].setRot(rot);
        loc.set(  0,  4, 10);rot.set(0,0,0);
        cps[11] = new CheckPoint();
        cps[11].setLoc(loc);cps[11].setRot(rot);

        for (int i=0;i<NUM;i++) {
            world.add(cps[i]);
            cps[i].getMainA3().setUserData(String.format("cp%02d",i));
        }

        classLoader = makeClassLoader(carClasspath);

        String carClass = gui.carClassTF.getText();
        prefs.put("carClass",carClass);
        //try{prefs.flush();}catch(Exception e){;}
        try {
            Class<?> theClass = classLoader.loadClass(carClass);
            Class<? extends RaceCarBase> tClass = theClass.asSubclass(RaceCarBase.class);
            car = tClass.newInstance();
        } catch(Exception e) {
            System.out.println("Class Load Error!!!");
            e.printStackTrace();
        }

        //下の行の3.1という数値は本来PI=3.141592...ジンバルロック対策でわざと誤差を入れた
        car.car.init("x-res:///res/stk_tux.a3",world,this);
        world.add(car.car.car);
        car.car.car.setLoc(new Vector3d( 0,0.8,-1));
        car.car.car.setRot(new Vector3d(0,3.1,0));

        gui.setCar(car.car);
        activeObjects.add(car.car);
        gui.updateCarInfo(car.car);
        gui.a3csController.init();
    }
    
    URLClassLoader makeClassLoader(String s) {
        try {
            URLClassLoader cl = null;
            URL urls[] = null;
            if (s==null) {
                urls = new URL[0];
            } else if (s.equals("System")) {
                urls = new URL[0];
            } else if (s.equals("IDE")) {
                if (workDirURL==null) {
                    urls = new URL[0];
                } else {
                    urls = new URL[]{new URL(workDirURL)};
                }
            } else {
                urls = new URL[]{new URL(s)};
            }
            final URL urlsF[] = urls;
            final ClassLoader pCL = CarBase.class.getClassLoader();
            cl = AccessController.doPrivilegedWithCombiner(new PrivilegedAction<URLClassLoader>() {
                public URLClassLoader run() {
                    return new URLClassLoader(urlsF,pCL);
                }
            });

            return cl;
        } catch (MalformedURLException e) {
            return null;
        } catch (Error e) {
            e.printStackTrace();
            return null;
        }
    }
    void startBattle() {
        if (simRunning) {
            return;
            //throw new IllegalStateException();
        }
        if (battleRunning) {
            world.resume();
            simRunning = true;
        } else {
            clearBattle();
            initBattle();
            world.resume();
            simRunning = true;
            gui.setParamEditable(false);
            battleRunning = true;
        }
    }
    void pauseBattle() {
        if (!battleRunning)
            return;
        if (simRunning) {
            world.pause();
            simRunning = false;
        } else {
            world.resume();
            simRunning = true;
        }
    }
    void stopBattle() {
        battleRunning = false;
        //clearBattle();
        world.pause();
        simRunning = false;
        gui.setParamEditable(true);
    }
    @Override
    public ArrayList<CarBase> getAllCar() {
        ArrayList<CarBase> ret = new ArrayList<CarBase>();
        ret.add(car.car);
        return ret;
    }

    @Override
    public void addActiveObject(ActiveObject o) {
        synchronized (activeObjects) {
            activeObjects.add(o);
        }
    }

    @Override
    public void delActiveObject(ActiveObject o) {
        synchronized (activeObjects) {
            activeObjects.remove(o);
        }
    }

    @Override
    public void collided(PVEPart aa, PVEPart bb) {
    	PVEObject a = aa.getObject();
    	PVEObject b = bb.getObject();
        if ((a instanceof CheckPoint)||(b instanceof CheckPoint)) {
            CheckPoint cp = null;
            PVEObject other = null;
            if (a instanceof CheckPoint) {
                cp = (CheckPoint)a;
                other = b;
            } else {
                cp = (CheckPoint)b;
                other = a;
            }
            if (other instanceof SimpleCarObj) {
                if (checkPointStack.peek()!=cp) {
                    checkPointStack.push(cp);
                    String t = String.format("%4.2f",world.getTime());
                    System.out.println(cp.getMainA3().getUserData()+":"+t);
                }
                if (cp==cps[NUM-1]) {
                    finishBattle();
                }
            } else if (other instanceof Ground){
                ;
            } else if (other instanceof Bullet){
                ;
            } else if (other instanceof CheckPoint){
                ;
            } else {
            }
        }
        //System.out.println("gaha a:"+a.a3.getUserData()+" b:"+b.a3.getUserData());
    }

    @Override
    public void run() {
        ArrayList<ActiveObject> tmp = new ArrayList<ActiveObject>();
        synchronized (activeObjects) {
            tmp.clear();
            tmp.addAll(activeObjects);
        }
        for (ActiveObject o: tmp) {
            if (o instanceof CarBase) {
                ((CarBase)o).beforeExec();
            }
            o.exec();
        }
        gui.updateCarInfo(car.car);
        gui.updateTime(world.getTime());
//GAHA
if (debugCount>50){
//cps[0].setLoc(0,4,-30);
debugCount=0;
}
debugCount++;
    }
int debugCount=0;

    void finishBattle() {
        world.pause();
        simRunning = false;
        boolean goal=true;
        for (int i=0;i<NUM;i++) {
            if (checkPointStack.isEmpty()){
                goal=false;
                break;
            }
            CheckPoint cp = checkPointStack.removeLast();
            if (cps[i]!=cp) {
                goal=false;
                break;
            }
        }
        String message = goal ? "goal":"fail";
        message = message+"\ntime:"+String.format("%4.2f",world.getTime());
        JOptionPane.showMessageDialog(gui,message);

        stopBattle();
    }
    void changeCP(String cp) {
        carClasspath = cp;
        prefs.put("carClasspath",carClasspath);
    }
    void setWorkDirURL(String wdu) {
        workDirURL = wdu;
        if (workDirURL!=null)
            prefs.put("workDirURL",workDirURL);
    }
    void setWorkDir(String wd) {
        workDir = wd;
        if (workDir!=null)
            prefs.put("workDir",workDir);
    }
    void fastForward(boolean b) {
        world.fastForward(b);
    }
}
