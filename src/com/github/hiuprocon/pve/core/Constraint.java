package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

public abstract class Constraint {
    public boolean disableCollisionsBetweenLinkedBodies = true;
    protected TypedConstraint con;
}
