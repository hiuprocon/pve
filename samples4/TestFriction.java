/* 摩擦係数の設定 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class Test {
    public static void main(String args[]) {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        ground.setFriction(1.0);
        ground.setLocRev(0,0,0, 30,0,0);
        world.add(ground);

        BoxObj b1 = new BoxObj();
        b1.setFriction(0.5);
        b1.setLocRev(-1,0.6,0, 30,0,0);
        world.add(b1);

        BoxObj b2 = new BoxObj();
        b2.setFriction(1.5);
        b2.setLocRev( 1,0.6,0, 30,0,0);
        world.add(b2);
    }
}
