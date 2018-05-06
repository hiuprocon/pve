/* 2Dの描画 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.*;

//ProconVEではなくAcerola3Dの機能です．
//Acerola3Dのドキュメントに詳しく記載されてます．
class MyComponent2D extends Component2D {
    Color c1,c2;
    Font f1,f2;
    int s=0;
    public MyComponent2D() {
        c1 = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        c2 = new Color(1.0f, 0.0f, 0.0f, 0.5f);
        f1 = new Font(Font.SANS_SERIF, Font.BOLD, 12);
        f2 = new Font("Arial", Font.PLAIN, 120);
    }
    @Override
    public void paint(Graphics2D g,A3CanvasInterface c) {
        g.setPaint(c1);
        g.setFont(f1);
        g.drawString("Score: "+s,30,30);
        g.setPaint(c2);
        g.setFont(f2);
        g.drawString("2D描画",(int)(100+100*Math.sin(s*0.1)),200);
        s++;
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
        window.add(new MyComponent2D());

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //立方体1個生成して10m上から落下
        BoxObj b = new BoxObj();
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
    }
}
