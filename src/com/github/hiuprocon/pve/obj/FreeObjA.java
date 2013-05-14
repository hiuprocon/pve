package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class FreeObjA extends PVEObject {
    Type type;
    String a3url;
    double mass;
    FreeShapeA shapeA;

    public FreeObjA(Type type, String a3url) {
        this(type, 1.0, a3url);
    }

    public FreeObjA(Type type, double mass, String a3url) {
        this.type = type;
        this.mass = mass;
        this.a3url = a3url;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        shapeA = new FreeShapeA(type, mass, a3url);
        // shapeA.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { shapeA };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return shapeA;
    }
}