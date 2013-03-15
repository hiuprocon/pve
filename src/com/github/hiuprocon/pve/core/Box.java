package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import javax.vecmath.*;

//立方体を表すクラス
public class Box extends PVEPart {
	float sx,sy,sz;
	String a3url;
    public Box(Type type,double mass) {
    	this(type,mass,new Vector3d(1,1,1));
    }
    public Box(Type type,double mass,Vector3d size) {
    	this(type,mass,size,"x-res:///res/Box.wrl");
    }
    public Box(Type type,double mass,Vector3d size,String a3url) {
        super(type,mass,a3url);
        this.sx = (float)size.x;
        this.sy = (float)size.y;
        this.sz = (float)size.z;
        this.a3url = a3url;
        init();
        a3.setScaleX(sx/2);
        a3.setScaleY(sy/2);
        a3.setScaleZ(sz/2);
    }

    //立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
        return new BoxShape(new Vector3f(sx/2,sy/2,sz/2));
    }
}
