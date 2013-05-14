package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class Ground extends PVEObject {
    String a3url;
    FreeShapeA g;

    public Ground() {
        this("x-res:///res/background0.a3");
    }

    public Ground(String a3url) {
        this.a3url = a3url;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        g = new FreeShapeA(Type.STATIC, a3url);
        // g.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { g };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return g;
    }
}