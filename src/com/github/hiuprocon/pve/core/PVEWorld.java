package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.broadphase.*;
import com.bulletphysics.collision.dispatch.*;
//import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.*;
import com.bulletphysics.dynamics.vehicle.DefaultVehicleRaycaster;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import java.util.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//物理計算をしてくれるクラス
public class PVEWorld implements Runnable {
    enum CanvasType {
        A3CANVAS, JA3CANVAS, A3WINDOW, JA3WINDOW
    }
    enum SimType {
        AUTO_STEP,
        MANUAL_STEP
    }

    public static CanvasType A3CANVAS = CanvasType.A3CANVAS;
    public static CanvasType JA3CANVAS = CanvasType.JA3CANVAS;
    public static CanvasType A3WINDOW = CanvasType.A3WINDOW;
    public static CanvasType JA3WINDOW = CanvasType.JA3WINDOW;

    public static SimType AUTO_STEP = SimType.AUTO_STEP;
    public static SimType MANUAL_STEP = SimType.MANUAL_STEP;

    static int MAX_PROXIES = 1024;
    public DiscreteDynamicsWorld dynamicsWorld;
    ArrayList<PVEObject> objects = new ArrayList<PVEObject>();
    ArrayList<PVEObject> newObjects = new ArrayList<PVEObject>();
    ArrayList<PVEObject> delObjects = new ArrayList<PVEObject>();
    A3CanvasInterface mainCanvas;
    ArrayList<A3CanvasInterface> subCanvases = new ArrayList<A3CanvasInterface>();
    ArrayList<CollisionListener> collisionListeners = new ArrayList<CollisionListener>();
    ArrayList<Runnable> tasks = new ArrayList<Runnable>();
    Object waitingRoom = new Object();
    boolean pauseRequest = true;
    double time;
    boolean fastForward = false;
    public final float stepTime = 1.0f / 30.0f;
    final long waitTime = 33;
    SimType simType;

    // 物理世界の初期化
    public PVEWorld(CanvasType canvasType) {
        this(canvasType,SimType.AUTO_STEP);
    }
    public PVEWorld(CanvasType canvasType,SimType simType) {
        // mainCanvas = new A3Window(500,500);
        this.simType = simType;

        makeDynamicsWorld();
        createMainCanvas(canvasType);

        time = 0.0;
        if (simType==SimType.AUTO_STEP) { 
            Thread t = new Thread(this);
            t.start();
        }
    }

    void makeDynamicsWorld() {
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(
                collisionConfiguration);
        /*
         * Vector3f worldAabbMin = new Vector3f(-10000,-10000,-10000); Vector3f
         * worldAabbMax = new Vector3f(10000,10000,10000); int maxProxies =
         * MAX_PROXIES; AxisSweep3 overlappingPairCache = new
         * AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
         * SequentialImpulseConstraintSolver solver = new
         * SequentialImpulseConstraintSolver();
         */
        BroadphaseInterface overlappingPairCache = new DbvtBroadphase();
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher,
                overlappingPairCache, solver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
    }

    void createMainCanvas(CanvasType canvasType) {
        if (canvasType == A3CANVAS) {
            mainCanvas = A3Canvas.createA3Canvas(300, 300);
        } else if (canvasType == JA3CANVAS) {
            mainCanvas = JA3Canvas.createJA3Canvas(300, 300);
        } else if (canvasType == A3WINDOW) {
            mainCanvas = new A3Window(300, 300);
        } else if (canvasType == JA3WINDOW) {
            mainCanvas = new JA3Window(300, 300);
        }
        mainCanvas.setUpdateInterval(waitTime);
        for (PVEObject o : objects)
            for (PVEPart p : o.parts)
                mainCanvas.add(p.a3);
    }

    public A3CanvasInterface getMainCanvas() {
        return mainCanvas;
    }

    public void addSubCanvas(A3CanvasInterface c) {
        mainCanvas.addA3SubCanvas(c);
    }

    // 新規の剛体を加える
    public void add(PVEObject o) {
        synchronized (newObjects) {
            newObjects.add(o);
        }
    }

