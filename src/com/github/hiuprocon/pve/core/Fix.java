package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;

public class Fix extends Constraint {
    // オブジェクトの座標系でピボットと軸の方向を指定
    public Fix(PVEPart a, PVEPart b) {
        Vector3d pivot = new Vector3d();
        pivot.add(a.innerLoc,b.innerLoc);
        pivot.scale(0.5);
        Vector3d axis = new Vector3d(0,0,1);

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
        ((HingeConstraint) con).setLimit(0f,0.000001f);
    }
}
