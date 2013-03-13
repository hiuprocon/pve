package com.github.hiuprocon.pve.car;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.dynamics.vehicle.*;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import jp.sourceforge.acerola3d.a3.*;

//car
public class MyCar extends PVEPart {
    String a3url;
    CarMotion motion;
    RaycastVehicle vehicle;
    public CarBase carBase;
    public MyCar(Vector3d l,Vector3d r,String a3url,DefaultVehicleRaycaster dvr) {
        super(l,r,PartType.DYNAMIC,a3url,dvr);
        //this.a3url = a3url;
        //group = 1;
        //mask = 3;
    }

    public A3Object makeA3Object(Object...args) throws Exception {
        a3url = (String)args[0];
        Action3D myA3 = new Action3D(a3url);
        myA3.setUserData("車");
        return myA3;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        MotionState ms = new CarMotionState(transform);
        return ms;
    }
    public RigidBody makeRigidBody(Object...args) {
        //motionState.setAutoUpdate(false);//MotionデータでコントロールするのでAutoUpdate不要
        motion = new CarMotion(motionState,(DefaultVehicleRaycaster)args[1]);
        vehicle = motion.vehicle;
        ((CarMotionState)motionState).setCarMotion(motion);
        ((Action3D)a3).setMotion("default",motion);
        ((Action3D)a3).transControlUsingRootBone(true);//rootの骨の情報でA3Objectの変換を制御
        return motion.carChassis;
    }
    public TypedConstraint makeConstraint(Object...args) {
    	return vehicle;
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
