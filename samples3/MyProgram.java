import etrobo.*;
import jp.etrobo.ev3.balancer.Balancer;

//ロボットを制御するプログラムはこのようにして
//書きます．
public class MyProgram extends Program {
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
