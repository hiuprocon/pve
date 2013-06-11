package com.github.hiuprocon.pve.obj;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class ForkLift extends PVEObject {
    String a3url;
    PVEWorld world;
    SimpleCar car;
    Box lift;
    Hinge liftHinge;
    Box fork;
    Slider forkSlider;

    public ForkLift(PVEWorld world, String a3url) {
        this.world = world;
        this.a3url = a3url;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        car = new SimpleCar(world, a3url);
        car.setDamping(0.98, 0.5);
        lift = new Box(Type.DYNAMIC, 0.1, new Vector3d(1.0, 2.0, 0.1),
                "x-res:///res/ClearBox.wrl");
        fork = new Box(Type.DYNAMIC, 0.1, new Vector3d(1.0, 0.1, 1.0),
                "x-res:///res/ClearBox.wrl");
        car.setInitLocRot(0.0, 0.0, 0.0, 0, 0, 0);
        lift.setInitLocRot(0.0, 1.1, 1.0, 0, 0, 0);
        fork.setInitLocRot(0.0, 0.2, 1.6, 0, 0, 0);

        return new PVEPart[] { car, lift, fork };
    }

    @Override
    protected Constraint[] createConstraints() {
        liftHinge = new Hinge(car, lift, new Vector3d(0, 0.1, 1.0),
                new Vector3d(1, 0, 0));
        forkSlider = new Slider(lift,fork, new Vector3d(0,0.2,1.6),new
         Vector3d(0,1,0));
        //forkSlider = new Slider(lift, fork, new Vector3d(0, 0, 1.6),
        //       new Vector3d(0, 0, 1));// ????
        return new Constraint[] { car.carConstraint, liftHinge, forkSlider };
    }

    @Override
    protected PVEPart getMainPart() {
        return car;
    }

    public void setForce(double force, double steering, double breaking,
            double drift) {
        car.setForce(force, steering, breaking, drift);
    }

    public void setLiftMotor(double f) {
        liftHinge.setLimit(-Math.PI / 16.0, Math.PI / 8.0);
        liftHinge.enableAngularMotor(true, f, 10);
    }

    public void lockLiftMotor() {
        double LIFT_EPS = 0.0000001;
        double hingeAngle = liftHinge.getHingeAngle();
        double lowLim = liftHinge.getLowerLimit();
        double hiLim = liftHinge.getUpperLimit();
        liftHinge.enableAngularMotor(false, 0, 0);
        if (hingeAngle < lowLim) {
            liftHinge.setLimit(lowLim, lowLim + LIFT_EPS);
        } else if (hingeAngle > hiLim) {
            liftHinge.setLimit(hiLim - LIFT_EPS, hiLim);
        } else {
            liftHinge.setLimit(hingeAngle - LIFT_EPS, hingeAngle + LIFT_EPS);
        }
    }

    public void setForkMotor(double f) {
        forkSlider.setLowerLinLimit(0.1);
        forkSlider.setUpperLinLimit(3.9);
        forkSlider.setPoweredLinMotor(true);
        forkSlider.setMaxLinMotorForce(10.0);
        forkSlider.setTargetLinMotorVelocity(f);
    }

    public void lockForkMotor() {
        double linDepth = forkSlider.getLinearPos();
        double lowLim = forkSlider.getLowerLinLimit();
        double hiLim = forkSlider.getUpperLinLimit();
        forkSlider.setPoweredLinMotor(false);
        if (linDepth <= lowLim) {
            forkSlider.setLowerLinLimit(lowLim);
            forkSlider.setUpperLinLimit(lowLim);
        } else if (linDepth > hiLim) {
            forkSlider.setLowerLinLimit(hiLim);
            forkSlider.setUpperLinLimit(hiLim);
        } else {
            forkSlider.setLowerLinLimit(linDepth);
            forkSlider.setUpperLinLimit(linDepth);
        }
    }
}