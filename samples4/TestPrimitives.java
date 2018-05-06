/* 最初のテストプログラム */
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
        world.add(ground);

        //いろんな形を作ります。
        BoxObj b1 = new BoxObj();
        b1.setLocRev(-1, 0.5, 2,  0,0,0);
        world.add(b1);

        ConeObj b2 = new ConeObj();
        b2.setLocRev( 1, 0.5, 2,  0,0,0);
        world.add(b2);

        CylinderObj b3 = new CylinderObj();
        b3.setLocRev(-2, 0.5,-2,  0,0,0);
        world.add(b3);

        SlopeObj b4 = new SlopeObj();
        b4.setLocRev( 0, 0.5,-2,  0,0,0);
        world.add(b4);

        SphereObj b5 = new SphereObj();
        b5.setLocRev( 2, 0.5,-2,  0,0,0);
        world.add(b5);

        CornerObj b6 = new CornerObj();
        b6.setLocRev( 4, 0.5,-2,  0,0,0);
        world.add(b6);
    }
}
