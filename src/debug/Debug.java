package debug;

import javax.vecmath.*;
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;

public class Debug implements CollisionListener {
    public static void main(String args[]) {
	    PVEWorld w = new PVEWorld(PVEWorld.A3CANVAS);
	    DebugGUI gui = new DebugGUI((A3Canvas)w.getMainCanvas());
	    w.resume();
	    
	    //Ground g = new Ground("x-rzip:x-res:///res/stk_racetrack.a3!/racetrack.wrl");
	    Ground g = new Ground("x-res:///res/background0.a3");
	    w.add(g);

	    //SimpleCarObj c = new SimpleCarObj(w,"x-res:///res/stk_tux.a3");
	    //KeyboardSimpleCar c = new KeyboardSimpleCar(w,"x-res:///res/stk_tux.a3");
	    KeyboardForkLift c = new KeyboardForkLift(w,"x-res:///res/stk_tux.a3");
	    c.setLoc(0,1,0);
	    w.add(c);gui.aKL = c;

	    BoxObj b = new BoxObj(Type.DYNAMIC,1,new Vector3d(1,2,1),"x-res:///res/Box.wrl");
	    b.setLoc(0,1,5);
	    w.add(b);
    }

	@Override
	public void collided(PVEPart a, PVEPart b) {
		PVEObject aa = a.getObject();
		PVEObject bb = b.getObject();
		System.out.println("collided "+aa.getClass().getName()+":"+
				bb.getClass().getName());
	}
}
