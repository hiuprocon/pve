package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;

public class ConeObj extends PVEObject {
	Type type;
	String a3url;
	double mass;
	double height;
	double radius;
	Cone cone;
	public ConeObj() {
		this(Type.DYNAMIC,1.0,1,1,"x-res:///res/Cone.wrl");
	}
	public ConeObj(Type type,double mass,double height,double radius,String a3url) {
		this.type = type;
		this.mass = mass;
		this.a3url = a3url;
		this.height = height;
		this.radius = radius;
		init();
	}

	@Override
	protected PVEPart[] createParts() {
		cone = new Cone(type,mass,height,radius,a3url);
		//cone.setInitLocRot(0,0,0, 0,0,0);
		return new PVEPart[]{cone};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return cone;
	}
}