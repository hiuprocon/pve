package prototype;

import javax.vecmath.*;
import com.github.hiuprocon.pve.core.*;

public class Lifter extends PVEObject {
    Box plate;
    Box rightLeg;
    Box leftLeg;
    Slider rightSlider;
    Slider leftSlider;
    boolean up = false;
    public Lifter() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        plate = new Box(Type.DYNAMIC,1.0,new Vector3d(10,1,10));
        plate.disableDeactivation(true);
        rightLeg = new Box(Type.STATIC,0.0,new Vector3d(10,5,1));
        leftLeg = new Box(Type.STATIC,0.0,new Vector3d(10,5,1));
        plate.setInitLocRev(0,0.5,0, 0,0,0);
        rightLeg.setInitLocRev(0,2,5.5, 0,0,0);
        leftLeg.setInitLocRev(0,2,-5.5, 0,0,0);
        return new PVEPart[]{plate,rightLeg,leftLeg};
    }

    @Override
    protected Constraint[] createConstraints() {
        rightSlider = new Slider(plate,rightLeg,new Vector3d(0,0.5,5),new Vector3d(0,0,1));
        leftSlider = new Slider(plate,rightLeg,new Vector3d(0,0.5,-5),new Vector3d(0,0,1));
        return new Constraint[]{rightSlider,leftSlider};
    }

    @Override
    protected PVEPart getMainPart() {
        return plate;
    }

    public void setUp() {
        up = true;
    }

    @Override
    protected void postSimulation() {
        Vector3d v = plate.getLoc();
        if (up==true) {
            if (v.y<4.5)
                slideUp();
            else
                lockSlider();
        } else {
            if (v.y>0.5)
                slideDown();
            else
                lockSlider();
        }
        up = false;
    }

    void slideUp() {
        //System.out.println("GAHA:slideUp");
        rightSlider.setLowerLinLimit(-4.5);
        rightSlider.setUpperLinLimit(-0.5);
        rightSlider.setPoweredLinMotor(true);
        rightSlider.setMaxLinMotorForce(10.0);
        rightSlider.setTargetLinMotorVelocity(-5);

        leftSlider.setLowerLinLimit(-4.5);
        leftSlider.setUpperLinLimit(-0.5);
        leftSlider.setPoweredLinMotor(true);
        leftSlider.setMaxLinMotorForce(10.0);
        leftSlider.setTargetLinMotorVelocity(-5);
    }
    void slideDown() {
        //System.out.println("GAHA:slideDown");
        rightSlider.setLowerLinLimit(-4.5);
        rightSlider.setUpperLinLimit(-0.5);
        rightSlider.setPoweredLinMotor(true);
        rightSlider.setMaxLinMotorForce(10.0);
        rightSlider.setTargetLinMotorVelocity(5);

        leftSlider.setLowerLinLimit(-4.5);
        leftSlider.setUpperLinLimit(-0.5);
        leftSlider.setPoweredLinMotor(true);
        leftSlider.setMaxLinMotorForce(10.0);
        leftSlider.setTargetLinMotorVelocity(5);
    }
    void lockSlider() {
        double linDepth = rightSlider.getLinearPos();
System.out.println("GAHA:lockSlider"+linDepth);
        if (linDepth<-4.5) linDepth=-4.5;
        if (linDepth>-0.5) linDepth=-0.5;
        rightSlider.setLowerLinLimit(linDepth);
        rightSlider.setUpperLinLimit(linDepth);
        linDepth = leftSlider.getLinearPos();
        if (linDepth<-4.5) linDepth=-4.5;
        if (linDepth>-0.5) linDepth=-0.5;
        leftSlider.setLowerLinLimit(linDepth);
        leftSlider.setUpperLinLimit(linDepth);
    }
}