    // 既存の剛体を削除
    public void del(PVEObject o) {
        synchronized (delObjects) {
            delObjects.add(o);
        }
    }

    public void pause() {
        pauseRequest = true;
    }

    public void resume() {
        pauseRequest = false;
        synchronized (waitingRoom) {
            waitingRoom.notifyAll();
        }
    }

    public void clear() {
        pauseRequest = true;
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            ;
        }
        if (mainCanvas != null) {
            mainCanvas.delAll();
            // for (A3CollisionObject co : objects)
            // mainCanvas.del(co.a3);
        }
        for (PVEObject o : objects)
            for (PVEPart p : o.parts)
                dynamicsWorld.removeRigidBody(p.body);

        objects.clear();
        newObjects.clear();
        delObjects.clear();
        time = 0.0;

        makeDynamicsWorld();// これでやっと毎回同じタイムになった。
    }

    // 物理計算を進める処理
    // 座標を変更するのがちょっとやっかい
    public void run() {
        while (true) {
            stepForward();
            if (fastForward == false) {
                if (mainCanvas != null) {
                    mainCanvas.waitForUpdate(waitTime * 2);
                    try {
                        Thread.sleep(waitTime / 2);
                    } catch (Exception e) {
                        ;
                    }// 微妙
                } else {
                    try {
                        Thread.sleep(waitTime);
                    } catch (Exception e) {
                        ;
                    }
                }
            }
        }
    }

    public void stepForward() {
        synchronized (waitingRoom) {
            if (pauseRequest == true) {
                try {
                    waitingRoom.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        synchronized (newObjects) {
            for (PVEObject o : newObjects) {
                for (PVEPart p : o.parts) {
                    dynamicsWorld.addRigidBody(p.body, p.group, p.mask);
                    if (mainCanvas != null)
                        mainCanvas.add(p.a3);
                }
                for (Constraint c : o.constraints)
                    if (c.con instanceof RaycastVehicle)
                        dynamicsWorld.addVehicle((RaycastVehicle) (c.con));
                    else
                        dynamicsWorld.addConstraint(c.con,
                                c.disableCollisionsBetweenLinkedBodies);
                objects.add(o);
            }
            newObjects.clear();
        }

        synchronized (delObjects) {
            for (PVEObject o : delObjects) {
                for (PVEPart p : o.parts) {
                    dynamicsWorld.removeRigidBody(p.body);
                    if (mainCanvas != null)
                        mainCanvas.del(p.a3);
                }
                for (Constraint c : o.constraints)
                    if (c.con instanceof RaycastVehicle)
                        dynamicsWorld
                                .removeVehicle((RaycastVehicle) (c.con));
                    else
                        dynamicsWorld.removeConstraint(c.con);
                objects.remove(o);
            }
            delObjects.clear();
        }

        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                if ((p.locRequest == null) && (p.quatRequest == null))
                    continue;
                Transform t = new Transform();
                t = p.motionState.getWorldTransform(t);
                if (p.locRequest != null)
                    t.origin.set(p.locRequest);
                if (p.quatRequest != null)
                    t.setRotation(new Quat4f(p.quatRequest));
                p.motionState.setWorldTransform(t);
                if (p.type == Type.DYNAMIC)
                    p.setKinematicTemp();
            }
        }

        // System.out.println("PhysicalWorld:-----gaha-----1");
        // ここで物理計算
        dynamicsWorld.stepSimulation(stepTime, 10);
        // dynamicsWorld.stepSimulation(stepTime,2);
        time += stepTime;
        // System.out.println("PhysicalWorld:-----gaha-----2");

        // 衝突
        int numManifolds = dynamicsWorld.getDispatcher().getNumManifolds();
        for (int ii = 0; ii < numManifolds; ii++) {
            PersistentManifold contactManifold = dynamicsWorld
                    .getDispatcher().getManifoldByIndexInternal(ii);
            int numContacts = contactManifold.getNumContacts();
            if (numContacts == 0)
                continue;
            CollisionObject obA = (CollisionObject) contactManifold
                    .getBody0();
            CollisionObject obB = (CollisionObject) contactManifold
                    .getBody1();
            /*
             * for (int j=0;j<numContacts;j++) { ManifoldPoint pt =
             * contactManifold.getContactPoint(j); if
             * (pt.getDistance()<0.0f) {
             * System.out.println("-----------------");
             * System.out.println("ii:"+ii+"    j:"+j);
             * System.out.println("getLifeTime:"+pt.getLifeTime());
             * System.out.println("PositionWorldOnA:"+pt.positionWorldOnA);
             * System.out.println("PositionWorldOnB:"+pt.positionWorldOnB);
             * System.out.println("normalWorldOnB:"+pt.normalWorldOnB);
             * System.out.println("-----------------"); } }
             */

            // ロックしすぎ？
            synchronized (collisionListeners) {
                for (CollisionListener cl : collisionListeners) {
                    cl.collided(((PVEPart) obA.getUserPointer()),
                            ((PVEPart) obB.getUserPointer()));
                }
            }
        }

        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                if (p.locRequest == null && p.quatRequest == null)
                    continue;
                p.resetKinematicTemp();
                p.locRequest = null;
                p.quatRequest = null;
            }
        }

        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                if (p.velRequest == null)
                    continue;
                p.body.setLinearVelocity(p.velRequest);
                p.velRequest = null;
            }
        }

        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                p.postSimulation();
                if (p.disableDeactivation == true)
                    p.body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);// 毎回必要みたい
            }
            o.postSimulation();
        }

        /*
         * //光線テストの実験 RayResultCallback rayRC = new
         * CollisionWorld.ClosestRayResultCallback(new
         * Vector3f(0,0.5f,0),new Vector3f(0,0.5f,5));
         * dynamicsWorld.rayTest(new Vector3f(0,0.5f,0), new
         * Vector3f(0,0.5f,5), rayRC);
         * System.out.println("gaha:"+rayRC.hasHit());
         */
         //光線テストgaha
        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                if (p instanceof Radar) {
                    processRadar((Radar)p);
                } else if (p instanceof Compound) {
                    for (PVEPart pp : ((Compound)p).parts) {
                        if (pp instanceof Radar) {
                            processRadar((Radar)pp);
                        }
                    }
                }
            }
            o.postSimulation();
        }

        synchronized (tasks) {
            for (Runnable r : tasks) {
                r.run();
            }
        }
    }

    void processRadar(Radar r) {
        Vector3f fromV = new Vector3f();
        Vector3f toV = new Vector3f();
        ClosestRayResultCallback rayRC = null;
        RigidBody rb = null;

        r.getFromAndTo(fromV,toV);
        rayRC = new ClosestRayResultCallback(fromV,toV);
        dynamicsWorld.rayTest(fromV,toV,rayRC);
        if (rayRC.hasHit()) {
            rb = RigidBody.upcast(rayRC.collisionObject);
            if (rb!=null) {
                fromV.sub(rayRC.hitPointWorld);
                r.distance = fromV.length();
            }
        } else {
            r.distance = Double.MAX_VALUE;
        }
    }

    public void addCollisionListener(CollisionListener cl) {
        synchronized (collisionListeners) {
            collisionListeners.add(cl);
        }
    }

    public void removeCollisionListener(CollisionListener cl) {
        synchronized (collisionListeners) {
            collisionListeners.remove(cl);
        }
    }

    public double getTime() {
        return time;
    }

    public void addTask(Runnable r) {
        synchronized (tasks) {
            tasks.add(r);
        }
    }

    public void removeTask(Runnable r) {
        synchronized (tasks) {
            tasks.remove(r);
        }
    }

    /*
     * やっぱりこれは危険 public void setWaitTime(long t) { waitTime = t; }
     */
    public void fastForward(boolean b) {
        fastForward = b;
    }

    public DefaultVehicleRaycaster createDefaultVehicleRaycaster() {
        return new DefaultVehicleRaycaster(dynamicsWorld);
    }
    public void polygonize() {
        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                p.getA3Object().polygonize();
            }
        }
    }
    public void unpolygonize() {
        for (PVEObject o : objects) {
            for (PVEPart p : o.parts) {
                p.getA3Object().unpolygonize();
            }
        }
    }
}
