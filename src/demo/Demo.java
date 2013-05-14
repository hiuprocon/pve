package demo;

import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;

public class Demo implements CollisionListener {
	static PVEObject ground;
    public static void main(String args[]) {
	    PVEWorld w = new PVEWorld(PVEWorld.A3CANVAS);
	    DemoGUI gui = new DemoGUI((A3Canvas)w.getMainCanvas());
	    w.addCollisionListener(new Demo());
	    w.resume();
	    
	    ground = new Ground("x-rzip:x-res:///res/stk_racetrack.zip!/racetrack.wrl");
	    //ground = new Ground("x-res:///res/background0.wrl");
	    //ground = new FreeObjC(Type.STATIC,"x-res:///res/background0.wrl");
	    //ground = new BoxObj(Type.STATIC,0,new Vector3d(100,1,100),"x-res:///res/background0.wrl");g.setLocRev(0,-0.5,0, 0,0,0);
	    w.add(ground);

	    //SimpleCarObj c = new SimpleCarObj(w,"x-res:///res/stk_tux.a3");
	    //KeyboardSimpleCar c = new KeyboardSimpleCar(w,"x-res:///res/stk_tux.a3");
	    //KeyboardForkLift c = new KeyboardForkLift(w,"x-res:///res/stk_tux.a3");
	    KeyboardHeavyCar c = new KeyboardHeavyCar();
	    c.setLocRev(0,1,0, 0,0,0);
	    w.add(c);gui.aKL = c;

	    //BoxObj b = new BoxObj(Type.DYNAMIC,1,new Vector3d(1,2,1),"x-res:///res/Box.wrl");
	    //CylinderObj b = new CylinderObj();
	    //ConeObj b = new ConeObj();
	    //SphereObj b = new SphereObj();
	    //FreeObjA b = new FreeObjA(Type.STATIC,"x-res:///res/axis.wrl");
	    //FreeObjB b = new FreeObjB(Type.DYNAMIC,"x-res:///res/axis.wrl");
	    //FreeObjC b = new FreeObjC(Type.DYNAMIC,"x-res:///res/axis.wrl");
	    //Luggage b = new Luggage();
	    //b.setLocRev(new Vector3d(0,10,5), new Vector3d(0,0,0));
        Seesaw b = new Seesaw();b.setLocRev(0,0,35, 0,0,0);
	    w.add(b);
    }

	@Override
	public void collided(PVEPart a, PVEPart b) {
		PVEObject aa = a.getObject();
		PVEObject bb = b.getObject();
		if (aa==ground || bb==ground) return;
		System.out.println("collided "+aa.getClass().getName()+":"+
				bb.getClass().getName());
	}
}
