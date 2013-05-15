package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;

public class Point2Point extends Constraint {
    public Point2Point(PVEPart a, PVEPart b, Vector3d pivot) {
        Quat4d q;
        Vector3d pivotInA = new Vector3d();
        Vector3d pivotInB = new Vector3d();

        pivotInA.sub(pivot, a.innerLoc);
        q = Util.euler2quat(a.innerRot);
        pivotInA = Util.trans(q, pivotInA);

        pivotInB.sub(pivot, b.innerLoc);
        q = Util.euler2quat(b.innerRot);
        pivotInB = Util.trans(q, pivotInB);

        con = new Point2PointConstraint(a.body, b.body, new Vector3f(pivotInA), new Vector3f(pivotInB));
    }

    public Point2Point(PVEPart a, PVEPart b, Vector3f pivotInA, Vector3f pivotInB) {
        con = new Point2PointConstraint(a.body, b.body, pivotInA, pivotInB);
    }
}
