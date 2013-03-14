package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import jp.sourceforge.acerola3d.a3.*;

public class FreeShapeA extends PVEPart {
    public FreeShapeA(Type type,String a3url) {
    	super(type,1.0,a3url);
    }

    protected CollisionShape makeCollisionShape() {
        return Util.makeBvhTriangleMeshShape(a3.getNode());
    }
}
