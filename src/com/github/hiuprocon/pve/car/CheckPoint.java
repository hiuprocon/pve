package com.github.hiuprocon.pve.car;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.Vector3d;

public class CheckPoint extends PVEObject {
	Box box;
	public CheckPoint() {
		init();
		this.getMainA3().setScale(8);
	}

	@Override
    protected PVEPart[] createParts() {
		box = new Box(Type.GHOST,0.0,new Vector3d(4,4,2),"x-rzip:x-res:///res/ClearBlocks2.a3!/blockBlack.wrl");
    	return new PVEPart[]{box};
    }

	@Override
    protected TypedConstraint[] createConstraints() {
    	return new TypedConstraint[0];
    }

	@Override
    protected PVEPart getMainPart() {
    	return box;
    }
}
