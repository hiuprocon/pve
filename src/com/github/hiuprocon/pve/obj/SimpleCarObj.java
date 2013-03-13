package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.parts.*;
import javax.vecmath.*;

public class SimpleCarObj extends PVEObject {
	String a3url;
	SimpleCar vehicle;
	public SimpleCarObj(String a3url) {
		this.a3url = a3url;
	}

	@Override
	protected PVEPart[] createParts() {
		Vector3d loc = new Vector3d();
		Vector3d rot = new Vector3d();
		vehicle = new SimpleCar(loc,rot,a3url);
		return new PVEPart[]{vehicle};
	}

	@Override
	protected TypedConstraint[] createConstraints() {
		return new TypedConstraint[]{vehicle.vehicleConstraint};
	}
	
}