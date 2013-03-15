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

    PVEObject obj;
    final Type type;
    protected A3Object a3;
    public MotionState motionState;//JBulletと座標をやりとりするオブジェクト
    protected RigidBody body;//JBulletにおける剛体などを表すオブジェクト
    Vector3d innerLoc = new Vector3d();
    Vector3d innerRot = new Vector3d();
    float mass;
    Vector3f localInertia;
    Vector3f locRequest;
    Quat4d quatRequest;
    Vector3f velRequest;
    short group = 1;
    short mask = 1;
    //DYNAMICなんだけど一時的にKINEMATICになってる時にtrue
    boolean kinematicTmp = false;

    //Acerola3DファイルのURLと初期座標で初期化
    public PVEPart(Type type,double mass,String a3url) {
    	this(type,mass,a3url,new Vector3d());
    }
    public PVEPart(Type type,double mass,String a3url,Vector3d localInertia) {
    	this(type,mass,a3url,localInertia,null);
    }
    public PVEPart(Type type,double mass,String a3url,Vector3d localInertia,PVEWorld world) {
    	//worldがnullでないのはSimpleCarの場合のみと想定している
    	if (world!=null) {
    		((SimpleCar)this).world = world;
    	}
        this.type = type;
        this.mass = (float)mass;
        a3 = PVEUtil.loadA3(a3url);
        this.localInertia = new Vector3f(localInertia);
    }
    /**
     * このメソッドはコンストラクタの中で必ず呼び出さなければならない。
     */
    protected final void init() {
        Transform t = new Transform();
        t.setIdentity();
        if (this instanceof SimpleCar)
        	motionState = new CarMotionState(t);
        else
        	motionState = new A3MotionState(a3,t);
        CollisionShape shape = makeCollisionShape();
        float massR = type==Type.DYNAMIC?mass:0.0f;
        shape.calculateLocalInertia(massR,localInertia);
        RigidBodyConstructionInfo rbcInfo =
        	new RigidBodyConstructionInfo(massR,motionState,shape,this.localInertia);
        body = new RigidBody(rbcInfo);
        body.setUserPointer(this);
        initRigidBody(type);
    }
    protected abstract CollisionShape makeCollisionShape();
    public void setInitLocRot(Vector3d l,Vector3d r) {
    	innerLoc.set(l);
    	innerRot.set(r);
        Transform t = new Transform();
        t.origin.set(innerLoc);
        t.setRotation(new Quat4f(Util.euler2quat(innerRot)));
        body.setWorldTransform(t);
        motionState.setWorldTransform(body.getWorldTransform(t));
    }
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
    public void setLoc(Vector3d loc) {
    	locRequest = new Vector3f(loc);
    }
    public void setLoc(double x,double y,double z) {
        locRequest = new Vector3f((float)x,(float)y,(float)z);
    }
    //座標変更．副作用で力や速度がリセットされる
    public void setRot(Vector3d rot) {
    	quatRequest = Util.euler2quat(rot);
    }
    public void setRot(double x,double y,double z) {
        quatRequest = Util.euler2quat(x,y,z);
    }
    //座標変更．副作用で力や速度がリセットされる
    public void setRev(Vector3d rev) {
    	rev.scale(180.0/Math.PI);
    	quatRequest = Util.euler2quat(rev);
    }
    public void setRev(double x,double y,double z) {
        quatRequest = Util.euler2quat(x/180.0*Math.PI,y/180.0*Math.PI,z/180.0*Math.PI);
    }
    public void setVel(Vector3d vel) {
    	velRequest = new Vector3f(vel);
    }
    public void setVel(double x,double y,double z) {
        velRequest = new Vector3f((float)x,(float)y,(float)z);
    }
    public Vector3d getLoc() {
    	Transform t = new Transform();
    	motionState.getWorldTransform(t);
    	return new Vector3d(t.origin);
    }
    public Quat4d getQuat() {
    	Transform t = new Transform();
    	motionState.getWorldTransform(t);
    	return Util.matrix2quat(t.basis);
    }
    public Vector3d getRot() {
    	return Util.quat2euler(getQuat());
    }
    public PVEObject getObject() {
    	return obj;
    }
    public void setGravity(Vector3d g) {
    	body.setGravity(new Vector3f(g));
    }
}
