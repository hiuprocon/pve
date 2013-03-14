package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;

public class SimpleCarObj extends PVEObject {
	String a3url;
	SimpleCar vehicle;
	public SimpleCarObj(String a3url) {
		this.a3url = a3url;
	}

	@Override
	protected PVEPart[] createParts() {
		vehicle = new SimpleCar(a3url);
		return new PVEPart[]{vehicle};
	}

	@Override
	protected TypedConstraint[] createConstraints() {
		return new TypedConstraint[]{vehicle.vehicleConstraint};
	}

	@Override
	protected PVEPart getMainPart() {
		return vehicle;
	}

	public void setForce(double force,double steering,double breaking,double drift) {
		vehicle.setForce((float)force,(float)steering,(float)breaking,(float)drift);
	}
}