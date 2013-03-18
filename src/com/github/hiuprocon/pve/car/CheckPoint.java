package com.github.hiuprocon.pve.car;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.Vector3d;

public class CheckPoint extends PVEObject {
	Box box;
	public CheckPoint() {
		init();
		this.getMainA3().setScaleX(8);
		this.getMainA3().setScaleY(8);
		this.getMainA3().setScaleZ(4);
	}

	@Override
    protected PVEPart[] createParts() {
		box = new Box(Type.GHOST,0.0,new Vector3d(8,8,4),"x-res:///res/ClearBox.wrl");
		//box.setInitLocRot(0,0,0, 0,0,0);
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
