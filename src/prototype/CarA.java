package prototype;

import java.util.ArrayList;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import com.github.hiuprocon.pve.ui.Server;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;

public class CarA extends SimpleCarObj implements PVEMsgListener, CarInterface {
    SimulatorInterface simulator;
    Server server;
    double speed;
    double handle;
    CarInterface anotherCar;
    ArrayList<String> messages = new ArrayList<String>();
    ArrayList<String> mailbox = new ArrayList<String>();

    public CarA(SimulatorInterface simulator, int port) {
        //super(simulator.getPVEWorld(), "x-res:///res/prototype/carA.a3", shassisShape());
        super(simulator.getPVEWorld(), port==10000?"x-res:///res/prototype/ChoroQred.a3":"x-res:///res/prototype/ChoroQblue.a3", shassisShape());
        this.simulator = simulator;
        //init();//スーパークラスでやってるので不要
        server = new Server(port, this);
    }

    static CollisionShape shassisShape() {
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

        CollisionShape s4 = new CylinderShape(new Vector3f(0.2f, 0.5f, 0.2f));
        Transform b4 = new Transform();
        b4.setIdentity();
        b4.origin.set(0.0f, 1.0f, 0.0f);
        compound.addChildShape(b4, s4);

        return compound;
    }

    @Override
    protected void postSimulation() {
        setForce(speed,handle,0,0);
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
        return "ERROR";
    }

    @Override
    public void dispose() {
        server.dispose();
    }

    String msgDrive(String line) {
        String s[] = line.split("\\s");
        speed = Double.parseDouble(s[1]);
        handle = Double.parseDouble(s[2]);
        speed = 500*speed;
        handle = -0.1*handle;
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

    public void swapMessageBuffer() {
        synchronized (messages) {
            synchronized (mailbox) {
                mailbox.addAll(messages);
                messages.clear();
            }
        }
    }
    String msgSendMessage(String line) {
        synchronized(anotherCar.getMessages()){
            String msg = line.substring(11);
            msg = msg.trim();
            anotherCar.getMessages().add(msg);
        }
        return "OK";
    }
    @Override
    public void setAnotherCar(CarInterface c) {
        anotherCar = c;
    }

    @Override
    public ArrayList<String> getMessages() {
        return messages;
    }
}
