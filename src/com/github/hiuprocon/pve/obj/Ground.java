package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;

public class Ground extends PVEObject {
	String a3url;
	FreeShapeA g;
	public Ground() {
		this("x-res:///res/background0.a3");
	}
	public Ground(String a3url) {
		this.a3url = a3url;
	}

	@Override
	protected PVEPart[] createParts() {
		g = new FreeShapeA(Type.STATIC,a3url);
		return new PVEPart[]{g};
	}

	@Override
	protected TypedConstraint[] createConstraints() {
		return new TypedConstraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return g;
	}
}