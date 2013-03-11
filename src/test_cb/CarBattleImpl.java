package test_cb;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.vecmath.Vector3d;


import com.github.hiuprocon.pve.car.CarBase;
import com.github.hiuprocon.pve.car.CarSim;
import com.github.hiuprocon.pve.car.MyCar;
import com.github.hiuprocon.pve.core.PVEPart;
import com.github.hiuprocon.pve.core.ActiveObject;
import com.github.hiuprocon.pve.core.CollisionListener;
import com.github.hiuprocon.pve.core.PVEWorld;
import com.github.hiuprocon.pve.obj.MyBox;
import com.github.hiuprocon.pve.obj.MyBullet;
import com.github.hiuprocon.pve.obj.MyGround2;
import com.github.hiuprocon.pve.obj.MyGround3;
import com.github.hiuprocon.pve.obj.MySphere;

import java.util.prefs.*;

class CarBattleImpl implements Runnable, CollisionListener, CarSim {
    PVEWorld world;
    Preferences prefs;
    BattleCarBase car1;
    BattleCarBase car2;
    String car1classpath;
    String car2classpath;
    String workDir;
    String workDirURL;
    ArrayList<ActiveObject> activeObjects = new ArrayList<ActiveObject>();
    Object waitingRoom = new Object();
    boolean battleRunning = false;//一時停止中でもtrue
    boolean simRunning = false;//一時停止中はfalse
    boolean pauseRequest = true;
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    CarBattleGUI gui;

    URLClassLoader classLoader1;
    URLClassLoader classLoader2;

    CarBattleImpl(String args[]) {
        //http://www.javainthebox.net/laboratory/JDK1.4/MiscAPI/Preferences/Preferences.html
        prefs = Preferences.userNodeForPackage(this.getClass());

        String carClass1 = prefs.get("carClass1","test.TestCar02");
        String carClass2 = prefs.get("carClass2","test.TestCar02");
        if (args.length>=1) {
            carClass1 = args[0];
            prefs.put("carClass1",carClass1);
        }
        if (args.length>=2) {
            carClass2 = args[1];
            prefs.put("carClass2",carClass2);
        }

        car1classpath = prefs.get("car1classpath","System");
        car2classpath = prefs.get("car2classpath","System");

        workDir = prefs.get("workDir",null);
        workDirURL = prefs.get("workDirURL",null);

        world = new PVEWorld(PVEWorld.A3CANVAS);
        world.resume();
        world.addCollisionListener(this);

        gui = new CarBattleGUI(this,carClass1,carClass2);
        gui.pack();
        gui.setVisible(true);

        world.addSubCanvas(gui.car1Canvas);
        world.addSubCanvas(gui.car2Canvas);

        Thread t = new Thread(this);
        pauseRequest = true;
        t.start();
    }

