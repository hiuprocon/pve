package prototype;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Jewel extends PVEObject {
    PVEPart jewel;

    public Jewel() {
        init();
    }

    //@Override
    protected PVEPart[] createParts_BAK() {
        jewel = new Box(Type.DYNAMIC, 10.0, new Vector3d(0.5, 0.5, 0.5),
                  "x-res:///res/prototype/Jewel.wrl");
        // jewel.setInitLocRot(0,0,0, 0,0,0);
        jewel.setFriction(0.5);
        jewel.setDamping(0.0,0.0);
        return new PVEPart[] { jewel };
    }

    //@Override
    protected PVEPart[] createParts() {
        jewel = new Sphere(Type.DYNAMIC, 10.0, 0.5,
                  "x-res:///res/Sphere.wrl");
        // jewel.setInitLocRot(0,0,0, 0,0,0);
        //jewel.setFriction(1);
        jewel.setDamping(0.9,0.0);
        return new PVEPart[] { jewel };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return jewel;
    }
}