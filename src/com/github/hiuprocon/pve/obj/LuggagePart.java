package com.github.hiuprocon.pve.obj;

import javax.vecmath.Vector3f;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.linearmath.Transform;
import com.github.hiuprocon.pve.core.*;

public class LuggagePart extends PVEPart {
    public LuggagePart() {
        super(Type.DYNAMIC, 1.0, "x-res:///res/Luggage.wrl");
        init();
    }

    @Override
    protected CollisionShape makeCollisionShape() {
        CompoundShape compound = new CompoundShape();
        BoxShape centerBox = new BoxShape(new Vector3f(1.5f / 2.0f,
                0.5f / 2.0f, 0.5f / 2.0f));
        Transform localTrans = new Transform();
        localTrans.setIdentity();
        compound.addChildShape(localTrans, centerBox);
        BoxShape rightBox = new BoxShape(new Vector3f(0.2f / 2.0f, 1.5f / 2.0f,
                1.5f / 2.0f));
        localTrans.setIdentity();
        localTrans.origin.set(0.95f, 0, 0);
        compound.addChildShape(localTrans, rightBox);
        BoxShape leftBox = new BoxShape(new Vector3f(0.2f / 2.0f, 1.5f / 2.0f,
                1.5f / 2.0f));
        localTrans.setIdentity();
        localTrans.origin.set(-0.95f, 0, 0);
        compound.addChildShape(localTrans, leftBox);
        return compound;
    }
}
