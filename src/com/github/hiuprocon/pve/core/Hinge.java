package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.linearmath.Transform;

public class Hinge extends Constraint {
    // オブジェクトの座標系でピボットと軸の方向を指定
    public Hinge(PVEPart a, PVEPart b, Vector3d pivot, Vector3d axis) {
        //どっちでも行けるみたいだけどそれぞれ思ってたのとちょっと違う
        test1(a,b,pivot,axis);
        //test2(a,b,pivot,axis);
    }

    void test1(PVEPart a, PVEPart b, Vector3d pivot, Vector3d axis) {
        Vector3d pivotDir = new Vector3d();
        Vector3d nAxis = new Vector3d(axis);
        Quat4d q;

        pivotDir.sub(pivot, a.innerLoc);
        q = Util.euler2quat(a.innerRot);
        q.conjugate();
        Vector3f pia = new Vector3f(Util.trans(q, pivotDir));
        Vector3f aia = new Vector3f(Util.trans(q, nAxis));

        pivotDir.sub(pivot, b.innerLoc);
        q = Util.euler2quat(b.innerRot);
        q.conjugate();
        Vector3f pib = new Vector3f(Util.trans(q, pivotDir));
        Vector3f aib = new Vector3f(Util.trans(q, nAxis));

        //なんか逆だと思うんだよね
        //con = new HingeConstraint(a.body, b.body, pia, pib, aia, aib);
        con = new HingeConstraint(b.body, a.body, pib, pia, aib, aia);
    }

    void test2(PVEPart a, PVEPart b, Vector3d pivot, Vector3d axis) {
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
        q2 = Util.a2bQuat(new Vector3d(0,0,-1),vTmp);//X軸だと思ったのに
System.out.println(vTmp);
//System.out.println(pivotDir);
//System.out.println(Util.quat2euler(q2));

        Transform localA = new Transform();
        localA.setIdentity();
        localA.origin.set(pivotDir);
        localA.basis.set(q2);

        q1 = Util.euler2quat(b.innerRot);
        q1.conjugate();
        pivotDir.sub(pivot, b.innerLoc);
        pivotDir = Util.trans(q1, pivotDir);

        vTmp = Util.trans(q1,nAxis);
        q2 = Util.a2bQuat(new Vector3d(0,0,-1),vTmp);//X軸だと思ったのに
System.out.println(vTmp);
//System.out.println(pivotDir);
//System.out.println(Util.quat2euler(q2));

        Transform localB = new Transform();
        localB.setIdentity();
        localB.origin.set(pivotDir);
        localB.basis.set(q2);

        con = new HingeConstraint(a.body, b.body, localA, localB);
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
