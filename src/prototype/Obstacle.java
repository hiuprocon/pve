package prototype;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Obstacle extends PVEObject {
    Cylinder cylinder;
    Vector3d loc;
    Vector3d vel;
    double maxX;
    double minX;
    double maxZ;
    double minZ;

    public Obstacle(int area,Vector3d loc, Vector3d vel) {
        this.loc = new Vector3d(loc);
        this.vel = new Vector3d(vel);
        if (area==1) {
            maxX=-25-2.5-10-1.5;
            minX=-125+1.5;
            maxZ= 50-1.5;
            minZ=-50+1.5;
        } else {
            maxX= 125-1.5;
            minX= 25+2.5+10+1.5;
            maxZ= 50-1.5;
            minZ=-50+1.5;
        }
        init();
        setLocRev(loc, new Vector3d());
    }

    @Override
    protected PVEPart[] createParts() {
        cylinder = new Cylinder(Type.KINEMATIC, 0.0, 2, 3);
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
        if (loc.x > maxX) {
            loc.x = maxX;
            vel.x *= -1;
        }
        if (loc.x < minX) {
            loc.x = minX;
            vel.x *= -1;
        }
        if (loc.y > 100) {
            loc.y = 100;
            vel.y *= -1;
        }
        if (loc.y < -100) {
            loc.y = -100;
            vel.y *= -1;
        }
        if (loc.z > maxZ) {
            loc.z = maxZ;
            vel.z *= -1;
        }
        if (loc.z < minZ) {
            loc.z = minZ;
            vel.z *= -1;
        }
        loc.add(vel);
        cylinder.setLoc(loc);
    }
}
