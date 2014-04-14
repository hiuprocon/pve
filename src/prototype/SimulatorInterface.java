package prototype;

import com.github.hiuprocon.pve.core.PVEWorld;

public interface SimulatorInterface {
    String searchJewels();
    void stepForward();
    void setWaitTime(int t);
    PVEWorld getPVEWorld();
}
