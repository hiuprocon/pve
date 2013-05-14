package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;

public class Hinge extends Constraint {
    // オブジェクトの座標系でピボットと軸の方向を指定
    public Hinge(PVEPart a, PVEPart b, Vector3d pivot, Vector3d axis) {
        Vector3d pivotDir = new Vector3d();
        Quat4d q;

        pivotDir.sub(pivot, a.innerLoc);
        q = Util.euler2quat(a.innerRot);
        pivotDir = Util.trans(q, pivotDir);
        Vector3f pia = new Vector3f(Util.trans(q, pivotDir));
        Vector3f aia = new Vector3f(Util.trans(q, axis));

        pivotDir.sub(pivot, b.innerLoc);
        q = Util.euler2quat(b.innerRot);
        pivotDir = Util.trans(q, pivotDir);
        Vector3f pib = new Vector3f(Util.trans(q, pivotDir));
        Vector3f aib = new Vector3f(Util.trans(q, axis));

        con = new HingeConstraint(a.body, b.body, pia, pib, aia, aib);
    }

    // パーツの座標系でピボットと軸の方向を指定
    public Hinge(PVEPart a, PVEPart b, Vector3d pivotInA, Vector3d pivotInB,
            Vector3d axisInA, Vector3d axisInB) {
        Vector3f pia = new Vector3f(pivotInA);
        Vector3f pib = new Vector3f(pivotInB);
        Vector3f aia = new Vector3f(axisInA);
        Vector3f aib = new Vector3f(axisInB);
        con = new HingeConstraint(a.body, b.body, pia, pib, aia, aib);
    }

    public void setLimit(double low, double high) {
        ((HingeConstraint) con).setLimit((float) low, (float) high);
    }

    public void enableAngularMotor(boolean enableMotor, double targetVelocity,
            double maxMotorImpulse) {
        ((HingeConstraint) con).enableAngularMotor(enableMotor,
                (float) targetVelocity, (float) maxMotorImpulse);
    }

    public double getHingeAngle() {
        return ((HingeConstraint) con).getHingeAngle();
    }

    public double getLowerLimit() {
        return ((HingeConstraint) con).getLowerLimit();
    }

    public double getUpperLimit() {
        return ((HingeConstraint) con).getUpperLimit();
    }
}
