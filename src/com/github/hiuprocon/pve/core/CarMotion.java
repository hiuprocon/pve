package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.vehicle.*;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import javax.media.j3d.*;

class CarMotion implements Motion {
    VehicleRaycaster vehicleRayCaster;
    RaycastVehicle vehicle;
    Transform rootTransform = new Transform();

    public CarMotion(MotionState ms,DefaultVehicleRaycaster vehicleRayCaster,RigidBody body) {
    	this.vehicleRayCaster = vehicleRayCaster;

        body.setDamping(0.5f,0.5f);//空気抵抗を設定

        // create vehicle
        {
            VehicleTuning tuning = new VehicleTuning();

            vehicle = new RaycastVehicle(tuning, body, vehicleRayCaster);

            //無効化の禁止
            body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);

            //dynamicsWorld.addVehicle(vehicle);

            //座標系の設定
            int rightIndex = 0;
            int upIndex = 1;
            int forwardIndex = 2;
            vehicle.setCoordinateSystem(rightIndex, upIndex, forwardIndex);

            //各タイヤごとのパラメータ
            Vector3f connectionPointCS0 = new Vector3f();//接続ポイント
            Vector3f wheelDirectionCS0 = new Vector3f(0,-1,0);//たぶんサスペンションの方向
            Vector3f wheelAxleCS = new Vector3f(-1,0,0);//車輪の回転軸方向
            float suspensionRestLength;//サスペンションの長さ？
            float wheelRadius;//車輪半径
            boolean isFrontWheel;//前輪かどうか？

            //##### 左前車輪生成 #####
            isFrontWheel = true;
            wheelRadius = 0.1f;
            suspensionRestLength = 0.2f;
            connectionPointCS0.set( 0.3f,0.1f, 0.4f);
            vehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);

