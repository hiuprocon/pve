package com.github.hiuprocon.pve.parts;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//立方体を表すクラス
public class Box extends PVEPart {
	float sx,sy,sz;
	String a3url;
    public Box(Type type,Vector3d innerLoc,double mass) {
    	this(type,innerLoc,new Vector3d(),mass);
    }
    public Box(Type type,Vector3d innerLoc,Vector3d innerRot,double mass) {
    	this(type,innerLoc,innerRot,mass,new Vector3d(1,1,1));
    }
    public Box(Type type,Vector3d innerLoc,Vector3d innerRot,double mass,Vector3d size) {
    	this(type,innerLoc,innerRot,mass,size,"x-res:///res/Box.wrl");
    }
    public Box(Type type,Vector3d innerLoc,Vector3d innerRot,double mass,Vector3d size,String a3url) {
        super(type,innerLoc,innerRot,mass);
        this.sx = (float)size.x;
        this.sy = (float)size.y;
        this.sz = (float)size.z;
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
        return new BoxShape(new Vector3f(sx,sy,sz));
    }
}
