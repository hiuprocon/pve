package com.github.hiuprocon.pve.core;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.linearmath.Transform;
import javax.media.j3d.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;

//合成
public class Compound extends PVEPart {
    PVEPart[] parts;

    public Compound(Type type, PVEPart... parts) {
        super(type, calMass(parts), "dummy");
        this.parts = parts;
        for (PVEPart p : parts) {
            p.compound = this;
        }
        init();
    }

    static double calMass(PVEPart parts[]) {
        double m = 0.0;
        for (PVEPart p : parts) {
            m += p.mass;
        }
        return m;
    }

    @Override
    protected A3Object makeA3Object(String a3url) {
        BranchGroup bg = new BranchGroup();
        for (PVEPart p : parts) {
            Transform3D t0 = new Transform3D();
            //Transform3D t1 = new Transform3D();
            //t0.set(p.innerLoc);
            //t1.set(Util.euler2quat(p.innerRot));
            //t0.mul(t1);
            TransformGroup tg = new TransformGroup(t0);
            Node n = p.a3.getNode();
            //Group g = (Group)n.getParent();
            //g.removeChild(n);
            tg.addChild(n);
            bg.addChild(tg);
        }
        return new A3Raw(bg);
    }

    @Override
    public CollisionShape makeCollisionShape() {
        CompoundShape cs = new CompoundShape();
        for (PVEPart p : parts) {
            Matrix4f m0 = new Matrix4f();
            m0.setIdentity();
            Matrix4f m1 = new Matrix4f();
            m1.setIdentity();
            m1.set(new Vector3f(p.innerLoc));
            m0.mul(m1);
            m1.setIdentity();
            m1.set(new Quat4f(Util.euler2quat(p.innerRot)));
            m0.mul(m1);
            Transform t = new Transform(m0);
            CollisionShape shape = p.body.getCollisionShape();
            cs.addChildShape(t,shape);
        }
        return cs;
    }
}
