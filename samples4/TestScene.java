/* シーンを利用する */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class Test {
    public static void main(String args[]) {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの準備
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);

        //1番シーンを作成して物理エンジンは
        //このシーンを使うように指定
        window.prepareScene(1);
        world.useScene(1);
        //シーン1のカメラなどを設定
        window.setCameraLocNow(0,6,8, 1);
        window.setCameraLookAtPointNow(0,0,0, 1);
        window.setNavigationMode(1, A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //デフォルトの0番シーンにGame Startと表示する．
        A3Text3D text = new A3Text3D("Game Start");
        text.setLoc(0,0,-10);
        window.add(text);

        //1番シーンで地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //3秒表示したら次に進む．
        Util.sleep(3000);

        //シーンを物理エンジンが動作する1に切り替える
        window.changeActiveScene(1);

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
        Util.sleep(5000);

        //0番シーンに配置されている文字を
        //Game Overに書き換える．
        text.setString("Game Over");
        //0番シーンに切り替える
        window.changeActiveScene(0);
    }
}
