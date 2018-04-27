package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class BoxObj extends PVEObject {
    Type type;
    String a3url;
    double mass;
    Vector3d size;
    Box box;

    public BoxObj() {
        this(Type.DYNAMIC, 1.0, 1, 1, 1, "x-res:///res/Box.wrl");
    }

    public BoxObj(Type type, double mass, double xs, double ys, double zs) {
        this(type,mass,xs,ys,zs,"x-res:///res/Box.wrl");
    }

    public BoxObj(Type type, double mass, Vector3d size, String a3url) {
        this(type,mass,size.x,size.y,size.z,a3url);
    }

    public BoxObj(Type type, double mass,double xs, double ys, double zs,
                  String a3url) {
        this.type = type;
        this.mass = mass;
        this.a3url = a3url;
        this.size = new Vector3d(xs,ys,zs);
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        box = new Box(type, mass, size, a3url);
        // box.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { box };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return box;
    }
}
