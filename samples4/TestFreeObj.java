/* 自由な形を作る */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;

/*
自由な形を作るためのPVEObjectは
FreeObjA, FreeObjB, FreeObjC, FreeObjDの
4種類あります。

* FreeObjA: は本当に自由な形が作れる。ただ，
  衝突判定のコストが高い。
* FreeObjB: 指定された形を取り囲む最小の凸包。
  穴のあいてる形や，くぼみのある形は作れないが
  衝突判定が低コストでできるのでお勧め。
* FreeObjC: 生成後に形が変えられるjbulletの
  GImpactMeshShapeで実装されているのだが，
  形を変えるAPIが無いのであまり意味ないけど，
  FreeObjAやFreeObjBで上手くいかないとき使って
  みると上手くいくことがたまにある。
*/

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

        //gouguiを1個生成して10m上から落下
        FreeObjB b = new FreeObjB(Type.DYNAMIC,1.0,"x-res:///res/prototype/gougui.a3");
        b.setLocRev(0,10,0, 5,5,5);
        world.add(b);
    }
}
