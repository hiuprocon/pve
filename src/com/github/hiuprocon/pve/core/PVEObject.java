package com.github.hiuprocon.pve.core;

import javax.vecmath.*;

import jp.sourceforge.acerola3d.a3.A3Object;
import jp.sourceforge.acerola3d.a3.Util;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

public abstract class PVEObject {
	PVEPart mainPart;
    PVEPart[] parts;
    TypedConstraint[] constraints;
    public PVEObject() {
    }
    protected abstract PVEPart[] createParts();
    protected abstract TypedConstraint[] createConstraints();
    protected abstract PVEPart getMainPart();
    protected void postSimulation(){;}
    /**
     * このメソッドはコンストラクタの中で必ず呼び出さなければならない。
     */
    protected final void init() {
    	parts = createParts();
    	for (PVEPart p:parts)
    		p.obj = this;
    	constraints = createConstraints();
    	mainPart = getMainPart();
    }
    public void setLoc(double x,double y,double z) {
    	for (PVEPart p:parts) {
    		p.setLoc(x,y,z);
    	}
    }
    public void setLoc(Vector3d loc) {
    	setLoc(loc.x,loc.y,loc.z);
    }
    public void setRot(double x,double y,double z) {
    	for (PVEPart p:parts) {
    		p.setRot(x,y,z);
    	}
    }
    public void setRot(Vector3d rot) {
    	setRot(rot.x,rot.y,rot.z);
    }
    public void setVel(double x,double y,double z) {
    	for (PVEPart p:parts) {
    		p.setVel(x,y,z);
    	}
    }
    public void setVel(Vector3d vel) {
    	setVel(vel.x,vel.y,vel.z);
    }
    public Vector3d getLoc() {
    	Vector3d v = mainPart.getLoc();
    	v.sub(mainPart.innerLoc);
    	return v;
    }
    //自信なし。テストすべし。
    public Quat4d getQuat() {
    	Quat4d q = mainPart.getQuat();
    	Quat4d qq = Util.euler2quat(mainPart.innerRot);
    	q.mul(qq);
    	return q;
    }
    //自信なし。テストすべし。
    public Vector3d getRot() {
    	return Util.quat2euler(getQuat());
    }
    public A3Object getMainA3() {
    	return mainPart.a3;
    }
    public void setGravity(Vector3d g) {
    	for (PVEPart p:parts) {
    		p.setGravity(g);
    	}
    }
}
