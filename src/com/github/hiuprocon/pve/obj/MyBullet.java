package com.github.hiuprocon.pve.obj;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//弾丸を表すクラス
public class MyBullet extends PVEPart implements ActiveObject {
    Vector3d l = new Vector3d();
    Vector3d v = new Vector3d();
    public MyBullet(Vector3d l,Vector3d v) {
        super(l,new Vector3d(),PartType.KINEMATIC);
        this.l.set(l);
        this.v.set(v);
    }

static int gaha;
    public A3Object makeA3Object(Object...args) throws Exception {
        Action3D a = new Action3D("x-res:///res/bullet.a3");
        a.setScale(2.0);
        a.setUserData("bullet"+(gaha++)+":"+System.currentTimeMillis());
        return a;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        return new A3MotionState(a3,transform);
    }
    //球状の剛体を作る
    public RigidBody makeRigidBody(Object...args) {
        CollisionShape shape = new SphereShape(0.2f);
        Vector3f localInertia = new Vector3f(0,0,0);
        shape.calculateLocalInertia(1.0f,localInertia);
        RigidBodyConstructionInfo rbcInfo =
                new RigidBodyConstructionInfo(1.0f,motionState,
                                              shape,localInertia);
        RigidBody rb = new RigidBody(rbcInfo);
        //rb.setCollisionFlags(rb.getCollisionFlags()|CollisionFlags.KINEMATIC_OBJECT);
        //rb.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        return rb;
    }

    boolean initGrav = false;
    public void beforeExec() {
        ;
    }
    public void exec() {
        //l.add(v);
        //this.setLoc2(l.x,l.y,l.z);
        if (initGrav==false) {
            ((RigidBody)body).setGravity(new Vector3f());
            this.setLoc(l.x,l.y,l.z);
            this.setVel(v.x,v.y,v.z);
            initGrav=true;
        }
    }
}
