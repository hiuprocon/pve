package com.github.hiuprocon.pve.obj;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Seesaw extends PVEObject {
    Box plate;
    Box rightLeg;
    Box leftLeg;
    Hinge rightHinge;
    Hinge leftHinge;

    public Seesaw() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        plate = new Box(Type.DYNAMIC, 10.0, new Vector3d(3, 0.1, 20));
        rightLeg = new Box(Type.STATIC, 0.0, new Vector3d(0.1, 1.0, 1.0));
        leftLeg = new Box(Type.STATIC, 0.0, new Vector3d(0.1, 1.0, 1.0));
        plate.setInitLocRev(0, 1, -2, 0, 0, 0);
        rightLeg.setInitLocRev(1.55, 0.5, 0, 0, 0, 0);
        leftLeg.setInitLocRev(-1.55, 0.5, 0, 0, 0, 0);
        return new PVEPart[] { plate, rightLeg, leftLeg };
    }

    @Override
    protected Constraint[] createConstraints() {
        rightHinge = new Hinge(plate, rightLeg, new Vector3d(1.5, 1, 0),
                new Vector3d(1, 0, 0));
        leftHinge = new Hinge(plate, leftLeg, new Vector3d(-1.5, 1, 0),
                new Vector3d(1, 0, 0));
        rightHinge.disableCollisionsBetweenLinkedBodies = true;
        leftHinge.disableCollisionsBetweenLinkedBodies = true;
        return new Constraint[] { rightHinge, leftHinge };
    }

    @Override
    protected PVEPart getMainPart() {
        return plate;
    }
}
