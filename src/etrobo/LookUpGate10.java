package etrobo;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class LookUpGate10 extends PVEObject {
    Compound comp;
    public LookUpGate10() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        //塩ビパイプの直径2cmという前提で
        Box box = new Box(Type.DYNAMIC,0.1,new Vector3d(2,0.9,0.01));
        box.setInitLocRev(0, 2.95,0, 0,0,0);
        Cylinder c1 = new Cylinder(Type.DYNAMIC, 0.1, 4.4, 0.2);
        c1.setInitLocRev(0,2.5,0, 0,0,90);
        Cylinder c2 = new Cylinder(Type.DYNAMIC, 0.1, 2.2, 0.2);
        c2.setInitLocRev( 2.3,1.3,0, 0,0,0);
        Cylinder c3 = new Cylinder(Type.DYNAMIC, 0.1, 2.2, 0.2);
        c3.setInitLocRev(-2.3,1.3,0, 0,0,0);
        Cylinder c4 = new Cylinder(Type.DYNAMIC, 0.1, 0.8, 0.2);
        c4.setInitLocRev( 2.3,0.1,0, 90,0,0);
        Cylinder c5 = new Cylinder(Type.DYNAMIC, 0.1, 0.8, 0.2);
        c5.setInitLocRev(-2.3,0.1,0, 90,0,0);
        comp = new Compound(Type.DYNAMIC,box,c1,c2,c3,c4,c5);
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
