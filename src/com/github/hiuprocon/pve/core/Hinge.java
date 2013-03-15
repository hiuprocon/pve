package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.linearmath.Transform;

public class Hinge extends Constraint {
    public Hinge(PVEPart p1,PVEPart p2) {
    	Transform local1 = new Transform();local1.setIdentity();
    	local1.origin.set( 1,0,0);
    	Transform local2 = new Transform();local2.setIdentity();
    	local2.origin.set(-1,0,0);
    	con = new HingeConstraint(p1.body,p2.body,local1,local2);
    }
    public void setMotor(double f) {
    	//TODO
    }
}
