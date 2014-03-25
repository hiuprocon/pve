package prototype;

import java.util.ArrayList;
import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;

public class Elevator3 extends PVEObject implements CollisionListener {
    Elevator3Holder holder;
    PVEWorld world;
    Box plate;
    Box rightLeg;
    Box leftLeg;
    PVEPart switch1;
    PVEPart switch2;
    PVEPart goal1;
    PVEPart goal2;
    PVEPart floor1;
    PVEPart floor2;
    boolean up = false;

    public Elevator3(Elevator3Holder holder,PVEWorld world) {
        this.holder = holder;
        this.world = world;
        init();
        world.addCollisionListener(this);
    }

    @Override
    protected PVEPart[] createParts() {
        plate = new Box(Type.KINEMATIC, 0.0, new Vector3d(10, 16, 10));
        rightLeg = new Box(Type.STATIC, 0.0, new Vector3d(10, 15, 1),"x-res:///res/ClearBox.wrl");
        leftLeg = new Box(Type.STATIC, 0.0, new Vector3d(10, 15, 1),"x-res:///res/ClearBox.wrl");
        switch1 = new Box(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/prototype/Switch.wrl");
        switch2 = new Box(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/prototype/Switch.wrl");
        goal1 = new Box(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/prototype/Goal.wrl");
        goal2 = new Box(Type.GHOST, 1, new Vector3d(10, 1, 10),
                "x-res:///res/prototype/Goal.wrl");
        floor1 = new Box(Type.STATIC,0.0,new Vector3d(10,1,10),"x-res:///res/ClearBox.wrl");
        floor2 = new Box(Type.STATIC,0.0,new Vector3d(10,1,10),"x-res:///res/ClearBox.wrl");
        plate.disableDeactivation(true);
        plate.setInitLocRev(0, -8, 0, 0, 0, 0);
        rightLeg.setInitLocRev(0, 7.5, 5.5, 0, 0, 0);
        leftLeg.setInitLocRev(0, 7.5, -5.5, 0, 0, 0);
        switch1.setInitLocRev(0.0, 0.5, 11, 0, 0, 0);
        switch2.setInitLocRev(0.0, 0.5, -11, 0, 0, 0);
        goal1.setInitLocRev(-10, 15.5, 0, 0, 0, 0);
        goal2.setInitLocRev( 10, 15.5, 0, 0, 0, 0);
        floor1.setInitLocRev(-10, 14.5,0,0,0,0);
        floor2.setInitLocRev( 10, 14.5,0,0,0,0);
        return new PVEPart[] { plate, rightLeg, leftLeg, switch1, switch2, goal1, goal2, floor1, floor2 };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return plate;
    }

    Vector3d initXYZ = new Vector3d();
    @Override
    public void setLocRot(double x,double y,double z,double rx,double ry,double rz) {
        super.setLocRot(x,y,z,rx,ry,rz);
        initXYZ.set(x,y,z);
        
    }
    public void setUp() {
        up = true;
    }

    double slide=-8.0;
    double speed=0.02;
    @Override
    protected void postSimulation() {
        if (up==true) {
            if (slide<7.0) {
                slide = slide + speed;
                Vector3d v = new Vector3d(initXYZ);
                v.y = v.y + slide;
                plate.setLoc(v);
            }
            up = false;
        } else {
            if (slide>-8.0) {
                slide = slide - speed;
                Vector3d v = new Vector3d(initXYZ);
                v.y = v.y + slide;
                plate.setLoc(v);
            }
            
        }
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        PVEObject aa = a.getObject();
        PVEObject bb = b.getObject();
        ArrayList<Jewel> alTmp = holder.getJewelsCopy();
        int jewelsCount=0;
        for (Jewel j:alTmp) {
            Vector3d jLoc = j.getLoc();
            if (jLoc.x<5.0 && jLoc.x>-5.0)
                if (jLoc.z<5.0 && jLoc.z>-5.0)
                    jewelsCount++;
        }
        if ((a == switch1 || a == switch2) && (bb instanceof CarInterface) && (jewelsCount<=5))
            setUp();
        if ((aa instanceof CarInterface) && (b == switch1 || b == switch2) && (jewelsCount<=5))
            setUp();
        if ((a == goal1 || a == goal2) && bb instanceof Jewel) {
            holder.processGoal((Jewel)bb);
        }
        if (aa instanceof Jewel && (b == goal1 || b == goal2)) {
            holder.processGoal((Jewel)aa);
        }
    }
}
