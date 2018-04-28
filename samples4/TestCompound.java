/* 独自オブジェクトの生成 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//ダンベルみたいな形を作ってみます．
class Dumbbells extends PVEObject {
    Compound dumbbells;
    Sphere sphereR;
    Sphere sphereL;
    Cylinder cylinder;

    public Dumbbells() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        sphereR = new Sphere(Type.DYNAMIC, 1, 1);
        sphereR.setInitLocRev( 1,0,0, 0,0,0);
        sphereL = new Sphere(Type.DYNAMIC, 1, 1);
        sphereL.setInitLocRev(-1,0,0, 0,0,0);
        cylinder = new Cylinder(Type.DYNAMIC,1, 2,0.3);
        cylinder.setInitLocRev(0,0,0, 0,0,90);
        dumbbells = new Compound(Type.DYNAMIC,
                                 sphereR,sphereL,cylinder);
        return new PVEPart[] {dumbbells};
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[0];
    }

    @Override
    protected PVEPart getMainPart() {
        return dumbbells;
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
