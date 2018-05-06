package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import com.bulletphysics.linearmath.Transform;
import javax.vecmath.Vector3f;

//PVEPart#setCenterOfMassTransform(t);ができないので，
//すごく面倒なことになっている．
public class CornerObj extends PVEObject {
    Type type;
    String a3url;
    double mass;
    double sizeX;
    double sizeY;
    double sizeZ;
    Corner corner;

    public CornerObj() {
        this(Type.DYNAMIC, 1.0, 1, 1, 1);
    }
    public CornerObj(Type type, double mass,
                     double sizeX, double sizeY, double sizeZ) {
        this(Type.DYNAMIC, mass, sizeX, sizeY, sizeZ,
             "x-res:///res/Corner.wrl");
    }

    public CornerObj(Type type, double mass,
                     double sizeX, double sizeY, double sizeZ,
                     String a3url) {
        this.type = type;
        this.mass = mass;
        this.a3url = a3url;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        init();
        //Transform t = new Transform();
        //t.setIdentity();
        //t.transform(new Vector3f(?,?,?));
        //corner.setCenterOfMassTransform(t);
    }

    @Override
    protected PVEPart[] createParts() {
        corner = new Corner(type, mass, sizeX, sizeY, sizeZ, a3url);
        corner.setInitLocRot(0.25*sizeX,-0.25*sizeY,0.25*sizeZ, 0,0,0);
        return new PVEPart[] { corner };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return corner;
    }
}
