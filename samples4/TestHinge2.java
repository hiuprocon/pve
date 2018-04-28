/* ヒンジの使い方2 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//車っぽい物
class MyCar extends PVEObject {
    Box chassis;
    Cylinder wheelR;
    Cylinder wheelL;
    Hinge hingeR;
    Hinge hingeL;

    public MyCar() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        chassis = new Box(Type.DYNAMIC, 5, new Vector3d(1,0.5,2));
        chassis.setInitLocRev(0,0.3,0, 0,0,0);
        wheelR = new Cylinder(Type.DYNAMIC,0.5, 0.1, 0.6);
        wheelR.setInitLocRev(0.55,0.3,-0.5, 0,0,90);
        wheelL = new Cylinder(Type.DYNAMIC,0.5, 0.1, 0.6);
        wheelL.setInitLocRev(-0.55,0.3,-0.5, 0,0,90);
        return new PVEPart[] {chassis,wheelR,wheelL};
    }

    @Override
    protected Constraint[] createConstraints() {
        hingeR = new Hinge(chassis,wheelR,
                           new Vector3d( 0.55,0.3,-0.5),
                           new Vector3d(1,0,0));
        hingeL = new Hinge(chassis,wheelL,
                           new Vector3d(-0.55,0.3,-0.5),
                           new Vector3d(1,0,0));
        return new Constraint[] {hingeR,hingeL};
    }

    @Override
    protected PVEPart getMainPart() {
        return chassis;
    }

    public void move(double d) {
        hingeR.enableAngularMotor(true,d,10);
        hingeL.enableAngularMotor(true,d,10);
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

        //ヒンジ(蝶番(ちょうつがい))を使った車
        MyCar c = new MyCar();
        c.setLocRev(0,0,0, 0,90,0);
        world.add(c);
        for (int i=0;i<100;i++) {
            if (i%2==0)
                c.move(3);
            else
                c.move(-3);
            Util.sleep(3000);
        }
    }
}
