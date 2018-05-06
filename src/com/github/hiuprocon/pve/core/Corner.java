package com.github.hiuprocon.pve.core;

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.util.ObjectArrayList;

public class Corner extends PVEPart {
    float sizeX, sizeY, sizeZ;
    String a3url;

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
        vertexes.add(new Vector3f(-0.25f*sizeX,-0.25f*sizeY,-0.25f*sizeZ));
        vertexes.add(new Vector3f( 0.75f*sizeX,-0.25f*sizeY,-0.25f*sizeZ));
        vertexes.add(new Vector3f(-0.25f*sizeX, 0.75f*sizeY,-0.25f*sizeZ));
        vertexes.add(new Vector3f(-0.25f*sizeX,-0.25f*sizeY, 0.75f*sizeZ));
        return new ConvexHullShape(vertexes);
    }
}
