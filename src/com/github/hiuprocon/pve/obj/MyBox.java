package com.github.hiuprocon.pve.obj;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//立方体を表すクラス
public class MyBox extends PVEObject {
    public MyBox(double x,double y,double z) {
        super(new Vector3d(x,y,z),new Vector3d(),ObjType.DYNAMIC);
    }

    public A3Object makeA3Object(Object...args) throws Exception {
    	Action3D myA3 = new Action3D("x-res:///res/SimpleBox.a3"); 
        myA3.setUserData("サイコロ");
        return myA3;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        return new A3MotionState(a3,transform);
    }
    //立方体の剛体を作る
    public RigidBody makeRigidBody(Object...args) {
        CollisionShape shape = new BoxShape(new Vector3f(1.0f,1.0f,1.0f));
        Vector3f localInertia = new Vector3f(0,0,0);
        shape.calculateLocalInertia(1.0f,localInertia);
        RigidBodyConstructionInfo rbcInfo =
                new RigidBodyConstructionInfo(1.0f,motionState,
                                              shape,localInertia);
        RigidBody rb = new RigidBody(rbcInfo);
        return rb;
    }
    public TypedConstraint makeConstraint(Object...args) {
    	return null;
    }
}
