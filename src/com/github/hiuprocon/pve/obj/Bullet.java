package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;

public class Bullet extends PVEObject implements ActiveObject {
	String a3url;
	Sphere sphere;
	public Bullet() {
		this("x-res:///res/bullet.a3");
	}
	public Bullet(String a3url) {
		this.a3url = a3url;
	}

	@Override
	protected PVEPart[] createParts() {
		sphere = new Sphere(Type.KINEMATIC,1.0,0.2,a3url);
		return new PVEPart[]{sphere};
	}

	@Override
	protected TypedConstraint[] createConstraints() {
		return new TypedConstraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return sphere;
	}

	@Override
	public void exec() {
		// TODO Auto-generated method stub
	}
}