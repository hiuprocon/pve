package etrobo;

import jp.sourceforge.acerola3d.a3.*;
import java.awt.event.*;
import javax.vecmath.*;

public class MyController extends A3Controller implements Runnable {
    volatile boolean stopRequest = false;
    int preX;
    int preY;
    double yaw=0.0;
    double pitch=-45.0;
    double scale=30.0;

    public void init() {
        stopRequest = false;
        a3canvas.setCameraRev(-60,0,0);
        a3canvas.setCameraScale(10.0);//ん?これが無いとちらつく時ある
        Thread t = new Thread(this);
        t.start();
    }
    public void stop() {
        stopRequest = true;
    }
    public void run() {
        while (!stopRequest) {
            Vector3d v = new Vector3d(0,0,scale);
            Quat4d q = Util.euler2quat(pitch/180.0*Math.PI,yaw/180.0*Math.PI,0);
            v = Util.trans(q,v);
            A3Object avatar = a3canvas.getAvatar();
            if (avatar!=null) {
                v.add(avatar.getLoc());
                a3canvas.setCameraLocNow(v);
                a3canvas.setCameraLookAtPointNow(avatar.getLoc());
            }
            Util.sleep(16);
        }
    }
    public void mousePressed(A3Event e) {
        MouseEvent me = e.getMouseEvent();
        preX = me.getX();
        preY = me.getY();
    }
    public void mouseDragged(A3Event e) {
        MouseEvent me = e.getMouseEvent();
        int x = me.getX();
        int y = me.getY();
        if ((me.getModifiersEx()&MouseEvent.BUTTON1_DOWN_MASK)!=0) {
            yaw = yaw - (x-preX);
            if (yaw>360) yaw = yaw - 360;
            if (yaw<0) yaw = yaw + 360;
            pitch = pitch - (y-preY);
            if (pitch>89) pitch=89;
            if (pitch<-89) pitch=-89;
        }
        if ((me.getModifiersEx()&MouseEvent.BUTTON3_DOWN_MASK)!=0) {
            scale *= Math.pow(0.99,preY-y);
        }
        preX = x;
        preY = y;
    }
    //public void mouseReleased(A3Event e) {;}
}
