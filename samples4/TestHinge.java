/* 最初のテストプログラム */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//ヒンジ(蝶番(ちょうつがい))を作ります．
class Tyoutsugai extends PVEObject {
    Box b1;
    Box b2;
    Hinge hinge;

    public Tyoutsugai() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        b1 = new Box(Type.DYNAMIC, 1, new Vector3d(1,0.1,2));
        b1.setInitLocRev( 0.5,0,0, 0,0,0);
        b2 = new Box(Type.DYNAMIC, 1, new Vector3d(1,0.1,2));
        b2.setInitLocRev(-0.5,0,0, 0,0,0);
        return new PVEPart[] {b1,b2};
    }

    @Override
    protected Constraint[] createConstraints() {
        hinge = new Hinge(b1,b2,new Vector3d(0,0,0),
                          new Vector3d(0,0,1));
        return new Constraint[] {hinge};
    }

    @Override
    protected PVEPart getMainPart() {
        return b1;
    }

    public void move(double d) {
        hinge.enableAngularMotor(true,d,10);
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

        //ヒンジ(蝶番(ちょうつがい))を生成して動かします．
        Tyoutsugai t = new Tyoutsugai();
        t.setLocRev(0,1,0, 0,0,0);
        world.add(t);
        for (int i=0;i<100;i++) {
            Util.sleep(3000);
            if (i%2==0)
                t.move(3);
            else
                t.move(-3);
        }
    }
}
