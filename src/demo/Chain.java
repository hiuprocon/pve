package demo;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Chain extends PVEObject {
    Box box0;
    Box box1;
    Box box2;
    Point2Point c1;
    Point2Point c2;

    public Chain() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        box0 = new Box(Type.STATIC, 0.0,new Vector3d(0.1,0.1,0.1));
        box1 = new Box(Type.DYNAMIC, 1.0,new Vector3d(0.1,0.1,0.1));
        box2 = new Box(Type.DYNAMIC, 1.0,new Vector3d(0.1,0.1,0.1));
        box0.setInitLocRev(0, 0, 0, 0, 0, 0);
        box1.setInitLocRev(0.3, 0, 0, 0, 0, 0);
        box2.setInitLocRev(0.6, 0, 0, 0, 0, 0);
        return new PVEPart[] { box0, box1, box2 };
    }

    @Override
    protected Constraint[] createConstraints() {
        c1 = new Point2Point(box0, box1, new Vector3d(0.5, 0, 0));
        c2 = new Point2Point(box1, box2, new Vector3d(0.35, 0, 0));
        return new Constraint[] { c1, c2 };
    }

    @Override
    protected PVEPart getMainPart() {
        return box0;
    }
}
