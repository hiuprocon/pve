package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.dynamics.constraintsolver.SliderConstraint;
import com.bulletphysics.linearmath.Transform;

public class Fix extends Constraint {
    public Fix(PVEPart a, PVEPart b) {
        Vector3d pivot = new Vector3d();
        pivot.add(a.innerLoc,b.innerLoc);
        pivot.scale(0.5);
        realConstructor(a,b,pivot);
    }

    //どうしてもこっちを使わないとゆらゆら揺れる時があるね
    public Fix(PVEPart a, PVEPart b,Vector3d pivot) {
        realConstructor(a,b,pivot);
    }
    void realConstructor(PVEPart a, PVEPart b,Vector3d pivot) {
        Vector3d axis = new Vector3d();
        axis.sub(pivot,a.innerLoc);
        axis.normalize();

        Vector3d pivotDir = new Vector3d();
        Quat4d q;
        Quat4d q2;

        pivotDir.sub(pivot, a.innerLoc);
        q = Util.euler2quat(a.innerRot);
        q.conjugate();
        Vector3d pia = new Vector3d(Util.trans(q, pivotDir));
        Vector3d aia = new Vector3d(Util.trans(q, axis));
        q2 = Util.a2bQuat(new Vector3d(1,0,0),aia);

        Transform localA = new Transform();
        localA.setIdentity();
        localA.origin.set(pia);
        localA.basis.set(q2);

        pivotDir.sub(pivot, b.innerLoc);
        q = Util.euler2quat(b.innerRot);
        q.conjugate();
        Vector3d pib = new Vector3d(Util.trans(q, pivotDir));
        Vector3d aib = new Vector3d(Util.trans(q, axis));
        q2 = Util.a2bQuat(new Vector3d(1,0,0),aib);

        Transform localB = new Transform();
        localB.setIdentity();
        localB.origin.set(pib);
        localB.basis.set(q2);

        con = new SliderConstraint(a.body, b.body, localA, localB, true);
        ((SliderConstraint) con).setLowerLinLimit(0f);
        ((SliderConstraint) con).setUpperLinLimit(0.00001f);
    }
}
