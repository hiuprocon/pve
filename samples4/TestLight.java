/* 光源のコントロール */
/* デフォルトでカメラに光源が付いているので
   物が表示されます．ですが，そのカメラの
   光源をOFFにして，他の光源を付け加える
   ことも可能です． */
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
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        //立方体1個生成
        BoxObj b = new BoxObj();
        b.setLocRev(0,0.5,0, 0,0,0);
        world.add(b);

        //DirectionalLightSet.a3というファイルの中に
        //入っている平行光線の光源(Light)を生成して表示させます．
        //他にPointLightSet.a3とSpotLightSet.a3もあります．
        //他の種類のLightを作ったりAcerola3Dのページから
        //ダウンロードすることもできます．
        //http://acerola3d.osdn.jp/samples/a3/
        //DirectionalLightsSet.a3には0.0から1.0までの
        //色々な強さの光源が入っていてlights.change("dl0.5");
        //のようにして光源から一つ選ぶようになってます．
        Action3D lights = new Action3D("x-res:///res/DirectionalLightSet.a3");
        lights.change("0.0");
        lights.setLoc(0,10,5);
        lights.setRev(0,0,-30);
        window.add(lights);

        while (true) {
            //window.setHeadLightEnable(boolean)で
            //カメラについてるライトをつけたり
            //消したりできます．
            window.setHeadLightEnable(true);
            Util.sleep(1000);
            window.setHeadLightEnable(false);
            Util.sleep(1000);
            for (int i=0;i<=10;i++) {
                String s = String.format("%3.1f",0.1*i);
                System.out.println(s);
                lights.change(s);
                Util.sleep(100);
            }
        }
    }
}
