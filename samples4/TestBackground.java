/* 背景を設定する */
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
        //ProconVEにはじめから入ってるデータでやるために
        //ファイルの指定が長くなってますが，
        //何か自分でVRML2.0のファイルを用意したなら
        //"x-res:///ファイル名.wrl"という感じで指定
        //すればOKです。
        Ground ground = new Ground("x-rzip:x-res:///res/stk_racetrack.zip!/racetrack.wrl");
        world.add(ground);

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
    }
}
