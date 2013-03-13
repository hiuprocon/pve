package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

public abstract class PVEObject {
    PVEPart[] parts;
    TypedConstraint[] constraints;
    protected PVEWorld world;
    public PVEObject() {
    }
    protected abstract PVEPart[] createParts();
    protected abstract TypedConstraint[] createConstraints();
    void init(PVEWorld world) {
    	this.world = world;
    	parts = createParts();
    	constraints = createConstraints();
    	for (PVEPart p : parts)
    		p.internalInit(world);
    }
}
