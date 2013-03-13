package com.github.hiuprocon.pve.parts;


import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.*;
import com.bulletphysics.dynamics.vehicle.*;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.car.CarBase;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//car
public class SimpleCar extends PVEPart {
    String a3url;
    CarMotion motion;
    public RaycastVehicle vehicleConstraint;
    public CarBase carBase;
    public SimpleCar() {
    	this(new Vector3d(),new Vector3d());
    }
    public SimpleCar(Vector3d l,Vector3d r) {
    	this(l,r,"x-res:///res/stk_tux.a3");
    }
    public SimpleCar(Vector3d l,Vector3d r,String a3url) {
        super(Type.DYNAMIC,l,r,100);
        this.a3url = a3url;
        //group = 1;
        //mask = 3;
    }

    protected A3Object makeA3Object() {
        A3Object a = PVEUtil.loadA3(a3url);
        a.setUserData("車");
        return a; 
    }
    protected MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        MotionState ms = new CarMotionState(transform);
        return ms;
    }
    protected CollisionShape makeCollisionShape() {
        CollisionShape chassisShape = new BoxShape(new Vector3f(0.4f, 0.25f, 0.75f));
        CompoundShape compound = new CompoundShape();
        Transform localTrans = new Transform();
        localTrans.setIdentity();
        localTrans.origin.set(0, 0.25f, 0);
        compound.addChildShape(localTrans, chassisShape);
    	return compound;
    }
    protected void init() {
    	DefaultVehicleRaycaster dvr = world.createDefaultVehicleRaycaster();
        //motionState.setAutoUpdate(false);//MotionデータでコントロールするのでAutoUpdate不要
        motion = new CarMotion(motionState,dvr,body);
        vehicleConstraint = motion.vehicle;
        ((CarMotionState)motionState).setCarMotion(motion);
        ((Action3D)a3).setMotion("default",motion);
        ((Action3D)a3).transControlUsingRootBone(true);//rootの骨の情報でA3Objectの変換を制御
    }
    public TypedConstraint makeConstraint(Object...args) {
    	return vehicleConstraint;
    }
    public void setCarBase(CarBase cb) {
        carBase = cb;
    }
    public void setForce(float gEngineForce,float gVehicleSteering,float gBreakingForce,float drift) {
        motion.setForce(gEngineForce,gVehicleSteering,gBreakingForce,drift);
    }

    @Override
    protected void postSimulation() {
    	updateWheelTransform();
    }
    //車輪の更新
    void updateWheelTransform() {
        motion.updateWheelTransform();
    }
}
