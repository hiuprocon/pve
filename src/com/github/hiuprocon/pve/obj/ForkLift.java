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
		setLiftMotor(1);setLiftMotor(0);
		setForkMotor(1);setForkMotor(0);
	}

	@Override
	protected PVEPart[] createParts() {
		car = new SimpleCar(world,a3url);
		lift = new Box(Type.DYNAMIC,0.1,new Vector3d(1.0,2.0,0.1));
		fork = new Box(Type.DYNAMIC,0.1,new Vector3d(1.0,0.1,1.0));

		car.setInitLocRot( 0.0, 0.0, 0.0,  0,0,0);
		lift.setInitLocRot(0.0, 1.1, 1.0,  0,0,0);
		fork.setInitLocRot(0.0, 0.0, 1.6,  0,0,0);

		liftHinge = new Hinge(car,lift,new Vector3d(0,0.1,1.0),new Vector3d(1,0,0));
		liftHinge.disableCollisionsBetweenLinkedBodies=true;
		//forkSlider = new Slider(lift,fork,new Vector3d(0,0,1.6),new Vector3d(0,1,0));
		forkSlider = new Slider(lift,fork,new Vector3d(0,0,1.6),new Vector3d(0,0,1));//????
		forkSlider.disableCollisionsBetweenLinkedBodies=true;
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