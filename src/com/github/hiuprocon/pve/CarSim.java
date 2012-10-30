package com.github.hiuprocon.pve;

import java.util.*;

interface CarSim {
    public ArrayList<CarBase> getAllCar();
    public void addActiveObject(ActiveObject o);
    public void delActiveObject(ActiveObject o);
}
