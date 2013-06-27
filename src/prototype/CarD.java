package prototype;

import java.util.ArrayList;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.ui.Server;
import jp.sourceforge.acerola3d.a3.Action3D;

public class CarD extends PVEObject implements PVEMsgListener, CarInterface {
    Simulator2 simulator;
    Server server;
    PVEPart chassis;
    double speed;
    double handle;
    CarD anotherCar;
    ArrayList<String> messages = new ArrayList<String>();

    public CarD(Simulator2 simulator,int port) {
        this.simulator = simulator;
        init();
        server = new Server(port,this);
        if (port==10000) {
            ((Action3D)chassis.getA3Object()).change("red");
        } else if (port==20000) {
            ((Action3D)chassis.getA3Object()).change("blue");
        }
    }

    @Override
    protected PVEPart[] createParts() {
        CompoundShape compound = new CompoundShape();

        CollisionShape chassisShape = new BoxShape(new Vector3f(0.4f, 0.25f,
                0.75f));
        Transform b1 = new Transform();
        b1.setIdentity();
        b1.origin.set(0, 0.25f, 0);
        compound.addChildShape(b1, chassisShape);

        CollisionShape s2 = new BoxShape(new Vector3f(0.05f, 0.25f, 0.5f));
        Transform b2 = new Transform();
        b2.setIdentity();
        b2.origin.set(0.754f, 0.25f, 1.104f);
        b2.basis.rotY(0.785f);
        compound.addChildShape(b2, s2);

        CollisionShape s3 = new BoxShape(new Vector3f(0.05f, 0.25f, 0.5f));
        Transform b3 = new Transform();
        b3.setIdentity();
        b3.origin.set(-0.754f, 0.25f, 1.104f);
        b3.basis.rotY(-0.785f);
        compound.addChildShape(b3, s3);

        chassis = new FreeShapeD(Type.DYNAMIC, 10.0,"x-res:///res/prototype/carD.a3",compound);
        chassis.setInitLocRev(0, 0, 0, 0, 0, 0);
        chassis.disableDeactivation(true);
        chassis.setDamping(0.9, 0.0);
        // chassis.setAngularFactor(0);
        return new PVEPart[] { chassis };
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[] {};
    }

    @Override
    protected PVEPart getMainPart() {
        return chassis;
    }

    public void setAnotherCar(CarD ac) {
        anotherCar = ac;
    }

    public void drive(double speed, double handle) {
        this.speed = speed;
        this.handle = handle;
    }

    @Override
    protected void postSimulation() {
        Quat4d q = chassis.getQuat();
        Vector3d front = Util.trans(q, new Vector3d(0, 0, 1));
        front.scale(10 * speed);
        Vector3d angVel = new Vector3d(0, handle, 0);
        chassis.applyCentralForce(front);
        chassis.setAngularVelocity(angVel);
    }

    @Override
    public String processMessage(String line) {
        if (line.startsWith("drive"))
            return msgDrive(line);
        else if (line.equals("getLoc"))
            return msgGetLoc(line);
        else if (line.equals("getRev"))
            return msgGetRev(line);
        else if (line.equals("searchJewels"))
            return msgSearchJewels(line);
        else if (line.equals("stepForward"))
            return msgStepForward(line);
        else if (line.startsWith("sendMessage"))
            return msgSendMessage(line);
        else if (line.equals("receiveMessages"))
            return msgReceiveMessages(line);
        return "ERROR";
    }

    @Override
    public void dispose() {
        server.dispose();
    }

    String msgDrive(String line) {
        String s[] = line.split("\\s");
        double speed = Double.parseDouble(s[1]);
        double handle = Double.parseDouble(s[2]);
        speed = 20*speed;
        handle = -0.5*handle;
        drive(speed, handle);
        return "OK";
    }
    String msgGetLoc(String line) {
        Vector3d v = getLoc();
        return ""+v.x+" "+v.y+" "+v.z;
    }
    String msgGetRev(String line) {
        Vector3d v = Util.rot2rev(getRot());
        return ""+v.x+" "+v.y+" "+v.z;
    }
    String msgSearchJewels(String line) {
        return simulator.searchJewels();
    }
    String msgStepForward(String line) {
        simulator.stepForward();
        return "OK";
    }
    String msgSendMessage(String line) {
        synchronized(anotherCar.messages){
            String msg = line.substring(11);
            msg = msg.trim();
            anotherCar.messages.add(msg);
        }
        return "OK";
    }
    String msgReceiveMessages(String line) {
        String msgs = "";
        synchronized (messages) {
            boolean first=true;
            for (String s : messages) {
                if (first==true) {
                    msgs = msgs+" "+s;
                    first = false;
                } else {
                    msgs = msgs+","+s;
                }
            }
            messages.clear();
        }
        return "messages:"+msgs;
    }
}
