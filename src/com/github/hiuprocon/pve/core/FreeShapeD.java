package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;

//JBulletのCollisionShapeを直接使いたい時の物
//特にCompoundShapeでConcaveなやつをつくりたい時に重宝するはず．
public class FreeShapeD extends PVEPart {
    CollisionShape cs;
    public FreeShapeD(Type type,String a3url,CollisionShape cs) {
    	this(type,1.0,a3url,cs);
    }
    public FreeShapeD(Type type,double mass,String a3url,CollisionShape cs) {
    	super(type,mass,a3url);
        this.cs = cs;
    	init();
    }

    protected CollisionShape makeCollisionShape() {
    	return cs;
    }
}
