/* BGMを流す */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class Test {
    public static void main(String args[]) throws Exception {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //A3Soundsオブジェクトをwindowに追加してBGMを流す
        //大きなデータを用意するのが嫌だったので
        //BGMというより効果音みたいですが，
        //何か自分でoggやmp3のファイルを用意したなら
        //"x-res:///ファイル名.ogg"という感じで指定
        //すればOKです。詳しくはAcerola3DのA3Soundクラスの
        //APIを参照して下さい。
        A3Sounds s = new A3Sounds();
        s.load("bgm1","x-res:///res/broken_glass.ogg","Background",1,true,
               new Vector3d(),new Vector3d(0,0,1));
        window.add(s);
        s.start("bgm1");

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
    }
}
