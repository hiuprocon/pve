/* スライダーの使い方 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

//スライダーを試すためのオブジェクト
class SliderTester extends PVEObject {
    Box b1;
    Box b2;
    Slider slider;

    public SliderTester() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        /*
         * このメソッドの中でパーツを生成して
         * setInitLocRev()でデフォルトの配置を
         * 決定して下さい．
         */
        b1 = new Box(Type.DYNAMIC, 1, new Vector3d(1,0.1,2));
        b2 = new Box(Type.DYNAMIC, 1, new Vector3d(1,0.1,2));
        b1.setInitLocRev(0,0.05,0, 0,0,0);
        b2.setInitLocRev(0,0.15,0, 0,0,0);
        return new PVEPart[] {b1,b2};
    }

    @Override
    protected Constraint[] createConstraints() {
        /*
         * このメソッドの中でパーツを結び付ける
         * ようなコンストレイントを生成します．
         * コンストレイントの一つであるスライダーの
         * コンストラクタの引数は，結合したい
         * パーツ2つ，スライダーを置く位置ベクトル，
         * スライダーのスライド方向を示すベクトルです．
         */
        slider = new Slider(b1,b2,new Vector3d(0,0.1,0),
                          new Vector3d(0,0,1));
        slider.setLowerLinLimit(-2.0);
        slider.setUpperLinLimit( 2.0);
        slider.setPoweredLinMotor(true);
        slider.setMaxLinMotorForce(10.0);
        return new Constraint[] {slider};
    }

    @Override
    protected PVEPart getMainPart() {
        return b1;
    }

    public void move(double d) {
        slider.setTargetLinMotorVelocity(d);
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

        //スライダーを試す物体を生成します．
        SliderTester s = new SliderTester();
        s.setLocRev(0,0,0, 0,0,0);
        world.add(s);
        for (int i=0;i<100;i++) {
            Util.sleep(3000);
            if (i%2==0)
                s.move(3);
            else
                s.move(-3);
System.out.println("GAHAaaaaaa");
        }
    }
}
