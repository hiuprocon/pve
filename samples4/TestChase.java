/* カメラ追従 */
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

        //地面を生成して世界に追加
        Ground ground = new Ground("x-rzip:x-res:///res/stk_racetrack.zip!/racetrack.wrl");
        world.add(ground);

        //車を生成して動かす
        SimpleCarObj car = new SimpleCarObj(world);
        car.setLocRev(0,1,0, 0,0,0);
        world.add(car);
        car.setForce(100,0.5,0,0);

        window.setAvatar(car.getMainA3());
        Vector3d lookAt = new Vector3d(0,0,5);
        Vector3d camera = new Vector3d(0,1,-5);
        Vector3d up = new Vector3d(0,1,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,
                                 lookAt,camera,up,1.0);
    }
}
