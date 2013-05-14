package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;

public class Cone extends PVEPart {
    float height, radius;
    String a3url;

    public Cone(Type type, double mass) {
        this(type, mass, 1, 1);
    }

    public Cone(Type type, double mass, double height, double radius) {
        this(type, mass, height, radius, "x-res:///res/Cone.wrl");
    }

    public Cone(Type type, double mass, double height, double radius,
            String a3url) {
        super(type, mass, a3url);
        this.height = (float) height;
        this.radius = (float) radius;
        this.a3url = a3url;
        init();
        a3.setScaleX(radius);
        a3.setScaleY(height);
        a3.setScaleZ(radius);
    }

    // 立方体の剛体を作る
    public CollisionShape makeCollisionShape() {
        return new ConeShape(radius, height);
    }
}
