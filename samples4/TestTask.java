/* Tickごとの処理(ゲーム全体) */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.util.ArrayList;

class Test implements Runnable {
    PVEWorld world;
    ArrayList<PVEObject> objects = new ArrayList<PVEObject>();

    public Test() {
        //仮想環境生成
        world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //立方体2個生成して10m上から落下
        BoxObj b1 = new BoxObj();
        b1.setLocRev(-2,1,0, 0,0,0);
        world.add(b1);
        objects.add(b1);
        BoxObj b2 = new BoxObj();
        b2.setLocRev( 2,1,0, 0,0,0);
        world.add(b2);
        objects.add(b2);

        world.addTask(this);
    }

    @Override
    public void run() {
        for (PVEObject o : objects) {
            if (o instanceof BoxObj)
                o.setAngVel(0,1,0);
        }
    }

    public static void main(String args[]) {
        new Test();
    }
}
