package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import jp.sourceforge.acerola3d.a3.*;

public class FreeShapeA extends PVEPart {
    public FreeShapeA(Type type,String a3url) {
    	this(type,1.0,a3url);
    }
    public FreeShapeA(Type type,double mass,String a3url) {
    	super(type,mass,a3url);
    	init();
    }

    protected CollisionShape makeCollisionShape() {
        return Util.makeBvhTriangleMeshShape(a3.getNode());
    }
}
