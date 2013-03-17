package com.github.hiuprocon.pve.core;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.dynamics.constraintsolver.SliderConstraint;
import com.bulletphysics.linearmath.Transform;

public class Slider extends Constraint {
    public Slider(PVEPart a,PVEPart b,Vector3d pivot,Vector3d axis) {
    	con = test2(a,b,pivot,axis);
    }
    static SliderConstraint test1(PVEPart a,PVEPart b,Vector3d pivot,Vector3d axis) {
    	Quat4d q = new Quat4d();
        q=Util.euler2quat(0,0,Math.PI/2-0.01);
    	Transform localA = new Transform();localA.setIdentity();
    	localA.basis.set(q);
    	localA.origin.set(  0.0f, -2.0f,  0.05f);
    	Transform localB = new Transform();localB.setIdentity();
    	localB.basis.set(q);
    	localB.origin.set(  0.0f,  0.0f,  -0.5f);
    	return new SliderConstraint(a.body,b.body,localA,localB,true);
    }
    static SliderConstraint test2(PVEPart a,PVEPart b,Vector3d pivot,Vector3d axis) {
    	Vector3d ax = new Vector3d();
    	double t = Math.PI/2/2;
    	Quat4d pi4 = new Quat4d();

    	Vector3d pivotDir = new Vector3d();
    	Quat4d q = new Quat4d();

    	pivotDir.sub(pivot,a.innerLoc);
    	q=Util.euler2quat(a.innerRot);
    	pivotDir = Util.trans(q, pivotDir);
    	Transform localA = new Transform();localA.setIdentity();
    	localA.origin.set(pivotDir);
    	ax.set(axis);ax.normalize();
    	ax = Util.trans(q, ax);
    	pi4.x=ax.x*Math.sin(t);pi4.y=ax.y*Math.sin(t);pi4.z=ax.z*Math.sin(t);
    	pi4.w=Math.cos(t);
    	localA.basis.set(pi4);

    	pivotDir.sub(pivot,b.innerLoc);
    	q=Util.euler2quat(b.innerRot);
    	pivotDir = Util.trans(q, pivotDir);
    	Transform localB = new Transform();localB.setIdentity();
    	localB.origin.set(pivotDir);
    	ax.set(axis);ax.normalize();
    	ax = Util.trans(q, ax);
    	pi4.x=ax.x*Math.sin(t);pi4.y=ax.y*Math.sin(t);pi4.z=ax.z*Math.sin(t);
    	pi4.w=Math.cos(t);
    	localB.basis.set(pi4);

    	return new SliderConstraint(a.body,b.body,localA,localB,true);
    }
    public Slider(PVEPart a,PVEPart b,Transform localA,Transform localB) {
    	con = new SliderConstraint(a.body,b.body,localA,localB,true);
    }
    public void setMotor(double f) {//??????
    	((SliderConstraint)con).setLowerLinLimit(0.1f);
    	((SliderConstraint)con).setUpperLinLimit(3.9f);
    	((SliderConstraint)con).setPoweredLinMotor(true);
    	((SliderConstraint)con).setMaxLinMotorForce(10.0f);
    	((SliderConstraint)con).setTargetLinMotorVelocity((float)f);
    }
}
