/* 最初のテストプログラム */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

class RadarCube extends PVEObject {
    Box box;
    Radar radar;
    Compound radarCube;

    public RadarCube() {
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        box = new Box(Type.DYNAMIC, 1, new Vector3d(1,1,1));
        box.setInitLocRev(0,0,0, 0,0,0);
        radar = new Radar(Type.DYNAMIC,0.1);
        radar.setInitLocRev(0,0.5,0, 0,0,0);
        radarCube = new Compound(Type.DYNAMIC,box,radar);
        return new PVEPart[] {radarCube};
    }
    @Override
    public Constraint[] createConstraints() {
        return new Constraint[] {};
    }
    @Override
    protected PVEPart getMainPart() {
        return radarCube;
    }
    public double getDistance() {
        return radar.getDistance();
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

        for (int i=0;i<4;i++) {
            double t = i*2*Math.PI/4;
            CylinderObj b = new CylinderObj(Type.DYNAMIC,1,2,1);
            b.setLocRev(2*Math.cos(t),1.0,2*Math.sin(t), 0,0,0);
            world.add(b);
        }

        RadarCube rc = new RadarCube();
        rc.setLocRev(0,10,0, 5,5,5);
        world.add(rc);

        for (;;) {
            rc.setAngVel(0,0.3,0);
            double d = rc.getDistance();
            System.out.printf("%e\n",d);
            Util.sleep(33);
        }
    }
}
