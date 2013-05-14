package com.github.hiuprocon.pve.obj;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.github.hiuprocon.pve.core.*;

public class Hovercraft extends PVEObject {
    Box chassis;

    public Hovercraft() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        chassis = new Box(Type.DYNAMIC, 80.0, new Vector3d(3, 1, 4));
        chassis.setInitLocRev(0, 0.5, 0, 0, 0, 0);
        chassis.disableDeactivation(true);
        chassis.setDamping(0.5, 0.0);
        // chassis.setAngularFactor(0);
        return new PVEPart[] { chassis };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[] {};
    }

    @Override
    protected PVEPart getMainPart() {
        return chassis;
    }

    public void drive(double speed, double handle) {
        Quat4d q = chassis.getQuat();
        Vector3d front = Util.trans(q, new Vector3d(0, 0, 1));
        front.scale(100 * speed);
        Vector3d angVel = new Vector3d(0, handle, 0);
        chassis.applyCentralForce(front);
        chassis.setAngularVelocity(angVel);
    }
}
