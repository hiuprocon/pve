package prototype;

import jp.sourceforge.acerola3d.a3.*;

public interface CarInterface {
    A3Object getMainA3();
    void setUserData(Object o);
    void setLocRev(double x,double y,double z,double rx,double ry,double rz);
}
