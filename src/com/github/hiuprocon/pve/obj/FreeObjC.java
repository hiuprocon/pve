package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class FreeObjC extends PVEObject {
	Type type;
	String a3url;
	double mass;
	FreeShapeC shapeC;
	public FreeObjC(Type type,String a3url) {
		this(type,1.0,a3url);
	}
	public FreeObjC(Type type,double mass,String a3url) {
		this.type = type;
		this.mass = mass;
		this.a3url = a3url;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		shapeC = new FreeShapeC(type,mass,a3url);
		//shapeC.setInitLocRot(0,0,0, 0,0,0);
		return new PVEPart[]{shapeC};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return shapeC;
	}
}