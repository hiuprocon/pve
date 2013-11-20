package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

public class Bullet extends PVEObject {
    String a3url;
    Sphere sphere;

    public Bullet(Vector3d loc, Vector3d vel) {
        this("x-res:///res/bullet.a3", loc, vel);
    }

    public Bullet(String a3url, Vector3d loc, Vector3d vel) {
        this.a3url = a3url;
        init();
        PVEPart p = this.getMainPart();
        p.setGravity(new Vector3d());//なぜか効果なしpostSimulationで？
        p.setInitLocRot(loc, new Vector3d());
        setVel(vel);
        A3Object a = getMainA3();
        a.setScale(2.0);
    }

    @Override
    protected PVEPart[] createParts() {
        sphere = new Sphere(Type.DYNAMIC, 1.0, 0.2, a3url);
        // sphere.setInitLocRot(0,0,0, 0,0,0);
        return new PVEPart[] { sphere };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return sphere;
    }

    boolean initGravity = false;
    @Override
    protected void postSimulation() {
        if (initGravity==false) {
            setGravity(new Vector3d());//ここでやるしかない？？？
            initGravity = true;
        }
    }
}
