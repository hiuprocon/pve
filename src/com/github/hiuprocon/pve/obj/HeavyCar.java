package com.github.hiuprocon.pve.obj;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class HeavyCar extends PVEObject {
    Box chassis;
    Box joint;
    Cylinder tire1;
    Cylinder tire2;
    Cylinder tire3;
    Hinge handle;
    Hinge axle1;
    Hinge axle2;
    Hinge axle3;

    public HeavyCar() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        chassis = new Box(Type.DYNAMIC, 80.0, new Vector3d(1, 0.333, 1));
        joint = new Box(Type.DYNAMIC, 1.0, new Vector3d(0.333, 0.333, 0.333));
        tire1 = new Cylinder(Type.DYNAMIC, 1, 0.333, 0.666);
        tire2 = new Cylinder(Type.DYNAMIC, 1, 0.333, 0.666);
        tire3 = new Cylinder(Type.DYNAMIC, 1, 0.333, 0.666);
        chassis.setInitLocRev(0, 0.5, 0, 0, 0, 0);
        joint.setInitLocRev(0, 0.5, 1, 0, 0, 0);
        tire1.setInitLocRev(0, 0.333, 1, 0, 0, 90);
        tire2.setInitLocRev(0.666, 0.333, -0.5, 0, 0, 90);
        tire3.setInitLocRev(-0.666, 0.333, -0.5, 0, 0, 90);
        chassis.disableDeactivation(true);
        joint.disableDeactivation(true);
        tire1.disableDeactivation(true);
        tire2.disableDeactivation(true);
        tire3.disableDeactivation(true);
        tire1.setFriction(2);
        tire2.setFriction(2);
        tire3.setFriction(2);
        return new PVEPart[] { chassis, joint, tire1, tire2, tire3 };
    }

    @Override
    protected Constraint[] createConstraints() {
        handle = new Hinge(chassis, joint, new Vector3d(0, 0.5, 1),
                new Vector3d(0, -1, 0));
        axle1 = new Hinge(joint, tire1, new Vector3d(0, 0.333, 1),
                new Vector3d(-1, 0, 0));
        axle2 = new Hinge(chassis, tire2, new Vector3d(0.666, 0.333, -0.5),
                new Vector3d(-1, 0, 0));
        axle3 = new Hinge(chassis, tire3, new Vector3d(-0.666, 0.333, -0.5),
                new Vector3d(-1, 0, 0));
        handle.disableCollisionsBetweenLinkedBodies = true;
        axle1.disableCollisionsBetweenLinkedBodies = true;
        axle2.disableCollisionsBetweenLinkedBodies = true;
        axle3.disableCollisionsBetweenLinkedBodies = true;
        return new Constraint[] { handle, axle1, axle2, axle3 };
    }

    @Override
    protected PVEPart getMainPart() {
        return chassis;
    }

    public void drive(double speed, double handle) {
        this.handle.setLimit(-Math.PI / 16.0, Math.PI / 16.0);
        this.handle.enableAngularMotor(true, handle, 10);
        axle2.enableAngularMotor(true, speed, 10);
        axle3.enableAngularMotor(true, speed, 10);
    }
}
