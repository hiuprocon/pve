/* アバターの生成 */
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
        //壁を生成
        BoxObj b1 = new BoxObj(Type.STATIC,0,6,1,0.1);
        BoxObj b2 = new BoxObj(Type.STATIC,0,6,1,0.1);
        BoxObj b3 = new BoxObj(Type.STATIC,0,6,1,0.1);
        BoxObj b4 = new BoxObj(Type.STATIC,0,6,1,0.1);
        b1.setLocRev( 0,0.5,-3,  0, 0,0);
        b2.setLocRev( 0,0.5, 3,  0, 0,0);
        b3.setLocRev(-3,0.5, 0,  0,90,0);
        b4.setLocRev( 3,0.5, 0,  0,90,0);
        world.add(b1);
        world.add(b2);
        world.add(b3);
        world.add(b4);

        //アバター
        for (int i=-4;i<=4;i++) {
            for (int j=-4;j<=4;j++) {
                Avatar a = new Avatar("x-res:///kappa.a3");
                a.setLocRev(0.5*i,0.5,0.5*j, 0,0,0);
                world.add(a);
            }
        }
        Avatar a0 = new Avatar("x-res:///kappa.a3");
        a0.setLocRev(1.0,1.5,0, 0,0,0);
        world.add(a0);
        Util.sleep(3000);
        while (true) {
            Util.sleep(33);
            a0.setForce(10,1);
        }
    }
}
