package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import javax.vecmath.*;

public class Cylinder extends PVEPart {
    float height, diameter;
    String a3url;

    public Cylinder(Type type, double mass) {
        this(type, mass, 1, 1);
    }

    public Cylinder(Type type, double mass, double height, double diameter) {
        this(type, mass, height, diameter, "x-res:///res/Cylinder.wrl");
    }

    public Cylinder(Type type, double mass, double height, double diameter,
            String a3url) {
        super(type, mass, a3url);
        this.height = (float) height;
        this.diameter = (float) diameter;
        this.a3url = a3url;
        init();
        a3.setScaleX(diameter);
        a3.setScaleY(height);
        a3.setScaleZ(diameter);
    }

    public CollisionShape makeCollisionShape() {
        Vector3f size = new Vector3f(diameter / 2, height / 2, diameter / 2);
        return new CylinderShape(size);
    }
}
