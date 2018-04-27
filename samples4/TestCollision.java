/* 当たり判定をする */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class Test implements CollisionListener { //このimplementsポイント！
    static BoxObj b1;
    static BoxObj b2;

    public static void main(String args[]) {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();
        world.addCollisionListener(new Test()); //重要！！！

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //物体を1つ配置
        b1 = new BoxObj();
        b1.setLocRev(0,1,0, 0,0,0);
        world.add(b1);
        //もう1つ配置
        b2 = new BoxObj();
        b2.setLocRev(0,10,0, 0,0,0);
        world.add(b2);
    }

    //以下が衝突を検知して処理しているところ
    @Override
    public void collided(PVEPart a,PVEPart b) {
        if (a.getObject()==b1 && b.getObject()==b2)
            System.out.println("ぶつかた！(1,2)");
        if (a.getObject()==b2 && b.getObject()==b1)
            System.out.println("ぶつかた！(2,1)");
    }
}
