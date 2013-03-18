package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class FreeObjB extends PVEObject {
	Type type;
	String a3url;
	double mass;
	FreeShapeB shapeB;
	public FreeObjB(Type type,String a3url) {
		this(type,1.0,a3url);
	}
	public FreeObjB(Type type,double mass,String a3url) {
		this.type = type;
		this.mass = mass;
		this.a3url = a3url;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		shapeB = new FreeShapeB(type,mass,a3url);
		//shapeB.setInitLocRot(0,0,0, 0,0,0);
		return new PVEPart[]{shapeB};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return shapeB;
	}
}