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
        plate = new Box(Type.DYNAMIC, 1.0, new Vector3d(10, 1, 10));
        rightLeg = new Box(Type.STATIC, 0.0, new Vector3d(1, 5, 10),"x-res:///res/ClearBox.wrl");
        leftLeg = new Box(Type.STATIC, 0.0, new Vector3d(1, 5, 10),"x-res:///res/ClearBox.wrl");
        plate.disableDeactivation(true);
        plate.setInitLocRev(0, 0.5, 0, 0, 0, 0);
        rightLeg.setInitLocRev(5.5, 2.5, 0, 0, 0, 0);
        leftLeg.setInitLocRev(-5.5, 2.5, 0, 0, 0, 0);
        return new PVEPart[] { plate, rightLeg, leftLeg };
    }

    @Override
    protected Constraint[] createConstraints() {
        rightSlider = new Slider(plate, rightLeg, new Vector3d(5, 0.5, 0),
                new Vector3d(0, 1, 0));
        leftSlider = new Slider(plate, rightLeg, new Vector3d(-5, 0.5, 0),
                new Vector3d(0, 1, 0));
double linDepth = rightSlider.getLinearPos();
System.out.println("GAHA0:"+linDepth);
        return new Constraint[] { rightSlider, leftSlider };
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
if (v.x>0){
double linDepth = rightSlider.getLinearPos();
System.out.println("GAHA:"+linDepth);
}
        if (up == true) {
            if (v.y < 4.5)
                slideUp();
            else
                lockSlider();
        } else {
            if (v.y > 0.5)
                slideDown();
            else
                lockSlider();
        }
        up = false;
    }

    double lowerLinLimit = 0.0;
    double upperLinLimit = 4.0;
    void slideUp() {
        //double linDepth = rightSlider.getLinearPos();
        //System.out.println("GAHA:slideUp"+linDepth);
        rightSlider.setLowerLinLimit(lowerLinLimit);
        rightSlider.setUpperLinLimit(upperLinLimit);
        rightSlider.setPoweredLinMotor(true);
        rightSlider.setMaxLinMotorForce(10.0);
        rightSlider.setTargetLinMotorVelocity(5);

        leftSlider.setLowerLinLimit(lowerLinLimit);
        leftSlider.setUpperLinLimit(upperLinLimit);
        leftSlider.setPoweredLinMotor(true);
        leftSlider.setMaxLinMotorForce(10.0);
        leftSlider.setTargetLinMotorVelocity(5);
    }

    void slideDown() {
        //double linDepth = rightSlider.getLinearPos();
        //System.out.println("GAHA:slideDown"+linDepth);
        rightSlider.setLowerLinLimit(lowerLinLimit);
        rightSlider.setUpperLinLimit(upperLinLimit);
        rightSlider.setPoweredLinMotor(true);
        rightSlider.setMaxLinMotorForce(10.0);
        rightSlider.setTargetLinMotorVelocity(-5);

        leftSlider.setLowerLinLimit(lowerLinLimit);
        leftSlider.setUpperLinLimit(upperLinLimit);
        leftSlider.setPoweredLinMotor(true);
        leftSlider.setMaxLinMotorForce(10.0);
        leftSlider.setTargetLinMotorVelocity(-5);
    }

    void lockSlider() {
        double linDepth = rightSlider.getLinearPos();
        //System.out.println("GAHA:lockSlider" + linDepth);
        if (linDepth < lowerLinLimit)
            linDepth = lowerLinLimit;
        if (linDepth > upperLinLimit)
            linDepth = upperLinLimit;
        rightSlider.setLowerLinLimit(linDepth);
        rightSlider.setUpperLinLimit(linDepth);
        linDepth = leftSlider.getLinearPos();
        if (linDepth < lowerLinLimit)
            linDepth = lowerLinLimit;
        if (linDepth > upperLinLimit)
            linDepth = upperLinLimit;
        leftSlider.setLowerLinLimit(linDepth);
        leftSlider.setUpperLinLimit(linDepth);
    }
}
