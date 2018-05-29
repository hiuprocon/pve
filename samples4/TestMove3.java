/* 物体から見た速度で動かす */
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
        Avatar a = new Avatar("x-res:///kappa.a3");
        a.setLocRev(0,1,0, 0,90,0);//左向け左させる
        world.add(a);
        //1秒おきにキャラクターの右手側，左手側，右手側・・・
        //と移動させることで，視点方向では奥，手前，奥・・・
        //と移動しているようになる
        for (int i=0;i<100;i++) {
            Util.sleep(1000);
            if (i%2==0) {
                a.setVelInLocal(3,0,0);
            } else {
                a.setVelInLocal(-3,0,0);
            }
        }
    }
}
