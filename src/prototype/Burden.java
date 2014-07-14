package prototype;

import com.github.hiuprocon.pve.core.*;
//import javax.vecmath.*;

public class Burden extends PVEObject {
    PVEPart burden;

    public Burden() {
        init();
    }

    /*
    @Override
    protected PVEPart[] createParts() {
        burden = new Box(Type.DYNAMIC, 10.0, new Vector3d(0.5, 0.5, 0.5),
                  "x-res:///res/prototype/Jewel.wrl");
        // burden.setInitLocRot(0,0,0, 0,0,0);
        burden.setFriction(0.5);
        burden.setDamping(0.0,0.0);
        return new PVEPart[] { burden };
    }
    */

    /*
    @Override
    protected PVEPart[] createParts() {
        burden = new Sphere(Type.DYNAMIC, 10.0, 0.5,
                  "x-res:///res/Sphere.wrl");
        // burden.setInitLocRot(0,0,0, 0,0,0);
        //burden.setFriction(1);
        burden.setDamping(0.9,0.0);
        return new PVEPart[] { burden };
    }
    */

    @Override
    protected PVEPart[] createParts() {
        burden = new FreeShapeB(Type.DYNAMIC,"x-res:///res/prototype/Jewel.wrl");
        // burden.setInitLocRot(0,0,0, 0,0,0);
        //burden.setFriction(3.0);
        burden.setDamping(0.9,0.0);
        return new PVEPart[] { burden };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return burden;
    }
}
