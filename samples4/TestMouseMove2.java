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
        Test t = new Test(world,window);
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

    PVEWorld world;
    A3Window window;
    PVEObject selectedObj = null;
    Point3d start;
    Point3d end;
    double depth;

    Test(PVEWorld world,A3Window window) {
        this.world = world;
        this.window = window;
    }

    @Override
    public void mouseClicked(MouseEvent e) {;}
    @Override
    public void mouseEntered(MouseEvent e) {;}
    @Override
    public void mouseExited(MouseEvent e) {;}
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        PVEPart p = world.pick(x,y);
        if (p != null) {
            PVEObject o = p.getObject();
            if (o instanceof BoxObj) {
                Vector3d cameraV = window.getCameraLoc();
                Vector3d objV = o.getLoc();
                objV.sub(cameraV);
                depth = objV.length();
                start = window.canvasToVirtualCS(x,y,depth);
                selectedObj = o;
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedObj!=null) {
            Vector3d cameraV = window.getCameraLoc();
            Vector3d objV = selectedObj.getLoc();
            objV.sub(cameraV);
            depth = objV.length();
            end = window.canvasToVirtualCS(e.getX(),e.getY(),depth);
            end.sub(start);
            end.scale(10);
            selectedObj.setVel(end.x,end.y,end.z);
            selectedObj = null;
        }
    }
}
