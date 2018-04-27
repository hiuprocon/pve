/* ETロボコンのシミュレーション環境。参考までに。*/
import etrobo.*;
import jp.etrobo.ev3.balancer.Balancer;

class Test {
    public static void main(String[] args) {
        //シミュレータ環境を生成
        SimEnv env = new SimEnv();

        //ステージを生成して環境内に配置
        Stage10 s = new Stage10();
        s.setLocRev(0,0,0, 0,0,0);
        env.add(s);

        //ロボットのプログラムを生成
        MyProgram prog = new MyProgram();
        //ロボットそのものを生成して環境に配置
        ETRobo robo = new ETRobo(prog);
        robo.setLocRev(0,0.1,0, 0,180,0);
        env.add(robo);

        //階段の難所を生成して配置
        Kaidan10 k = new Kaidan10();
        k.setLocRev(5,0,0, 0,0,0);
        env.add(k);

        //ルックアップゲートの難所を生成して配置
        LookUpGate10 l = new LookUpGate10();
        l.setLocRev(-5,0,0, 0,0,0);
        env.add(l);

        //ガレージの難所(1)を生成して配置
        Garage10 g1 = new Garage10();
        g1.setLocRev(0,0.25,5, 0,0,0);
        env.add(g1);

        //ガレージの難所(2)を生成して配置
        Garage10 g2 = new Garage10();
        g2.setLocRev(0,0.25,-5, 0,0,0);
        env.add(g2);

        //シミュレーションを実行．
        env.loop();

        //env.loop();のかわりにシミュレーションの
        //実行を自前でコントロールしたい時は
        //以下のようにすればOK．
        //while (true) {
        //    env.stepForward();
        //    try{Thread.sleep(4);}catch(Exception e){;}
        //}
    }
}

//ロボットを制御するプログラムはこのようにして
//書きます．
class MyProgram extends Program {
    /*
    etrobo.Programをextendsして作るわけだけど，
    このクラスにインスタンス変数でprotectedな物に，
    色々センサーの値が自動でセットされるように
    なっている．どんな変数が使えるかメモしておく．
    (2017,09/03現在)

    //カラーセンサーの光度
    protected double brightness;//0-100
    //超音波センサー
    protected double distance;//センチメートル
    //モーターのエンエンコーダーの値
    protected int rightWheelCount;//deg
    protected int leftWheelCount;//deg
    protected int tailCount;//deg
    //加速度センサーの値
    protected double angleVelocity;//deg/sec

    //以下，シミュレーターだけのセンサー?
    //キーボードとう投下状態
    protected boolean keyUp = false;
    protected boolean keyDown = false;
    protected boolean keyRight = false;
    protected boolean keyLeft = false;
    protected boolean keyZ = false;
    */

    public MyProgram() {
        //倒立振子APIは公開されている物をそのまま使わせて
        //もらう．
        Balancer.init();
    }

    public void exec4ms() {
        //前進のパワーと旋回のパワーを
        //計算するための変数を用意
        //どっちも-100から100まで(？)
        float engine = 0.0f;
        float steering = 0.0f;

        //キーボードに状態でコントロールできるようにする
        if (keyUp) engine += 40.0f;
        if (keyDown) engine -= 40.0f;
        if (keyRight) steering -= 40.0f;
        if (keyLeft)  steering += 40.0f;

        //倒立振子APIに情報を与えてモーターのPWM値を
        //計算させる．
        Balancer.control(engine,steering,(float)angleVelocity,
                         0.0f,leftWheelCount,rightWheelCount,5000);
        double pwmR = Balancer.getPwmR();
        double pwmL = Balancer.getPwmL();
        double pwmT = 0.0;//-1.0*tailCount;

        //計算したPWM値でモーターを駆動させる
        drive(pwmR,pwmL,0);
    }
}
