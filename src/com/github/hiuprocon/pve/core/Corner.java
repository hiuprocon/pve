package com.github.hiuprocon.pve.core;

import jp.sourceforge.acerola3d.a3.*;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.util.ObjectArrayList;
import javax.vecmath.*;

public class Corner extends FreeShapeB {
    float sizeX = 1.0f;
    float sizeY = 1.0f;
    float sizeZ = 1.0f;

    public Corner(Type type, double mass) {
        this(type, mass, 1, 1, 1);
    }

    public Corner(Type type, double mass,
                  double sizeX, double sizeY, double sizeZ) {
        this(type, mass, sizeX, sizeY, sizeZ, "x-res:///res/Corner.wrl");
    }

    public Corner(Type type, double mass,
                  double sizeX, double sizeY, double sizeZ,
                  String a3url) {
        super(type, mass, a3url);
        this.sizeX = (float) sizeX;
        this.sizeY = (float) sizeY;
        this.sizeZ = (float) sizeZ;
        this.a3url = a3url;
        init();
        a3.setScaleX(sizeX);
        a3.setScaleY(sizeY);
        a3.setScaleZ(sizeZ);
    }

    public CollisionShape makeCollisionShape() {
        ObjectArrayList<Vector3f> vertexes = new ObjectArrayList<Vector3f>();
        vertexes.add(new Vector3f( 0.0f,-0.5f*sizeY, 0.0f));
        vertexes.add(new Vector3f(sizeX,-0.5f*sizeY, 0.0f));
        vertexes.add(new Vector3f( 0.0f, 0.5f*sizeY, 0.0f));
        vertexes.add(new Vector3f( 0.0f,-0.5f*sizeY,sizeZ));
        return new ConvexHullShape(vertexes);
    }
}
