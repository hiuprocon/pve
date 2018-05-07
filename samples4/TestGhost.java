/* GHOSTオブジェクトのテストプログラム */
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

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj(Type.GHOST,1,1,1,1,"x-res:///res/ClearBox.wrl");
        //以下の方を有効にすれば本当に見えないGHOSTオブジェクトになる
        //BoxObj b = new BoxObj(Type.GHOST,1,1,1,1,"x-res:///res/Empty.wrl");
        b.setLocRev(0,0.5,0, 0,0,0);
        world.add(b);
    }
}
