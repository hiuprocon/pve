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
        window.setCameraLocNow(0,60,80);//解説は下
        window.setCameraLookAtPointNow(0,0,0);//解説は下
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,100.0);//解説は下

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
A3WindowクラスはAcerola3Dライブラリのものをそのまま
利用しており，これが持つ機能でカメラに関する色々な
操作が可能です。

setCameraLocNow(x,y,z)でカメラの座標を指定します。
setCameraLookAtPointNow(x,y,z)でカメラの注視点を
設定することで，カメラの向きを指定します。
setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,distance)で
マウスで視点を移動できるモードにします。distanceの所は，
左ドラッグした時に視点がぐるぐる回転しますが，その回転の
中心がカメラからどれだけ離れているかを指定する引数です。
(上のプログラムでは，カメラの注視点とカメラの位置がちょうど
100メートル離れているので，このように設定するのが自然になる。)

詳細はAcerola3DライブラリのA3WindowのAPIを参照して
下さい。
http://acerola3d.osdn.jp/docs/api/jp/sourceforge/acerola3d/a3/A3Window.html
 */
