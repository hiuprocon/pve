/* キーボードで操作できるようにする */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.event.*; //追加！！！！！！！

class Test extends KeyAdapter { //KeyAdapterポイント！！！！！
    static boolean rightKey = false;
    static boolean leftKey = false;

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
        window.addKeyListener(new Test()); //ポイント！！！！！

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //物体を1つ配置
        BoxObj b = new BoxObj();
        b.setLocRev(0,1,0, 0,0,0);
        world.add(b);
        //キーの状態にあわせて動かす
        for (;;) {
            Util.sleep(33);
            if (rightKey==true) {
                b.setVel(1,0,0);
            } else if (leftKey==true) {
                b.setVel(-1,0,0);
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
            rightKey = true;
        } else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            leftKey = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
            rightKey = false;
        } else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            leftKey = false;
        }
    }
}
