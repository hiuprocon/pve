package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import com.bulletphysics.collision.shapes.*;

public class SimpleCarObj extends PVEObject {
	String a3url;
	PVEWorld world;
	SimpleCar car;
    CollisionShape chassisShape;
	public SimpleCarObj(PVEWorld world) {
        this(world,"x-res:///res/stk_tux.a3");
    }
	public SimpleCarObj(PVEWorld world,String a3url) {
        this(world,a3url,null);
    }
	public SimpleCarObj(PVEWorld world,String a3url,CollisionShape chassisShape) {
		this.world = world;
		this.a3url = a3url;
        this.chassisShape = chassisShape;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		car = new SimpleCar(world,a3url,chassisShape);
		//car.setInitLocRot(0,0,0, 0,0,0);
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