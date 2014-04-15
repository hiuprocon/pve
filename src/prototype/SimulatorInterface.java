package prototype;

import com.github.hiuprocon.pve.core.PVEWorld;

public interface SimulatorInterface {
    String searchJewels();
    String stepForward();
    void setWaitTime(int t);
    PVEWorld getPVEWorld();
    String searchObstacles();
    String getElevator1Height();
    String getElevator2Height();
}
