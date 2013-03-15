package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;

public class ForkLift extends PVEObject {
	String a3url;
	PVEWorld world;
	SimpleCar vehicle;
	public ForkLift(PVEWorld world,String a3url) {
		this.world = world;
		this.a3url = a3url;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		vehicle = new SimpleCar(world,a3url);
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
		vehicle.setForce(force,steering,breaking,drift);
	}
}