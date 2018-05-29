/* 物体から見た角速度で回転させる */
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

        //物体を生成して。。。
        BoxObj b = new BoxObj();
        b.setLocRev(0,1,0, 90,0,0); //物体をはらばい(?)にする
        world.add(b);
        //1秒おきにへそから前に出る軸(ローカルの
        //Z軸)右ネジ方向，左ネジ方向，右ネジ方向・・・と回転させる
        //その結果ワールド座標のY軸周りに左ネジ方向，右ネジ方向・・・と
        //回転する。わかりずらくて<(_ _)>。
        for (int i=0;i<100;i++) {
            Util.sleep(1000);
            if (i%2==0) {
                b.setAngVelInLocal(0,0,3);
            } else {
                b.setAngVelInLocal(0,0,-3);
            }
        }
    }
}
