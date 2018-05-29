package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.A3Object;
import jp.sourceforge.acerola3d.a3.Util;

public abstract class PVEObject {
    PVEPart mainPart;
    PVEPart[] parts;
    Constraint[] constraints;
    Object userData;

    public PVEObject() {
    }

    protected abstract PVEPart[] createParts();

    protected abstract Constraint[] createConstraints();

    protected abstract PVEPart getMainPart();

    protected void postSimulation() {
        ;
    }

    /**
     * このメソッドはコンストラクタの中で必ず呼び出さなければならない。
     */
    protected final void init() {
        parts = createParts();
        for (PVEPart p : parts)
            p.obj = this;
        constraints = createConstraints();
        mainPart = getMainPart();
    }

    /**
     * setLocRot。 パーツの相対的な関係などの情報は失われる。
     * これをオーバーライドすればsetLocRot(Vector3d,Vector3d),setLocRev(double,double,double,double,double,double),
     * setLocRev(Vector3d,Vector3d)を実行した時にも呼ばれるようにしてある。
     */
    public void setLocRot(double x, double y, double z, double rx, double ry,
            double rz) {
        Quat4d q = Util.euler2quat(rx, ry, rz);
        for (PVEPart p : parts) {
            Vector3d v = new Vector3d(p.innerLoc);
            v = Util.trans(q, v);
            p.setLoc(x + v.x, y + v.y, z + v.z);
            Quat4d qq = Util.euler2quat(p.innerRot);
            qq.mul(q,qq);
            p.setQuat(qq);
        }
    }

    public void setLocRot(Vector3d loc, Vector3d rev) {
        setLocRot(loc.x, loc.y, loc.z, rev.x, rev.y, rev.z);
    }

    /**
     * setLocRev。 パーツの相対的な関係などの情報は失われる
     */
    public void setLocRev(double x, double y, double z, double rx, double ry,
            double rz) {
        setLocRot(x,y,z,rx/180.0*Math.PI,ry/180.0*Math.PI,rz/180.0*Math.PI);
    }

    public void setLocRev(Vector3d loc, Vector3d rev) {
        setLocRev(loc.x, loc.y, loc.z, rev.x, rev.y, rev.z);
    }

    public void setVel(double x, double y, double z) {
        for (PVEPart p : parts) {
            p.setVel(x, y, z);
        }
    }

    public void setVel(Vector3d vel) {
        setVel(vel.x, vel.y, vel.z);
    }

    public void setVelInLocal(Vector3d vel) {
        Quat4d q = getQuat();
        Vector3d v = Util.trans(q,vel);
        setVel(v);
    }

    public void setVelInLocal(double x, double y, double z) {
        setVelInLocal(new Vector3d(x,y,z));
    }

    public void setAngVel(double x, double y, double z) {
        mainPart.setAngVel(x, y, z);
    }

    public void setAngVel(Vector3d av) {
        setAngVel(av.x, av.y, av.z);
    }

    public void setAngVelInLocal(double x, double y, double z) {
        //自信なし
        //オイラー角なんだけど強さの調節をどうしよう
        Quat4d q0 = getQuat();
        Quat4d q1 = Util.euler2quat(x,y,z);
        Vector3d v = Util.trans(q0,new Vector3d(q1.x,q1.y,q1.z));
        q1.x = v.x; q1.y = v.y; q1.z = v.z; 
        v = Util.quat2euler(q1);
        setAngVel(v.x, v.y, v.z);
    }

    public void setAngVelInLocal(Vector3d av) {
        setAngVelInLocal(av.x,av.y,av.z);
    }

    public void setForce(double x, double y, double z) {
        for (PVEPart p : parts) {
            p.setForce(x, y, z);
        }
    }

    public void setForce(Vector3d f) {
        setForce(f.x, f.y, f.z);
    }

    public void setForceInLocal(double x, double y, double z) {
        setForceInLocal(new Vector3d(x, y, z));
    }

    public void setForceInLocal(Vector3d f) {
        Quat4d q = getQuat();
        Vector3d v = Util.trans(q,f);
        setForce(v);
    }

    public void setTorque(double x, double y, double z) {
        mainPart.setTorque(x, y, z);
    }

    public void setTorque(Vector3d t) {
        setTorque(t.x, t.y, t.z);
    }

    public void setTorqueInLocal(double x, double y, double z) {
        //自信なし
        //オイラー角なんだけど強さの調節をどうしよう
        Quat4d q0 = getQuat();
        Quat4d q1 = Util.euler2quat(x,y,z);
        Vector3d v = Util.trans(q0,new Vector3d(q1.x,q1.y,q1.z));
        q1.x = v.x; q1.y = v.y; q1.z = v.z; 
        v = Util.quat2euler(q1);
        setTorque(v);
    }

    public void setTorqueInLocal(Vector3d t) {
        //オイラー角なんだけど強さの調節をどうしよう
        setTorqueInLocal(t.x, t.y, t.z);
    }

    public void goForward(double v) {
        setVelInLocal(0,0,v);
    }

    public void goBackward(double v) {
        setVelInLocal(0,0,-v);
    }

    public void goRight(double v) {
        setVelInLocal(-v,0,0);
    }

    public void goLeft(double v) {
        setVelInLocal(v,0,0);
    }

    public void goUp(double v) {
        setVelInLocal(0,v,0);
    }

    public void goDown(double v) {
        setVelInLocal(0,-v,0);
    }

    public void turnRight(double v) {
        setAngVelInLocal(0,v,0);
    }

    public void turnLeft(double v) {
        setAngVelInLocal(0,-v,0);
    }

    public void turnUp(double v) {
        setAngVelInLocal(-v,0,0);
    }

    public void turnDown(double v) {
        setAngVelInLocal(v,0,0);
    }

    public void rollRight(double v) {
        setAngVelInLocal(0,0,v);
    }

    public void rollLeft(double v) {
        setAngVelInLocal(0,0,-v);
    }

    //ラグがあるので注意
    public Vector3d getLoc() {
        Vector3d v = mainPart.getLoc();
        Quat4d q = getQuat();
        Vector3d vv = new Vector3d(mainPart.innerLoc);
        vv = Util.trans(q,vv);
        v.sub(vv);
        return v;
    }

    //ラグがあるので注意
    public Quat4d getQuat() {
        Quat4d q = mainPart.getQuat();
        Quat4d qq = Util.euler2quat(mainPart.innerRot);
        qq.conjugate();
        q.mul(qq);
        return q;
    }

    //ラグがあるので注意
    public Vector3d getRot() {
        return Util.quat2euler(getQuat());
    }

    //ラグがあるので注意
    public Vector3d getRev() {
        Vector3d r = Util.quat2euler(getQuat());
        r.scale(180/Math.PI);
        return r;
    }

    public A3Object getMainA3() {
        return mainPart.a3;
    }

    public void setGravity(Vector3d g) {
        for (PVEPart p : parts) {
            p.setGravity(g);
        }
    }

    public void setUserData(Object o) {
        userData = o;
    }

    public Object getUserData() {
        return userData;
    }

    public void setRestitution(double r) {
        for (PVEPart p : parts) {
            p.setRestitution(r);
        }
    }

    public void setFriction(double f) {
        for (PVEPart p : parts) {
            p.setFriction(f);
        }
    }
}
