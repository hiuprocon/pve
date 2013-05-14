package com.github.hiuprocon.pve.core;

import com.bulletphysics.dynamics.vehicle.RaycastVehicle;

public class CarConstraint extends Constraint {
    public CarConstraint(RaycastVehicle rv) {
        this.con = rv;
    }
}
