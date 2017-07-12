package etrobo;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Garage10 extends PVEObject {
    Compound comp;
    public Garage10() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        Box box1 = new Box(Type.DYNAMIC,1,new Vector3d(2.7,0.5,0.1));
        box1.setInitLocRev(0,0.25,1.05, 0,0,0);
        Box box2 = new Box(Type.DYNAMIC,1,new Vector3d(0.1,0.5,2.0));
        box2.setInitLocRev(-1.3,0.25,0, 0,0,0);
        Box box3 = new Box(Type.DYNAMIC,1,new Vector3d(0.1,0.5,2.0));
        box3.setInitLocRev(1.3,0.25,0, 0,0,0);
        comp = new Compound(Type.DYNAMIC,box1,box2,box3);
        comp.setInitLocRev(0,0,0, 0,0,0);
        return new PVEPart[] { comp };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return comp;
    }

}
