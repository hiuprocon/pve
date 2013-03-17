package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.A3Object;
import jp.sourceforge.acerola3d.a3.Util;

public abstract class PVEObject {
	PVEPart mainPart;
    PVEPart[] parts;
    Constraint[] constraints;
    public PVEObject() {
    }
    protected abstract PVEPart[] createParts();
    protected abstract Constraint[] createConstraints();
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
    /**
     * setLoc。
     * パーツの相対的な関係などの情報は失われる
     */
    public void setLoc(double x,double y,double z) {
    	Quat4d q = getQuat();
    	for (PVEPart p:parts) {
    		Vector3d v = new Vector3d(p.innerLoc);
    		v = Util.trans(q,v);
    		p.setLoc(x+v.x,y+v.y,z+v.z);
    		Quat4d qq = Util.euler2quat(p.innerRot);
    		qq.mul(q);
    		p.setQuat(qq);
    	}
    }
    public void setLoc(Vector3d loc) {
    	setLoc(loc.x,loc.y,loc.z);
    }
    public void setRot(double x,double y,double z) {
    	Vector3d l = getLoc();
    	Quat4d q = Util.euler2quat(x,y,z);
    	for (PVEPart p:parts) {
    		Vector3d v = new Vector3d(p.innerLoc);
    		v = Util.trans(q,v);
    		p.setLoc(l.x+v.x,l.y+v.y,l.z+v.z);
    		Quat4d qq = Util.euler2quat(p.innerRot);
    		qq.mul(q);
    		p.setQuat(qq);
    	}
    	for (PVEPart p:parts) {
    		p.setRot(x,y,z);
    	}
    }
    public void setRot(Vector3d rot) {
    	setRot(rot.x,rot.y,rot.z);
    }
    public void setRev(double x,double y,double z) {
    	setRot(x*Math.PI/180.0,y*Math.PI/180.0,z*Math.PI/180.0);
    }
    public void setRev(Vector3d rev) {
    	Vector3d v = new Vector3d(rev);
    	v.scale(Math.PI/180.0);
    	setRot(v);
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
