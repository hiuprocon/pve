package prototype;

import com.github.hiuprocon.pve.core.*;
//import javax.vecmath.*;

public class Jewel2 extends Jewel {
    @Override
    protected PVEPart[] createParts() {
        //jewel = new FreeShapeB(Type.DYNAMIC,"x-res:///res/prototype/Jewel.wrl");
        jewel = new FreeShapeB(Type.DYNAMIC,"x-res:///res/prototype/gougui.a3");
        // jewel.setInitLocRot(0,0,0, 0,0,0);
        //jewel.setFriction(3.0);
        jewel.setDamping(0.9,0.0);
        return new PVEPart[] { jewel };
    }
}
