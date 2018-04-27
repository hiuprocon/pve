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

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
    }
}
/*
わからないところは，そのまま真似しましょう。
worldがコンピュータの中の仮想物理世界です。
worldの中にwindowが入っているので取り出して，
色々調整します。次に地面を追加して，立方体を
少し高い所に追加して落してます。

下の方にある「b.setLocRev(0,10,0, 5,5,5);」の
ところは物体の位置と回転を指定するプログラムで
特に重要です。括弧の中に数字が6つならんで
いますが，順番にX座標，Y座標，Z座標，
X軸周りの回転，Y軸周りの回転，Z軸周りの回転です。
(面白みのために5度ほど回転させてます。)
 */
