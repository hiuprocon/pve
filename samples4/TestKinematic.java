/* 物理法則を無視した動き */
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

        //物体を作る時のタイプをKINEMATICにする
        BoxObj b = new BoxObj(Type.KINEMATIC,1,1,1,1);
        b.setLocRev(0,1,0, 0,0,0);
        world.add(b);
        //1秒おきにワープ
        for (int i=0;i<5;i++) {
            Util.sleep(1000);
            if (i%2==0) {
                b.setLocRev(1,1,0, 0,0,0);
            } else {
                b.setLocRev(-1,1,0, 0,0,0);
            }
        }
        //完全にプログラムでコントロール
        for (int i=0;i<1000;i++) {
            Util.sleep(33);
            b.setLocRev(0,1,0, i,i,i);
        }
    }
}
