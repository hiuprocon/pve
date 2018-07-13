/* 2画面で表示する例 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.*; //追加
import javax.swing.*; //追加

class Test {
    public static void main(String args[]) {
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3CANVAS);
        world.resume();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        //表示用のキャンバス1の処理
        A3Canvas canvas1 = (A3Canvas)world.getMainCanvas();
        canvas1.setSize(400,500);
        frame.add(canvas1);
        //表示用のキャンバス2の処理
        A3SubCanvas canvas2 = new A3SubCanvas(400,500);
        canvas1.addA3SubCanvas(canvas2);
        frame.add(canvas2);

        frame.pack();
        frame.setVisible(true);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //車1を生成して動かす
        SimpleCarObj car1 = new SimpleCarObj(world);
        car1.setLocRev(1,1,0, 0,0,0);
        world.add(car1);
        car1.setForce(100,0.5,0,0);

        //車2を生成して動かす
        SimpleCarObj car2 = new SimpleCarObj(world);
        car2.setLocRev(-1,1,0, 0,0,0);
        world.add(car2);
        car2.setForce(100,-0.5,0,0);

        //カメラ追従の処理
        canvas1.setAvatar(car1.getMainA3());
        Vector3d lookAt = new Vector3d(0,0,5);
        Vector3d camera = new Vector3d(0,1,-5);
        Vector3d up = new Vector3d(0,1,0);
        canvas1.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,
                                  lookAt,camera,up,1.0);
        canvas2.setAvatar(car2.getMainA3());
        canvas2.setNavigationMode(A3CanvasInterface.NaviMode.CHASE,
                                  lookAt,camera,up,1.0);
    }
}
