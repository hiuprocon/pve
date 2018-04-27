/* マウスで物体を動かす2 まだ未完成 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.event.*;//追加！！！！！

class Test implements MouseListener, MouseMotionListener {
    public static void main(String args[]) {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        //window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);
        Test t = new Test(window);
        window.getA3Canvas().addMouseListener(t);
        window.getA3Canvas().addMouseMotionListener(t);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        for (int i=0;i<10;i++) {
            double k = i*2*Math.PI/10;
            BoxObj b = new BoxObj(Type.DYNAMIC,1,1,1,1);
            b.setLocRev(3*Math.cos(k),0.5,3*Math.sin(k), 0,0,0);
            world.add(b);
        }
    }

    A3Window w;
    PVEObject selectedObj = null;
    int nowX,nowY,oldX,oldY;
    double depth;

    Test(A3Window window) {
        this.w = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {;}
    @Override
    public void mouseEntered(MouseEvent e) {;}
    @Override
    public void mouseExited(MouseEvent e) {;}
    @Override
    public void mousePressed(MouseEvent e) {
        //Point3d p = w.canvasToVirtualCS(e.getX(),e.getY(),2.0);
        A3Object a3 = w.pickA3(e.getX(),e.getY());
        if (a3 != null) {
            PVEPart p = (PVEPart)a3.getUserData();
            selectedObj = p.getObject();
            if (selectedObj != null) {
                nowX = e.getX();
                nowY = e.getY();
                Vector3d cameraV = w.getCameraLoc();
                Vector3d a3V = a3.getLoc();
                a3V.sub(cameraV);
                depth = a3V.length();
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        selectedObj = null;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        oldX = nowX; oldY = nowY;
        nowX = e.getX(); nowY = e.getY();
        if (selectedObj!=null) {
            Point3d p = w.canvasToVirtualCS(nowX,nowY,depth);
            selectedObj.setLocRev(p.x,p.y,p.z, 0,0,0);
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {;}
}
/*
まだ未完成
 */
