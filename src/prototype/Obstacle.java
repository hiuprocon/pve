package prototype;

import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.*;

public class Obstacle extends PVEObject {
	Cylinder cylinder;
	Vector3d loc;
	Vector3d vel;
	public Obstacle(Vector3d loc,Vector3d vel) {
		init();
		this.loc = new Vector3d(loc);
		this.vel = new Vector3d(vel);
		setLocRev(loc,new Vector3d());
	}

	@Override
	protected PVEPart[] createParts() {
		cylinder = new Cylinder(Type.KINEMATIC,0.0,1,3);
		return new PVEPart[]{cylinder};
	}

	@Override
	protected Constraint[] createConstraints() {
		return new Constraint[0];
	}

	@Override
	protected PVEPart getMainPart() {
		return cylinder;
	}

	@Override
	protected void postSimulation() {
        if (loc.x> 125) {loc.x= 125;vel.x*=-1;}
        if (loc.x<-125) {loc.x=-125;vel.x*=-1;}
        if (loc.y> 100) {loc.y= 100;vel.y*=-1;}
        if (loc.y<-100) {loc.y=-100;vel.y*=-1;}
        if (loc.z>  50) {loc.z=  50;vel.z*=-1;}
        if (loc.z< -50) {loc.z= -50;vel.z*=-1;}
		loc.add(vel);
		cylinder.setLoc(loc);
	}
}
