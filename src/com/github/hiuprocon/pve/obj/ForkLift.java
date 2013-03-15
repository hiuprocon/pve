package com.github.hiuprocon.pve.obj;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class ForkLift extends PVEObject {
	String a3url;
	PVEWorld world;
	SimpleCar car;
	Box lift;
	Hinge liftHinge;
	Box fork;
	Slider forkSlider;

	public ForkLift(PVEWorld world,String a3url) {
		this.world = world;
		this.a3url = a3url;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		car = new SimpleCar(world,a3url);
		car.setLoc(0,0,0);
		lift = new Box(Type.DYNAMIC,0.1,new Vector3d(1.0,0.1,1.0));
		lift.setLoc(0,0,1);
		fork = new Box(Type.DYNAMIC,0.1,new Vector3d(0.1,1.0,1.0));
		fork.setLoc(0,1,1);
		liftHinge = new Hinge(car,lift);
		forkSlider = new Slider(lift,fork);
		return new PVEPart[]{car,lift,fork};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[]{car.carConstraint,liftHinge,forkSlider};
	}

	@Override
	protected PVEPart getMainPart() {
		return car;
	}

	public void setForce(double force,double steering,double breaking,double drift) {
		car.setForce(force,steering,breaking,drift);
	}
	public void setLiftMotor(double f) {
		liftHinge.setMotor(f);
	}
	public void setForkMotor(double f) {
		forkSlider.setMotor(f);
	}
}