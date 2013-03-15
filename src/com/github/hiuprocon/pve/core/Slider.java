package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.SliderConstraint;
import com.bulletphysics.linearmath.Transform;

public class Slider extends Constraint {
    public Slider(PVEPart p1,PVEPart p2) {
    	Transform local1 = new Transform();local1.setIdentity();
    	local1.origin.set( 1,0,0);
    	Transform local2 = new Transform();local2.setIdentity();
    	local2.origin.set(-1,0,0);
    	con = new SliderConstraint(p1.body,p2.body,local1,local2,true);
    }
    public void setMotor(double f) {
    	((SliderConstraint)con).setTargetLinMotorVelocity((float)f);
    }
}
