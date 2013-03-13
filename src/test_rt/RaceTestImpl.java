package test_rt;

import java.util.ArrayList;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;


import com.github.hiuprocon.pve.car.CarBase;
import com.github.hiuprocon.pve.car.CarSim;
import com.github.hiuprocon.pve.core.ActiveObject;
import com.github.hiuprocon.pve.core.PVEWorld;
import com.github.hiuprocon.pve.parts.Box;
import com.github.hiuprocon.pve.parts.MyGround2;

class RaceTestImpl implements CarSim, Runnable {
    PVEWorld world;
    KeyboardRaceCar car;
    A3Window window;

    RaceTestImpl() {
        world = new PVEWorld(PVEWorld.A3WINDOW);
        window = (A3Window)world.getMainCanvas();
        window.setSize(500,300);

        MyGround2 g = new MyGround2();
        world.add(g);

        for (double x=-3.0;x<=3.0;x+=2.0) {
            for (double y=1.0;y<=7.0;y+=2.0) {
                for (double z=-3.0;z<=3.0;z+=2.0) {
                    Box b = new Box(x,y,z+20);//立方体
                    world.add(b);
                }
            }
        }

        car = new KeyboardRaceCar();
        car.car.init(new Vector3d(0,0.8,-1),new Vector3d(0,3.1,0),"x-res:///res/stk_tux.a3",world,this);
        window.addKeyListener(car);
        world.add(car.car.car);

        window.setAvatar(car.car.car.a3);
        Vector3d lookAt = new Vector3d(0.0,0.0,6.0);
        Vector3d camera = new Vector3d(0.0,3.0,-6.0);
        Vector3d up = new Vector3d(0.0,1.0,0.0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,lookAt,camera,up,1.0);
        //window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,3.0);

        world.addTask(this);

        world.resume();
    }

    @Override
    public ArrayList<CarBase> getAllCar() {
        ArrayList<CarBase> al = new ArrayList<CarBase>();
        al.add(car.car);
        return al;
    }

    @Override
    public void addActiveObject(ActiveObject o) {;}

    @Override
    public void delActiveObject(ActiveObject o) {;}

    @Override
    public void run() {
        car.exec();
    }
}
