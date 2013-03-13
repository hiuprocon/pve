package com.github.hiuprocon.pve.car;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.PVEPart;
import com.github.hiuprocon.pve.core.A3MotionState;
import com.github.hiuprocon.pve.core.PartType;

public class MyCheckPoint extends PVEPart {
    public MyCheckPoint(Vector3d l,Vector3d r) {
        super(PartType.GHOST,l,r);
        //group = 2;
        //mask = 2;
    }

    public A3Object makeA3Object(Object...args) throws Exception {
        VRML vrml = new VRML("x-rzip:x-res:///res/ClearBlocks2.a3!/blockBlack.wrl");
        vrml.setScale(8);
        vrml.setUserData("CheckPoint");
        return vrml;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        return new A3MotionState(a3,transform);
    }

    public RigidBody makeRigidBody(Object...args) {
        //CollisionShape shape = Util.makeConvexHullShape(a3.getNode());
        CollisionShape shape = new BoxShape(new Vector3f(4,4,2));
        Vector3f localInertia = new Vector3f(0,0,0);
        shape.calculateLocalInertia(0.0f,localInertia);
        RigidBodyConstructionInfo rbcInfo =
            new RigidBodyConstructionInfo(0.0f,motionState,shape,localInertia);
        RigidBody rb = new RigidBody(rbcInfo);
        return rb;
    }

    /*
    //GhostObjectバージョンl
    public RigidBody makeRigidBody_BAK(Object...args) {
        //CollisionShape shape = Util.makeConvexHullShape(a3.getNode());
        CollisionShape shape = new BoxShape(new Vector3f(40,40,20));
        GhostObject body = new GhostObject();
        body.setCollisionShape(shape);
        body.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);//ポイント
        Transform t = new Transform();
        body.setWorldTransform(t);
        a3.setLocImmediately(t.origin.x,t.origin.y,t.origin.z);
        a3.setQuat(Util.matrix2quat(t.basis));
        return body;
    }
    */
    public TypedConstraint makeConstraint(Object...args) {
    	return null;
    }
}
