package prototype;

import com.github.hiuprocon.pve.core.*;
//import javax.vecmath.*;

public class Burden2 extends Burden {
    @Override
    protected PVEPart[] createParts() {
        //burden = new FreeShapeB(Type.DYNAMIC,"x-res:///res/prototype/Jewel.wrl");
        burden = new FreeShapeB(Type.DYNAMIC,"x-res:///res/prototype/gougui.a3");
        // burden.setInitLocRot(0,0,0, 0,0,0);
        //burden.setFriction(3.0);
        burden.setDamping(0.9,0.0);
        return new PVEPart[] { burden };
    }
}
