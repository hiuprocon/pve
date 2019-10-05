/* ゲームのベースとなるプログラム */
/* 特にゲームの終了後にまたゲームが始められるようにするサンプル */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import java.awt.event.*;
import java.awt.*;

// 画面に2D表示するためのコンポーネント
class MyComponent2D extends Component2D {
    Color c;
    Font f;
    public MyComponent2D() {
        c = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        f = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    }

    @Override
    public void paint(Graphics2D g,A3CanvasInterface canvas) {
        g.setPaint(c);
        g.setFont(f);
        g.drawString("Score: "+Test.score,30,30);
    }
}

class Test extends KeyAdapter implements CollisionListener {
    static int score = 0;
    static boolean spaceKey = false;
    static boolean leftKey = false;
    static PVEObject b1;
    static PVEObject b2;
    static boolean gameEnd = false;

    public static void main(String args[]) {
        Test t = new Test(); //イベントリスナーとなるオブジェクト

        /***** 物理エンジンや付随する基本的な初期化をする部分 *****/
        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW,
                                      PVEWorld.MANUAL_STEP);
        world.resume();
        world.addCollisionListener(t);
        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.addKeyListener(t);
        //メインのシーンとなる1番シーンを準備して
        //物理エンジンはこのシーンを使うように指定
        window.prepareScene(1);
        world.useScene(1);
        //ゲームオーバー用のシーンの準備
        window.prepareScene(2);
        //2D描画用コンポーネント追加
        window.add(new MyComponent2D(),1);

        /***** デフォルトの0番シーンにスタート画面の描画をする *****/
        A3Text3D text = new A3Text3D("Game Start");
        text.setLoc(0,0,-10);
        window.add(text);

        /***** 物理エンジンが使用するメインの1番シーンの準備をする *****/
        //(スタート画面を表示したまま)
        //カメラの設定
        window.setCameraLocNow(0,6,8, 1);
        window.setCameraLookAtPointNow(0,0,0, 1);
        window.setNavigationMode(1, A3CanvasInterface.NaviMode.SIMPLE,10.0);
        //ゲーム内でずっと存在しつづけるようなオブジェクト
        //の生成と世界への追加。(初期位置への移動などゲーム毎に
        //必要な処理は後でやる。
        Ground ground = new Ground();
        world.add(ground);
        b1 = new BoxObj();
        world.add(b1);
        b2 = new BoxObj();
        world.add(b2);

        /***** ゲームが終った時に表示する2番シーンの準備をする *****/
        A3Text3D text2 = new A3Text3D("Game Over");
        text2.setLoc(0,0,-10);
        window.add(text2, 2);
        A3Text3D text3 = new A3Text3D("PRESS SPACE KEY");
        text3.setLoc(0,-3,-20);
        window.add(text3, 2);

        //メインシーンの準備が整ったらスタート画面に
        //"PRESS SPACE KEY"と表示
        A3Text3D text4 = new A3Text3D("PRESS SPACE KEY");
        text4.setLoc(0,-3,-20);
        window.add(text4);
        /***** ここからがゲーム毎のループ *****/
        while (true) {
            /***** ゲーム毎にやらなければなない初期化 *****/
            gameEnd= false;
            b1.setLocRev( 2,2,0, 5,5,5);
            b2.setLocRev(-2,2,0, 5,5,5);

            //スペースキーが押されるまでまつ
            while (spaceKey==false) {
                Util.sleep(10);
            }
            window.changeActiveScene(1);
            //以下がゲームのメインループ
            int counter = 0;
            while (true) {
                world.stepForward();
                counter++;
                if (leftKey==true)
                    b1.setVel(-3,0,0);
                if (gameEnd==true)
                    break; //ゲームオーバーやクリアしたらbreak
                Util.sleep(16);
            }
            //以下1ゲーム終った後の処理
            window.changeActiveScene(2);
            //スペースキーが押されるまでまつ
            while (spaceKey==false) {
                Util.sleep(10);
            }
            window.changeActiveScene(0);
            //ここで待たないとスタート画面もすぐに抜けるから1秒待つ
            Util.sleep(1000);
            //1回分のゲーム終了。ループして最初に戻る
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE) {
            spaceKey = true;
        } else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            leftKey = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE) {
            spaceKey = false;
        } else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            leftKey = false;
        }
    }

    @Override
    public void collided(PVEPart a, PVEPart b) {
        // b1とb2が衝突したらgameEnd
        if ((a.getObject()==b1 && b.getObject()==Test.b2)||
            (a.getObject()==b2 && b.getObject()==b1)) {
            gameEnd = true;
        }
    }
}
