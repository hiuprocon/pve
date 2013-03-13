package com.github.hiuprocon.pve.parts;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//立方体を表すクラス
public class Sphere extends PVEPart {
	float size;
	String a3url;
    public Sphere(Type type,Vector3d innerLoc,double mass) {
    	this(type,innerLoc,new Vector3d(),mass);
    }
    public Sphere(Type type,Vector3d innerLoc,Vector3d innerRot,double mass) {
    	this(type,innerLoc,innerRot,mass,1.0);
    }
    public Sphere(Type type,Vector3d innerLoc,Vector3d innerRot,double mass,double size) {
    	this(type,innerLoc,innerRot,mass,size,"x-res:///res/Sphere.wrl");
    }
    public Sphere(Type type,Vector3d innerLoc,Vector3d innerRot,double mass,double size,String a3url) {
        super(type,innerLoc,innerRot,mass);
        this.size = (float)size;
        this.a3url = a3url;
    }

    public A3Object makeA3Object() {
    	A3Object a = PVEUtil.loadA3(a3url);
        a.setUserData("サイコロ");
        return a;
    }
    public MotionState makeMotionState(Vector3d l,Vector3d r) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set((float)l.x,(float)l.y,(float)l.z);
        transform.setRotation(new Quat4f(Util.euler2quat(r)));
        return new A3MotionState(a3,transform);
    }
    //立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
        return new SphereShape(size);
    }
}