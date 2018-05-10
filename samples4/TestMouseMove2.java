/* マウスで物体を動かす2
  マウスで物体をドラッグしようとすると
  物体を弾くことができる． */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.event.*;//追加！！！！！

class Test implements MouseListener {
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

        //地面を生成して世界に追加
        Ground ground = new Ground();
        ground.getMainA3().setPickable(false);//将来の課題
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
    Point3d start;
    Point3d end;
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
        A3Object a3 = w.pickA3(e.getX(),e.getY());
        if (a3 != null) {
            PVEPart p = (PVEPart)a3.getUserData();
            selectedObj = p.getObject();
            if (selectedObj != null) {
                Vector3d cameraV = w.getCameraLoc();
                Vector3d a3V = a3.getLoc();
                a3V.sub(cameraV);
                depth = a3V.length();
                start = w.canvasToVirtualCS(e.getX(),e.getY(),depth);
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedObj!=null) {
            Vector3d cameraV = w.getCameraLoc();
            Vector3d a3V = selectedObj.getMainA3().getLoc();
            a3V.sub(cameraV);
            depth = a3V.length();
            end = w.canvasToVirtualCS(e.getX(),e.getY(),depth);
            end.sub(start);
            end.scale(10);
            selectedObj.setVel(end.x,end.y,end.z);
            selectedObj = null;
        }
    }
}
/*
まだ未完成
 */
