package com.github.hiuprocon.pve.parts;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

public class FreeShapeA extends PVEPart {
	Type type;
	String a3url;
    public FreeShapeA(Type type,String a3url) {
    	this.type = type;
    	this.a3url = a3url;
    }

    public A3Object makeA3Object(Object...args) throws Exception {
        VRML vrml = new VRML("x-rzip:x-res:///res/stk_racetrack.a3!/racetrack.wrl");
        vrml.setUserData("地面2");
        return vrml;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(PVEUtil.euler2quat(r)));
        return new A3MotionState(a3,transform);
    }
    //地面用の剛体を作る
    public RigidBody makeRigidBody(Object...args) {
        CollisionShape groundShape = PVEUtil.makeBvhTriangleMeshShape(a3.getNode());
        RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(0.0f, motionState, groundShape, new Vector3f());
        RigidBody body = new RigidBody(cInfo);
        return body;
    }
}
