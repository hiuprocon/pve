package prototype;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Obstacle3 extends PVEObject {
    Vector3d loc;
    Cylinder cylinder;

    public Obstacle3(Vector3d loc) {
        this.loc = loc;
        init();
        setLocRev(loc, new Vector3d());
    }

    @Override
    protected PVEPart[] createParts() {
        cylinder = new Cylinder(Type.STATIC, 0.0, 2, 3,"x-res:///res/prototype/WCtree.a3");
        return new PVEPart[] { cylinder };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return cylinder;
    }
}
