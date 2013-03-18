package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import javax.vecmath.*;

public class Cylinder extends PVEPart {
	float height,radius;
	String a3url;
    public Cylinder(Type type,double mass) {
    	this(type,mass,1,1);
    }
    public Cylinder(Type type,double mass,double height,double radius) {
    	this(type,mass,height,radius,"x-res:///res/Cylinder.wrl");
    }
    public Cylinder(Type type,double mass,double height,double radius,String a3url) {
        super(type,mass,a3url);
        this.height = (float)height;
        this.radius = (float)radius;
        this.a3url = a3url;
        init();
        a3.setScaleX(radius);
        a3.setScaleY(height);
        a3.setScaleZ(radius);
    }

    //立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
    	Vector3f size = new Vector3f(radius/2,height/2,radius/2);
        return new CylinderShape(size);
    }
}
