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
     * setLocRot。
     * パーツの相対的な関係などの情報は失われる
     */
    public void setLocRot(double x,double y,double z,double rx,double ry,double rz) {
    	Quat4d q = Util.euler2quat(rx,ry,rz);
    	for (PVEPart p:parts) {
    		Vector3d v = new Vector3d(p.innerLoc);
    		v = Util.trans(q,v);
    		p.setLoc(x+v.x,y+v.y,z+v.z);
    		Quat4d qq = Util.euler2quat(p.innerRot);
    		qq.mul(q);
    		p.setQuat(qq);
    	}
    }
    public void setLocRot(Vector3d loc,Vector3d rev) {
    	setLocRot(loc.x,loc.y,loc.z, rev.x,rev.y,rev.z);
    }
    /**
     * setLocRev。
     * パーツの相対的な関係などの情報は失われる
     */
    public void setLocRev(double x,double y,double z,double rx,double ry,double rz) {
    	Quat4d q = Util.euler2quat(rx/180.0*Math.PI,ry/180.0*Math.PI,rz/180.0*Math.PI);
    	for (PVEPart p:parts) {
    		Vector3d v = new Vector3d(p.innerLoc);
    		v = Util.trans(q,v);
    		p.setLoc(x+v.x,y+v.y,z+v.z);
    		Quat4d qq = Util.euler2quat(p.innerRot);
    		qq.mul(q);
    		p.setQuat(qq);
    	}
    }
    public void setLocRev(Vector3d loc,Vector3d rev) {
    	setLocRev(loc.x,loc.y,loc.z, rev.x,rev.y,rev.z);
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
