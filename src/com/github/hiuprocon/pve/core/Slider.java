package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.dynamics.constraintsolver.SliderConstraint;
import com.bulletphysics.linearmath.Transform;

public class Slider extends Constraint {
    public Slider(PVEPart a, PVEPart b, Vector3d pivot, Vector3d axis) {
        Vector3d pivotDir = new Vector3d();
        Vector3d nAxis = new Vector3d(axis);
        nAxis.normalize();
        Vector3d vTmp = new Vector3d();
        Quat4d q1 = new Quat4d();
        Quat4d q2 = new Quat4d();

        q1 = Util.euler2quat(a.innerRot);
        q1.conjugate();
        pivotDir.sub(pivot, a.innerLoc);
        pivotDir = Util.trans(q1, pivotDir);

        vTmp = Util.trans(q1,nAxis);
        q2 = Util.a2bQuat(new Vector3d(1,0,0),vTmp);
System.out.println(pivotDir);
System.out.println(Util.quat2euler(q2));

        Transform localA = new Transform();
        localA.setIdentity();
        localA.origin.set(pivotDir);
        localA.basis.set(q2);

        q1 = Util.euler2quat(b.innerRot);
        q1.conjugate();
        pivotDir.sub(pivot, b.innerLoc);
        pivotDir = Util.trans(q1, pivotDir);

        vTmp = Util.trans(q1,nAxis);
        q2 = Util.a2bQuat(new Vector3d(1,0,0),vTmp);
System.out.println(pivotDir);
System.out.println(Util.quat2euler(q2));

        Transform localB = new Transform();
        localB.setIdentity();
        localB.origin.set(pivotDir);
        localB.basis.set(q2);

        con = new SliderConstraint(a.body, b.body, localA, localB, true);
    }

    public Slider(PVEPart a, PVEPart b, Transform localA, Transform localB) {
        con = new SliderConstraint(a.body, b.body, localA, localB, true);
    }

    public void setLowerLinLimit(double lowerLimit) {
        ((SliderConstraint) con).setLowerLinLimit((float) lowerLimit);
    }

    public void setUpperLinLimit(double upperLimit) {
        ((SliderConstraint) con).setUpperLinLimit((float) upperLimit);
    }

    public void setPoweredLinMotor(boolean onOff) {
        ((SliderConstraint) con).setPoweredLinMotor(onOff);
    }

    public void setMaxLinMotorForce(double maxLinMotorForce) {
        ((SliderConstraint) con).setMaxLinMotorForce((float) maxLinMotorForce);
    }

    public void setTargetLinMotorVelocity(double targetLinMotorVelocity) {
        ((SliderConstraint) con)
                .setTargetLinMotorVelocity((float) targetLinMotorVelocity);
    }

    public double getLinearPos() {
        return ((SliderConstraint) con).getLinearPos();
    }

    public double getLowerLinLimit() {
        return ((SliderConstraint) con).getLowerLinLimit();
    }

    public double getUpperLinLimit() {
        return ((SliderConstraint) con).getUpperLinLimit();
    }
}
