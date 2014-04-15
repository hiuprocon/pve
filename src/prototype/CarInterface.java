package prototype;

import jp.sourceforge.acerola3d.a3.*;
import java.util.ArrayList;
import com.github.hiuprocon.pve.ui.Server;

public interface CarInterface {
    A3Object getMainA3();
    void setUserData(Object o);
    void setLocRev(double x,double y,double z,double rx,double ry,double rz);
    void dispose();
    void swapMessageBuffer();
    void setAnotherCar(CarInterface c);
    ArrayList<String> getMessages();
    Server getServer();
}
