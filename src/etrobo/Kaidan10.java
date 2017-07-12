package etrobo;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Kaidan10 extends PVEObject {
    Compound comp;
    public Kaidan10() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        Box box01 = new Box(Type.STATIC,1,new Vector3d(3,0.09,0.2),
                       "x-res:///res/BlackBox.wrl");
        box01.setInitLocRev(-1.20,0.045,0, 0,0,0);
        Box box02 = new Box(Type.STATIC,1,new Vector3d(3,0.09,0.3),
                       "x-res:///res/WhiteBox.wrl");
        box02.setInitLocRev(-1.20,0.045,-0.25, 0,0,0);
        Box box03 = new Box(Type.STATIC,1,new Vector3d(3,0.09,0.3),
                       "x-res:///res/WhiteBox.wrl");
        box03.setInitLocRev(-1.20,0.045, 0.25, 0,0,0);
        Box box04 = new Box(Type.STATIC,1,new Vector3d(3.3,0.09,1.1),
                       "x-res:///res/WhiteBox.wrl");
        box04.setInitLocRev(-1.35,0.045,-0.95, 0,0,0);
        Box box05 = new Box(Type.STATIC,1,new Vector3d(3.3,0.09,1.1),
                       "x-res:///res/WhiteBox.wrl");
        box05.setInitLocRev(-1.35,0.045, 0.95, 0,0,0);

        Box box06 = new Box(Type.STATIC,1,new Vector3d(2.7,0.09,3.0),
                       "x-res:///res/WhiteBox.wrl");
        box06.setInitLocRev(1.65,0.045, 0.0, 0,0,0);

        Box box07 = new Box(Type.STATIC,1,new Vector3d(1.3,0.09,0.2),
                       "x-res:///res/BlackBox.wrl");
        box07.setInitLocRev(0.95,0.135, 0.0, 0,0,0);
        Box box08 = new Box(Type.STATIC,1,new Vector3d(0.2,0.09,1.4),
                       "x-res:///res/BlackBox.wrl");
        box08.setInitLocRev(1.5,0.135,-0.8, 0,0,0);
        Box box09 = new Box(Type.STATIC,1,new Vector3d(1.1,0.09,0.3),
                       "x-res:///res/WhiteBox.wrl");
        box09.setInitLocRev(0.85,0.135,-0.25, 0,0,0);
        Box box10 = new Box(Type.STATIC,1,new Vector3d(1.3,0.09,0.3),
                       "x-res:///res/WhiteBox.wrl");
        box10.setInitLocRev(0.95,0.135, 0.25, 0,0,0);
        Box box11 = new Box(Type.STATIC,1,new Vector3d(1.4,0.09,1.1),
                       "x-res:///res/WhiteBox.wrl");
        box11.setInitLocRev(0.7,0.135,-0.95, 0,0,0);
        Box box12 = new Box(Type.STATIC,1,new Vector3d(1.6,0.09,1.1),
                       "x-res:///res/WhiteBox.wrl");
        box12.setInitLocRev(0.8,0.135, 0.95, 0,0,0);
        Box box13 = new Box(Type.STATIC,1,new Vector3d(1.4,0.09,3.0),
                       "x-res:///res/WhiteBox.wrl");
        box13.setInitLocRev(2.3,0.135, 0.0, 0,0,0);


        comp = new Compound(Type.STATIC,box01,box02,box03,box04,
                            box05,box06,box07,box08,box09,box10,
                            box11,box12,box13);
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
