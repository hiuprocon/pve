package com.github.hiuprocon.pve.car;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.Vector3d;

public class CheckPoint extends PVEObject {
	Box box;

	@Override
    protected PVEPart[] createParts() {
		box = new Box(Type.GHOST,0.0,new Vector3d(1,1,1),"x-res:///res/Box.wrl");
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
