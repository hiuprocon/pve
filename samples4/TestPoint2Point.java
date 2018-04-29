/* Point2Pointの使い方 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

// 鎖みないな物を作ります．
class SphereChain extends PVEObject {
    Sphere s1;
    Sphere s2;
    Sphere s3;
    Sphere s4;
    Sphere s5;
    Point2Point pp12;
    Point2Point pp23;
    Point2Point pp34;
    Point2Point pp45;

    public SphereChain() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        /*
         * このメソッドの中でパーツを生成して
         * setInitLocRev()でデフォルトの配置を
         * 決定して下さい．
         */
        s1 = new Sphere(Type.STATIC,  1, 0.1);
        s2 = new Sphere(Type.DYNAMIC, 1, 0.1);
        s3 = new Sphere(Type.DYNAMIC, 1, 0.1);
        s4 = new Sphere(Type.DYNAMIC, 1, 0.1);
        s5 = new Sphere(Type.DYNAMIC, 1, 0.1);
        s1.setInitLocRev(0, 0.0,0, 0,0,0);
        s2.setInitLocRev(0,-0.1,0, 0,0,0);
        s3.setInitLocRev(0,-0.2,0, 0,0,0);
        s4.setInitLocRev(0,-0.3,0, 0,0,0);
        s5.setInitLocRev(0,-0.4,0, 0,0,0);
        return new PVEPart[] {s1,s2,s3,s4,s5};
    }

    @Override
    protected Constraint[] createConstraints() {
        /*
         * このメソッドの中でパーツを結び付ける
         * ようなコンストレイントを生成します．
         * コンストレイントの一つであるPoint2Pointの
         * コンストラクタの引数は，結合したい
         * パーツ2つ，Point2Pointを置く位置ベクトルです．
         */
        pp12 = new Point2Point(s1,s2,new Vector3d(0,-0.05,0));
        pp23 = new Point2Point(s2,s3,new Vector3d(0,-0.15,0));
        pp34 = new Point2Point(s3,s4,new Vector3d(0,-0.25,0));
        pp45 = new Point2Point(s4,s5,new Vector3d(0,-0.35,0));
        return new Constraint[] {pp12,pp23,pp34,pp45};
    }

    @Override
    protected PVEPart getMainPart() {
        return s1;
    }
}

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

        //SphereChainを作って配置
        SphereChain sc = new SphereChain();
        sc.setLocRev(0,0.5,0, 0,0,90);
        world.add(sc);
    }
}
