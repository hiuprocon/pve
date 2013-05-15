package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class CylinderObj extends PVEObject {
    Type type;
    String a3url;
    double mass;
    double height;
    double diameter;
    Cylinder cylinder;

    public CylinderObj() {
        this(Type.DYNAMIC, 1.0, 1, 1, "x-res:///res/Cylinder.wrl");
    }

    public CylinderObj(Type type, double mass, double height, double diameter,
            String a3url) {
        this.type = type;
        this.mass = mass;
        this.a3url = a3url;
        this.height = height;
        this.diameter = diameter;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        cylinder = new Cylinder(type, mass, height, diameter, a3url);
        // cylinder.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { cylinder };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return cylinder;
    }
}