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

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //1番シーンを作成してアクティブにして
        //Game Startと表示する．(3秒表示したら次に進む．
        window.prepareScene(1);
        window.changeActiveScene(1);
        A3Text3D text = new A3Text3D("Game Start");
        text.setLoc(0,0,-10);
        window.add(text);
        Util.sleep(3000);

        //シーンをデフォルトの0に戻す
        window.changeActiveScene(0);
        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
        Util.sleep(10000);

        //1番シーンに配置されていた文字を
        //Game Overに書き換える．
        text.setString("Game Over");
        //1番シーンに切り替える
        window.changeActiveScene(1);
    }
}