    //clearの処理
    void clearBattle() {
        if (simRunning)
            throw new IllegalStateException();
        if (battleRunning)
            throw new IllegalStateException();
        world.clear();
        activeObjects.clear();
        car1 = null;
        car2 = null;
        classLoader1 = null;
        classLoader2 = null;
        System.gc();
        gui.clearTA();
    }
    void initBattle() {
        if (simRunning)
            throw new IllegalStateException();
        if (battleRunning)
            throw new IllegalStateException();
        //MyGround g = new MyGround(pw);
        //MyGround2 g = new MyGround2(pw);
        MyGround3 g = new MyGround3();
        world.add(g);

        classLoader1 = makeClassLoader(car1classpath);
        classLoader2 = makeClassLoader(car2classpath);

        String carClass1 = gui.car1classTF.getText();
        String carClass2 = gui.car2classTF.getText();
        prefs.put("carClass1",carClass1);
        prefs.put("carClass2",carClass2);
        //try{prefs.flush();}catch(Exception e){;}
        try {
            Class<?> theClass = classLoader1.loadClass(carClass1);
            Class<? extends BattleCarBase> tClass = theClass.asSubclass(BattleCarBase.class);
            car1 = tClass.newInstance();

            theClass = classLoader2.loadClass(carClass2);
            tClass = theClass.asSubclass(BattleCarBase.class);
            car2 = tClass.newInstance();
        } catch(Exception e) {
            System.out.println("Class Load Error!!!");
            e.printStackTrace();
        }

        car1.car.init(new Vector3d( 0,1.0,-10),new Vector3d(),"x-res:///res/stk_tux.a3",world,this);
        car2.car.init(new Vector3d( 0,1.0, 10),new Vector3d(0,3.1,0),"x-res:///res/stk_wilber2.a3",world,this);

        world.add(new MyBox(-10.0,1.0,0.0));
        world.add(new MyBox(-13.0,1.0,0.0));
        world.add(new MyBox(-16.0,1.0,0.0));

        world.add(new MySphere(10,1.0,0.0));
        world.add(new MySphere(13,1.0,0.0));
        world.add(new MySphere(16,1.0,0.0));

        world.add(car1.car.car);
        world.add(car2.car.car);
        gui.setCar1(car1.car);
        gui.setCar2(car2.car);
        activeObjects.add(car1.car);
        activeObjects.add(car2.car);
        gui.updateCar1Info(car1.car);
        gui.updateCar2Info(car2.car);
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
            pauseRequest = false;
            synchronized (waitingRoom) {
                waitingRoom.notifyAll();
            }
            world.resume();
        } else {
            clearBattle();
            initBattle();
            world.resume();
            try{Thread.sleep(100);}catch(Exception e){;}//gaha:落ち着くまで待つ
            pauseRequest = false;
            synchronized (waitingRoom) {
                waitingRoom.notifyAll();
            }
            gui.setParamEditable(false);
            battleRunning = true;
        }
    }
    void pauseBattle() {
        if (!battleRunning)
            return;
        if (simRunning) {
            pauseRequest = true;
            world.pause();
        } else {
            pauseRequest = false;
            synchronized (waitingRoom) {
                waitingRoom.notifyAll();
            }
            world.resume();
        }
    }
    void stopBattle() {
        pauseRequest = true;
        try{Thread.sleep(300);}catch(Exception e){;}
        battleRunning = false;
        clearBattle();
        world.pause();
        gui.setParamEditable(true);
    }
    public void run() {
        ArrayList<ActiveObject> tmp = new ArrayList<ActiveObject>();
        simRunning = true;
        while (true) {
            synchronized (waitingRoom) {
                try {
                    if (pauseRequest) {
                        simRunning = false;
                        waitingRoom.wait();
                        simRunning = true;
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
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
            gui.updateCar1Info(car1.car);
            gui.updateCar2Info(car2.car);

            if ((car1.getEnergy()<=0)||(car2.getEnergy()<=0)) {
                pauseRequest = true;
                world.pause();
                Runnable r = new Runnable() {
                    public void run() {
                        finishBattle();
                    }
                };
                executor.schedule(r,100,TimeUnit.MILLISECONDS);
            }
            try{Thread.sleep(33);}catch(Exception e){;}
        }
    }

    void finishBattle() {
        String message = null;
        if (car1.getEnergy()==car2.getEnergy())
            message = "draw";
        else if (car1.getEnergy()>car2.getEnergy())
            message = "car1 win!";
        else
            message = "car2 win!";
        JOptionPane.showMessageDialog(gui,message);

        stopBattle();
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        if ((a instanceof MyBullet)||(b instanceof MyBullet)) {
            MyBullet bullet = null;
            PVEPart other = null;
            if (a instanceof MyBullet) {
                bullet = (MyBullet)a;
                other = b;
            } else {
                bullet = (MyBullet)b;
                other = a;
            }
            if (other instanceof MyBullet) {
                world.del(other);
                this.delActiveObject((MyBullet)other);
            } else if (other instanceof MyCar) {
                ((MyCar)other).carBase.hit();
            } else if (other instanceof MyGround2){
                ;
            } else {
                ;
            }
            world.del(bullet);
            this.delActiveObject(bullet);
        }
        //System.out.println("gaha a:"+a.a3.getUserData()+" b:"+b.a3.getUserData());
    }

    @Override
    public ArrayList<CarBase> getAllCar() {
        ArrayList<CarBase> ret = new ArrayList<CarBase>();
        ret.add(car1.car);
        ret.add(car2.car);
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
    void changeCP1(String cp) {
        car1classpath = cp;
        prefs.put("car1classpath",car1classpath);
    }
    void changeCP2(String cp) {
        car2classpath = cp;
        prefs.put("car2classpath",car2classpath);
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
}
