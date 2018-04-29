/* Fixコンストレイント */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//ダンベルみたいな形を作ってみます．
class Dumbbells extends PVEObject {
    Sphere sphereR;
    Sphere sphereL;
    Cylinder cylinder;
    Fix fixR;
    Fix fixL;

    public Dumbbells() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        /*
         * このメソッドの中でパーツを生成して
         * setInitLocRev()でデフォルトの配置を
         * 決定して下さい．
         */
        sphereR = new Sphere(Type.DYNAMIC, 1, 1);
        sphereL = new Sphere(Type.DYNAMIC, 1, 1);
        cylinder = new Cylinder(Type.DYNAMIC,1, 1,0.3);
        sphereR.setInitLocRev( 1,0,0, 0,0,0);
        sphereL.setInitLocRev(-1,0,0, 0,0,0);
        cylinder.setInitLocRev(0,0,0, 0,0,90);
        return new PVEPart[] {sphereR,sphereL,cylinder};
    }

    @Override
    protected Constraint[] createConstraints() {
        /*
         * このメソッドの中でパーツを結び付ける
         * ようなコンストレイントを生成します．
         * コンストレイントの一つであるFixの
         * コンストラクタの引数は，結合したい
         * パーツ2つです．
         */
        fixR = new Fix(sphereR,cylinder);
        fixL = new Fix(sphereL,cylinder);
        return new Constraint[] {fixR,fixL};
    }

    @Override
    protected PVEPart getMainPart() {
        return cylinder;
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

        //ダンベル
        Dumbbells d = new Dumbbells();
        d.setLocRev(0,5,0, 0,0,30);
        world.add(d);
    }
}
