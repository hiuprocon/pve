package com.github.hiuprocon.pve.core;

import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.linearmath.*;
import static com.bulletphysics.collision.dispatch.CollisionFlags.*;

//このプログラムで剛体を表すクラス
public abstract class PVEPart {
    static final int DYNAMIC_FLAGS = 0;
    static final int STATIC_FLAGS = KINEMATIC_OBJECT | STATIC_OBJECT;
    static final int KINEMATIC_FLAGS = KINEMATIC_OBJECT;
    static final int GHOST_FLAGS = KINEMATIC_OBJECT | NO_CONTACT_RESPONSE;
    static final int KINEMATIC_TEMP_FLAGS = KINEMATIC_OBJECT;

    protected PVEWorld world;
    final Type type;
    public A3Object a3;
    public MotionState motionState;//JBulletと座標をやりとりするオブジェクト
    protected RigidBody body;//JBulletにおける剛体などを表すオブジェクト
    final Vector3d innerLoc;
    final Vector3d innerRot;
    float mass;
    Vector3f locRequest;
    Quat4d quatRequest;
    Vector3f velRequest;
    short group = 1;
    short mask = 1;
    //DYNAMICなんだけど一時的にKINEMATICになってる時にtrue
    boolean kinematicTmp = false;

    //Acerola3DファイルのURLと初期座標で初期化
    public PVEPart(Type type,Vector3d innerLoc,Vector3d innerRot,double mass) {
        this.type = type;
        this.innerLoc = new Vector3d(innerLoc);
        this.innerRot = new Vector3d(innerRot);
        this.mass = (float)mass;
    }

    void internalInit(PVEWorld world) {
        this.world = world;
        try {
            a3 = makeA3Object();
        } catch(Exception e) {
            a3 = new VRML("gaha");
        }
        motionState = makeMotionState(innerLoc,innerRot);
        CollisionShape shape = makeCollisionShape();
        Vector3f localInertia = new Vector3f(0,0,0);
        float massR = type==Type.STATIC?0.0f:mass;
        shape.calculateLocalInertia(massR, localInertia);
        RigidBodyConstructionInfo rbcInfo =
        	new RigidBodyConstructionInfo(mass,motionState,shape,localInertia);
        body = new RigidBody(rbcInfo);
        //constraint = makeConstraint(tmpArgs);
        initRigidBody(type);
        body.setUserPointer(this);
        //DYNAMIC以外なんか最初の座標が表示に反映されないので。。。
        Transform t = new Transform();
        motionState.setWorldTransform(body.getWorldTransform(t));
        init();
    }
    protected abstract A3Object makeA3Object();
    protected abstract MotionState makeMotionState(Vector3d l,Vector3d r);
    protected abstract CollisionShape makeCollisionShape();
    /**
     * A3Object,MotionState,CollisionShapeが生成されRigidBody(body)
     * がの準備が整った後に呼ばれるメソッド。
     */
    protected void init() {;}
    protected void postSimulation() {;}

    void initRigidBody(Type t) {
        if (t==Type.DYNAMIC) {
            body.setCollisionFlags(DYNAMIC_FLAGS);
            //rb.body.setActivationState(CollisionObject.ACTIVE_TAG);
            body.forceActivationState(CollisionObject.ACTIVE_TAG);
            body.setDeactivationTime(0.0f);
            body.clearForces();
            body.setLinearVelocity(new Vector3f());
            body.setAngularVelocity(new Vector3f());
        } else if (t==Type.STATIC) {
            body.setCollisionFlags(STATIC_FLAGS);
            //body.setActivationState(CollisionObject.DISABLE_SIMULATION);
            body.clearForces();
            body.setLinearVelocity(new Vector3f());
            body.setAngularVelocity(new Vector3f());
        } else if (t==Type.KINEMATIC){
            body.setCollisionFlags(KINEMATIC_FLAGS);
            //body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
            body.clearForces();
            body.setLinearVelocity(new Vector3f());
            body.setAngularVelocity(new Vector3f());
        } else if (t==Type.GHOST) {
        	body.setCollisionFlags(GHOST_FLAGS);
            //body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
            body.clearForces();
            body.setLinearVelocity(new Vector3f());
            body.setAngularVelocity(new Vector3f());
        }
    }
    void setKinematicTemp() {
        body.setCollisionFlags(KINEMATIC_TEMP_FLAGS);
        //body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        body.clearForces();
        body.setLinearVelocity(new Vector3f());
        body.setAngularVelocity(new Vector3f());
        kinematicTmp = true;
    }
    void resetKinematicTemp() {
    	if (kinematicTmp==false)
    		return;
        body.setCollisionFlags(DYNAMIC_FLAGS);
        //rb.body.setActivationState(CollisionObject.ACTIVE_TAG);
        body.forceActivationState(CollisionObject.ACTIVE_TAG);
        body.setDeactivationTime(0.0f);
        body.clearForces();
        body.setLinearVelocity(new Vector3f());
        body.setAngularVelocity(new Vector3f());
        kinematicTmp=false;
    }
    //座標変更．副作用で力や速度がリセットされる
    public void setLoc(double x,double y,double z) {
        locRequest = new Vector3f((float)x,(float)y,(float)z);
    }
    //座標変更．副作用で力や速度がリセットされる
    public void setRot(double x,double y,double z) {
        quatRequest = Util.euler2quat(x,y,z);
    }
    //座標変更．副作用で力や速度がリセットされる
    public void setRev(double x,double y,double z) {
        quatRequest = Util.euler2quat(x/180.0*Math.PI,y/180.0*Math.PI,z/180.0*Math.PI);
    }
    public void setVel(double x,double y,double z) {
        velRequest = new Vector3f((float)x,(float)y,(float)z);
    }
}
