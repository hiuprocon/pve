package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

public abstract class PVEObject {
    final PVEPart[] parts;
    final TypedConstraint[] constraints;
    PVEWorld world;
    public PVEObject() {
    	parts = createParts();
    	constraints = createConstraints();
    }
    protected abstract PVEPart[] createParts();
    protected abstract TypedConstraint[] createConstraints();
    void init(PVEWorld world) {
    	this.world = world;
    	for (PVEPart p : parts)
    		p.init(world);
    }
}
