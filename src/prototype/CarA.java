package prototype;

import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import com.github.hiuprocon.pve.ui.Server;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.*;
import javax.vecmath.*;

public class CarA extends SimpleCarObj implements PVEMsgListener {
    public CarA(PVEWorld w, int port) {
        super(w, "x-res:///res/prototype/carA/carA.a3", shassisShape());
        new Server(port, this);
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

        return compound;
    }

    @Override
    public String processMessage(String line) {
        if (line.startsWith("drive"))
            msgDrive(line);
        else if (line.equals("b"))
            setForce(0, 0, 0, 0);
        return "OK";
    }

    void msgDrive(String line) {
        String s[] = line.split("\\s");
        double speed = Double.parseDouble(s[1]);
        double handle = Double.parseDouble(s[2]);
        setForce(speed, handle, 0, 0);
    }
}
