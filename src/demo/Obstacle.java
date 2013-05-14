package demo;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Obstacle extends PVEObject {
    Cylinder cylinder;
    Vector3d loc;
    Vector3d vel;

    public Obstacle(Vector3d loc, Vector3d vel) {
        init();
        this.loc = new Vector3d(loc);
        this.vel = new Vector3d(vel);
        setLocRev(loc, new Vector3d());
    }

    @Override
    protected PVEPart[] createParts() {
        cylinder = new Cylinder(Type.KINEMATIC, 0.0);
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

    @Override
    protected void postSimulation() {
        loc.add(vel);
        cylinder.setLoc(loc);
    }
}
