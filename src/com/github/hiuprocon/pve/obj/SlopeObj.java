package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class SlopeObj extends PVEObject {
    Type type;
    String a3url;
    double mass;
    double width;
    double height;
    double depth;
    Slope slope;

    public SlopeObj(double width, double height, double depth) {
        this(Type.STATIC, 0.0, width, height, depth, "x-res:///res/Slope.wrl");
    }

    public SlopeObj(Type type, double mass, double width, double height,
            double depth, String a3url) {
        this.type = type;
        this.mass = mass;
        this.a3url = a3url;
        this.width = width;
        this.height = height;
        this.depth = depth;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        slope = new Slope(type, mass, width, height, depth, a3url);
        // cone.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { slope };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return slope;
    }
}
