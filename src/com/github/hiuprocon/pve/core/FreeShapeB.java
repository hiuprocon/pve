package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import jp.sourceforge.acerola3d.a3.*;

//ConvexHullShape
public class FreeShapeB extends PVEPart {
    public FreeShapeB(Type type,String a3url) {
    	this(type,1.0,a3url);
    }
    public FreeShapeB(Type type,double mass,String a3url) {
    	super(type,mass,a3url);
    	init();
    }

    protected CollisionShape makeCollisionShape() {
    	return Util.makeConvexHullShape(a3.getNode());
    }
}
