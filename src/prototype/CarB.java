package prototype;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.ui.Server;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.*;

public class CarB extends PVEObject implements PVEMsgListener {
    //FreeShapeD chassis;
    PVEPart chassis;
    double speed;
    double handle;
    Vector3d loc = new Vector3d();
    Vector3d rev = new Vector3d();
    public CarB(int port) {
        init();
        new Server(port,this);
    }

    @Override
    protected PVEPart[] createParts() {
        CompoundShape compound = new CompoundShape();

        CollisionShape chassisShape = new BoxShape(new Vector3f(0.4f,0.25f,0.75f));
        Transform b1 = new Transform();
        b1.setIdentity();
        b1.origin.set(0, 0.25f, 0);
        compound.addChildShape(b1, chassisShape);

        CollisionShape s2 = new BoxShape(new Vector3f(0.05f,0.25f,0.5f));
        Transform b2 = new Transform();
        b2.setIdentity();
        b2.origin.set(0.754f,0.25f,1.104f);
        b2.basis.rotY(0.785f);
        compound.addChildShape(b2,s2);

        CollisionShape s3 = new BoxShape(new Vector3f(0.05f,0.25f,0.5f));
        Transform b3 = new Transform();
        b3.setIdentity();
        b3.origin.set(-0.754f,0.25f,1.104f);
        b3.basis.rotY(-0.785f);
        compound.addChildShape(b3,s3);

        chassis = new FreeShapeD(Type.KINEMATIC,80.0,"x-res:///res/prototype/carB.wrl",compound);
        //chassis = new Box(Type.DYNAMIC,80.0,new Vector3d(3,1,4));
        chassis.setInitLocRev(0,0.25,0, 0,0,0);
        chassis.disableDeactivation(true);
        chassis.setDamping(0.5,0.0);
        //chassis.setAngularFactor(0);
        return new PVEPart[]{chassis};
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[]{};
    }

    @Override
    protected PVEPart getMainPart() {
        return chassis;
    }

    @Override
    public void setLocRot(double x,double y,double z,double rx,double ry,double rz) {
        super.setLocRot(x,y,z,rx,ry,rz);
        loc.set(x,y,z);
        rev.set(180*rx/Math.PI,180*ry/Math.PI,180*rz/Math.PI);
    }

    @Override
    public void setLocRev(double x,double y,double z,double rx,double ry,double rz) {
        super.setLocRev(x,y,z,rx,ry,rz);
        loc.set(x,y,z);
        rev.set(rx,ry,rz);
    }

    public void drive(double speed,double handle) {
        this.speed = speed;
        this.handle = handle;
    }

    @Override
    protected void postSimulation() {
        Quat4d q = chassis.getQuat();
        Vector3d front = Util.trans(q,new Vector3d(0,0,1));
        front.scale(speed/10.0);
        Vector3d angVel = new Vector3d(0,handle,0);
        loc.add(front);
        rev.add(angVel);
        chassis.setLoc(loc);
        chassis.setRev(rev);
    }

    @Override
    public String processMessage(String line) {
        if (line.startsWith("drive"))
            msgDrive(line);
        else if (line.equals("b"))
            drive(0,0);
        return "OK";
    }
    void msgDrive(String line) {
        String s[] = line.split("\\s");
        double speed = Double.parseDouble(s[1]);
        double handle = Double.parseDouble(s[2]);
        drive(speed,handle);
    }
}
