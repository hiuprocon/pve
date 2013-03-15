package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class SimpleCarObj extends PVEObject {
	String a3url;
	PVEWorld world;
	SimpleCar car;
	public SimpleCarObj(PVEWorld world,String a3url) {
		this.world = world;
		this.a3url = a3url;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		car = new SimpleCar(world,a3url);
		return new PVEPart[]{car};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[]{car.carConstraint};
	}

	@Override
	protected PVEPart getMainPart() {
		return car;
	}

	public void setForce(double force,double steering,double breaking,double drift) {
		car.setForce((float)force,(float)steering,(float)breaking,(float)drift);
	}
}