package com.github.hiuprocon.pve.car;

import java.util.*;


public interface CarSim {
    public ArrayList<CarBase> getAllCar();
    public void addActiveObject(ActiveObject o);
    public void delActiveObject(ActiveObject o);
}
