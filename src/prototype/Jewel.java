package prototype;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Jewel extends PVEObject {
	Box box;
	public Jewel() {
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		box = new Box(Type.DYNAMIC,10.0,new Vector3d(0.5,0.5,0.5),"x-res:///res/prototype/Jewel.wrl");
		//box.setInitLocRot(0,0,0, 0,0,0);
        box.setFriction(1);
		return new PVEPart[]{box};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return box;
	}
}