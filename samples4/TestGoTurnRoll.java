/* 物体に命令を出す感じで動かす */
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
        b.setLocRev(0,1,0, 0,0,0);
        world.add(b);
        //1秒おきに前進，左に回転の繰り返しで移動させる
        //使えるメソッドは沢山ある
        //goForward,goBackward,goRight,goLeft,goUp,goDown,
        //turnDown,turnUp,turnRight,turnLeft,rollRight,rollLeft
        b.rollRight(1);
        for (int i=0;i<100;i++) {
            Util.sleep(1000);
            if (i%2==0) {
                b.goForward(1);
            } else {
                b.turnLeft(3);
            }
        }
    }
}
