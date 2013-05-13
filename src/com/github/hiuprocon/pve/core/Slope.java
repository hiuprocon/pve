package com.github.hiuprocon.pve.core;

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.util.ObjectArrayList;

public class Slope extends PVEPart {
	float width,height,depth;
	String a3url;
    public Slope(Type type,double mass) {
    	this(type,mass,1,1,1);
    }
    public Slope(Type type,double mass,double width,double height,double depth) {
    	this(type,mass,width,height,depth,"x-res:///res/Slope.wrl");
    }
    public Slope(Type type,double mass,double width,double height,double depth,String a3url) {
        super(type,mass,a3url);
        this.width = (float)width;
        this.height = (float)height;
        this.depth = (float)depth;
        this.a3url = a3url;
        init();
        a3.setScaleX(width);
        a3.setScaleY(height);
        a3.setScaleZ(depth);
    }

    //立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
        ObjectArrayList<Vector3f> vertexes = new ObjectArrayList<Vector3f>();
        vertexes.add(new Vector3f( 0.5f*width,-0.5f*height, 0.5f*depth));
        vertexes.add(new Vector3f( 0.5f*width,-0.5f*height,-0.5f*depth));
        vertexes.add(new Vector3f(-0.5f*width,-0.5f*height,-0.5f*depth));
        vertexes.add(new Vector3f(-0.5f*width,-0.5f*height, 0.5f*depth));
        vertexes.add(new Vector3f( 0.5f*width, 0.5f*height,-0.5f*depth));
        vertexes.add(new Vector3f(-0.5f*width, 0.5f*height,-0.5f*depth));
        ConvexHullShape chs = new ConvexHullShape(vertexes);
        return chs;
    }
}
