package etrobo;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class SimEnv {
    PVEWorld w;
    SimGUI gui;
    ETRobo robo;

    public SimEnv() {
        w = new PVEWorld(PVEWorld.A3CANVAS,PVEWorld.MANUAL_STEP,0.1f);
        gui = new SimGUI(this);
        SimEnvImp imp = new SimEnvImp();
        w.addCollisionListener(imp);
        w.resume();
    }

    public void add(PVEObject obj) {
        if (obj instanceof ETRobo) {
            robo = (ETRobo)obj;
            gui.setETRobo(robo);
            robo.setSimEnv(this);
        }
        w.add(obj);
        //たぶんstepForwardをやってあげてからでないと
        //重力の変更はできないと思う．
        w.stepForward();
        obj.setGravity(new Vector3d(0,-1.176,0));
    }

    public void loop() {
        while (true) {
            w.stepForward();
            try{Thread.sleep(4);}catch(Exception e){;}
        }
    }

    public void stepForward() {
        w.stepForward();
    }

    class SimEnvImp implements CollisionListener{
        //衝突検知ができます．
        @Override
        public void collided(PVEPart a, PVEPart b) {
            /*
            PVEObject aa = a.getObject();
            PVEObject bb = b.getObject();
            if (aa==ground || bb==ground) return;
            System.out.println("collided "+aa.getClass().getName()+":"+
                    bb.getClass().getName());
            */
        }
    }
}
