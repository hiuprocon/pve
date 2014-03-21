package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;

public class Avatar extends SphereObj {
    public Avatar(String a3url) {
        super(Type.DYNAMIC,1.0,1.0,a3url);
        PVEPart mp = getMainPart();
        mp.setAngularFactor(0);
        mp.disableDeactivation(true);
    }

    public void setForce(double zengo, double sayu) {
        PVEPart mp = getMainPart();
        Quat4d q = mp.getQuat();
        Vector3d front = Util.trans(q,new Vector3d(0,0,1));
        front.scale(zengo);
        Vector3d angVel = new Vector3d(0,sayu,0);
        //mp.applyCentralForce(front);
        mp.setLinearVelocity(front);
        mp.setAngularVelocity(angVel);
    }
}
