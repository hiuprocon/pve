package com.github.hiuprocon.pve.car;

import java.util.*;

import com.github.hiuprocon.pve.core.ActiveObject;

public interface CarSim {
    public ArrayList<CarBase> getAllCar();
    public void addActiveObject(ActiveObject o);
    public void delActiveObject(ActiveObject o);
}
