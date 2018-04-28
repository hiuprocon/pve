/* 反発係数の設定 */
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
        ground.setRestitution(1.0);
        world.add(ground);

        SphereObj s1 = new SphereObj();
        s1.setRestitution(0.8);
        s1.setLocRev(-2,3,0, 0,0,0);
        world.add(s1);

        SphereObj s2 = new SphereObj();
        s2.setRestitution(1.0);
        s2.setLocRev( 0,3,0, 0,0,0);
        world.add(s2);

        SphereObj s3 = new SphereObj();
        s3.setRestitution(1.2);
        s3.setLocRev( 2,3,0, 0,0,0);
        world.add(s3);
    }
}
