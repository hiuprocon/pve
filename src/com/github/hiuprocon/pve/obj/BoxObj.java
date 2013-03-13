package com.github.hiuprocon.pve.obj;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.parts.*;
import javax.vecmath.*;

public class BoxObj extends PVEObject {
	Type type;
	String a3url;
	double mass;
	Vector3d size;
	Box box;
	public BoxObj(Type type,double mass,Vector3d size,String a3url) {
		this.type = type;
		this.a3url = a3url;
		this.size = new Vector3d(size);
	}

	@Override
	protected PVEPart[] createParts() {
		Vector3d loc = new Vector3d();
		Vector3d rot = new Vector3d();
		box = new Box(Type.DYNAMIC,loc,rot,mass,size,a3url);
		return new PVEPart[]{box};
	}

	@Override
	protected TypedConstraint[] createConstraints() {
		return new TypedConstraint[0];
	}
	
}