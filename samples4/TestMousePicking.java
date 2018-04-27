/* 物体のクリックを検知 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class Test extends A3Adapter { //ポイント！！！
    static BoxObj b1;
    static BoxObj b2;

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
        window.addA3Listener(new Test());

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //1つめの立方体を生成ます。
        b1 = new BoxObj();
        b1.setLocRev(-2,0.5,0, 0,0,0);
        world.add(b1);

        //2つめの立方体を生成ます。
        b2 = new BoxObj();
        b2.setLocRev( 2,0.5,0, 0,0,0);
        world.add(b2);
    }

    @Override
    public void mouseClicked(A3Event e) {
        A3Object a3 = e.getA3Object();
        if (a3 != null) {
            PVEPart p = (PVEPart)a3.getUserData();
            PVEObject o = p.getObject();
            if (o==b1)
                System.out.println("1をクリック！");
            else if (o==b2)
                System.out.println("2をクリック！");
        }
    }
}
/*
ほぼAcerola3Dの機能を使って実現されています。

 */
