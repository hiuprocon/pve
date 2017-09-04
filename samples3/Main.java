import etrobo.*;

/*
  ステージを用意してシミュレーションを実行する
  プログラム．
 */
public class Main {
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
