package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.*;
import com.bulletphysics.dynamics.vehicle.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

public class SimpleCar extends PVEPart {
    PVEWorld world;
    CarMotion motion;
    public RaycastVehicle vehicleConstraint;
    public SimpleCar(PVEWorld world) {
    	this(world,"x-res:///res/stk_tux.a3");
    }
    public SimpleCar(PVEWorld world,String a3url) {
        super(Type.DYNAMIC,100,a3url,new Vector3d(),world);
        //group = 1;
        //mask = 3;
        init();
        //motionState.setAutoUpdate(false);//MotionデータでコントロールするのでAutoUpdate不要
        motion = new CarMotion(motionState,world,body);
        vehicleConstraint = motion.vehicle;
        ((CarMotionState)motionState).setCarMotion(motion);
        ((Action3D)a3).setMotion("default",motion);
        ((Action3D)a3).transControlUsingRootBone(true);//rootの骨の情報でA3Objectの変換を制御
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
    public void setForce(double gEngineForce,double gVehicleSteering,double gBreakingForce,double drift) {
        motion.setForce((float)gEngineForce,(float)gVehicleSteering,(float)gBreakingForce,(float)drift);
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