            //##### 右前車輪生成 #####
            isFrontWheel = true;
            wheelRadius = 0.1f;
            suspensionRestLength = 0.2f;
            connectionPointCS0.set(-0.3f,0.1f, 0.4f);
            vehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);

            //##### 右後車輪生成 #####
            isFrontWheel = false;
            wheelRadius = 0.15f;
            suspensionRestLength = 0.2f;
            connectionPointCS0.set(-0.35f,0.15f,-0.35f);
            vehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);

            //##### 左後車輪生成 #####
            isFrontWheel = false;
            wheelRadius = 0.15f;
            suspensionRestLength = 0.2f;
            connectionPointCS0.set(0.35f,0.15f,-0.35f);
            vehicle.addWheel(connectionPointCS0, wheelDirectionCS0, wheelAxleCS, suspensionRestLength, wheelRadius, tuning, isFrontWheel);

            //車輪の細かい設定
            float suspensionStiffness = 20.f;//サスペンションの硬さ
            float suspensionDamping = 2.3f;//サスペンションの減衰
            float suspensionCompression = 4.4f;//サスペンションの圧縮?
            float wheelFriction = 1000;//摩擦係数
            float rollInfluence = 0.1f;//回転影響?
            for (int i = 0; i < vehicle.getNumWheels(); i++) {
                WheelInfo wheel = vehicle.getWheelInfo(i);
                wheel.suspensionStiffness = suspensionStiffness;
                wheel.wheelsDampingRelaxation = suspensionDamping;
                wheel.wheelsDampingCompression = suspensionCompression;
                wheel.frictionSlip = wheelFriction;
                wheel.rollInfluence = rollInfluence;
            }
        }
    }

    //車をコントロールするためのメソッド
    public void setForce(float gEngineForce,float gVehicleSteering,float gBreakingForce,float drift) {
        int wheelIndex = 2;
        vehicle.setBrake(gBreakingForce,wheelIndex);
        vehicle.applyEngineForce(gEngineForce,wheelIndex);//後輪駆動の場合
        wheelIndex = 3;
        vehicle.setBrake(gBreakingForce,wheelIndex);
        vehicle.applyEngineForce(gEngineForce,wheelIndex);//後輪駆動の場合

        wheelIndex = 0;
        vehicle.setSteeringValue(gVehicleSteering,wheelIndex);
        //vehicle.applyEngineForce(gEngineForce,wheelIndex);//前輪駆動の場合
        wheelIndex = 1;
        vehicle.setSteeringValue(gVehicleSteering,wheelIndex);
        //vehicle.applyEngineForce(gEngineForce,wheelIndex);//前輪駆動の場合

        WheelInfo wi = null;
        wi = vehicle.getWheelInfo(0);
        wi.frictionSlip = (drift>0.9)?1.5f:1000.0f;
        wi = vehicle.getWheelInfo(1);
        wi.frictionSlip = (drift>0.9)?1.5f:1000.0f;
        wi = vehicle.getWheelInfo(2);
        wi.frictionSlip = (drift>0.9)?1.0f:1000.0f;
        wi = vehicle.getWheelInfo(3);
        wi.frictionSlip = (drift>0.9)?1.0f:1000.0f;

        //float d = 100 * gVehicleSteering * drift;
        //carChassis.applyTorqueImpulse(new Vector3f(0,d,0));
    }

    //public RigidBody localCreateRigidBody(float mass, Transform startTransform, CollisionShape shape) {
    public RigidBody localCreateRigidBody(float mass, MotionState ms, CollisionShape shape) {
        boolean isDynamic = (mass != 0f);

        Vector3f localInertia = new Vector3f(0f, 0f, 0f);
        if (isDynamic) {
            shape.calculateLocalInertia(mass, localInertia);
        }

        //DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
        
        RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, ms, shape, localInertia);
        
        RigidBody body = new RigidBody(cInfo);
        
        //dynamicsWorld.addRigidBody(body);

        return body;
    }
    
    //------------------------
    //以下Acerola3DのMotionインターフェースの実装部
    //------------------------
    public double getMotionLength() {
        return -1.0;
    }
    public double getDefaultFrameTime() {
        return 0.033;
    }
    public String getRootBone() {
        return "root";
    }
    public String getParentBone(String b) {
        if (b.equals("root"))
            return null;
        else
            return "root";
    }
    static final String cbs[] = {"chassis","frontRight","frontLeft","rearRight","rearLeft"};
    public String[] getChildBones(String b) {
        if (b.equals("root")) {
            return cbs;
        } else {
            return new String[0];
        }
    }
    static final String abs[] = {"root","chassis","frontRight","frontLeft","rearRight","rearLeft"};
    public String[] getAllBones() {
        return abs;
    }
    public Transform3D getTransform3D(String bone,double time) {
        //Transform rootTrans = new Transform();
        //vehicle.getChassisWorldTransform(rootTrans);
        Transform rootTrans = new Transform(rootTransform);
        Transform retTrans = new Transform();
        if (bone.equals("root")) {
            retTrans.set(rootTrans);
        } else if (bone.equals("chassis")) {
            retTrans.setIdentity();
        } else {
            int wheelIndex = 0;
            if (bone.equals("frontRight"))
                wheelIndex= 0;//なんで？左右が逆？
            else if (bone.equals("frontLeft"))
                wheelIndex= 1;//なんで？左右が逆？
            else if (bone.equals("rearRight"))
                wheelIndex= 3;//なんで？左右が逆？
            else if (bone.equals("rearLeft"))
                wheelIndex= 2;//なんで？左右が逆？

            Transform rootTransInv = new Transform(rootTrans);
            rootTransInv.inverse();
            Transform trans = vehicle.getWheelInfo(wheelIndex).worldTransform;
            retTrans.mul(rootTransInv,trans);
        }
        //return new Transform3D(retTrans.basis,retTrans.origin,1.0f);
        Quat4d q = Util.matrix2quat(retTrans.basis);
        q.normalize();//???
        return new Transform3D(q,new Vector3d(retTrans.origin),1.0f);
    }

    //車輪の更新
    void updateWheelTransform() {
        for (int i = 0; i < vehicle.getNumWheels(); i++) {
            vehicle.updateWheelTransform(i, true);
        }
    }
}
