package prototype;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Elevator extends PVEObject {
    Box plate;
    Box rightLeg;
    Box leftLeg;
    boolean up = false;

    public Elevator() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        plate = new Box(Type.KINEMATIC, 0.0, new Vector3d(10, 1, 10));
        rightLeg = new Box(Type.STATIC, 0.0, new Vector3d(1, 15, 10),"x-res:///res/ClearBox.wrl");
        leftLeg = new Box(Type.STATIC, 0.0, new Vector3d(1, 15, 10),"x-res:///res/ClearBox.wrl");
        plate.disableDeactivation(true);
        plate.setInitLocRev(0, 0.5, 0, 0, 0, 0);
        rightLeg.setInitLocRev(5.5, 7.5, 0, 0, 0, 0);
        leftLeg.setInitLocRev(-5.5, 7.5, 0, 0, 0, 0);
        return new PVEPart[] { plate, rightLeg, leftLeg };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return plate;
    }

    Vector3d initXYZ = new Vector3d();
    @Override
    public void setLocRot(double x,double y,double z,double rx,double ry,double rz) {
        super.setLocRot(x,y,z,rx,ry,rz);
        initXYZ.set(x,y,z);
        
    }
    public void setUp() {
        up = true;
    }

    double slide=0.5;
    double speed=0.02;
    @Override
    protected void postSimulation() {
        if (up==true) {
            if (slide<14.5) {
                slide = slide + speed;
                Vector3d v = new Vector3d(initXYZ);
                v.y = v.y + slide;
                plate.setLoc(v);
            }
            up = false;
        } else {
            if (slide>0.5) {
                slide = slide - speed;
                Vector3d v = new Vector3d(initXYZ);
                v.y = v.y + slide;
                plate.setLoc(v);
            }
            
        }
    }
}
