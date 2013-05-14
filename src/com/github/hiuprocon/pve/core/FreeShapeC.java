package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import jp.sourceforge.acerola3d.a3.*;

//GImpactMeshShape
//FreeShapeA,FreeShapeCとは衝突しない。FreeShapeBとも不安定。
public class FreeShapeC extends PVEPart {
    public FreeShapeC(Type type, String a3url) {
        this(type, 1.0, a3url);
    }

    public FreeShapeC(Type type, double mass, String a3url) {
        super(type, mass, a3url);
        init();
    }

    protected CollisionShape makeCollisionShape() {
        return Util.makeGImpactMeshShape(a3.getNode());
    }
}
