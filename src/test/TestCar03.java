package test;

import test_cb.*;
import com.github.hiuprocon.pve.car.*;
import java.util.*;
import javax.vecmath.*;

public class TestCar03 extends BattleCarBase {
    ArrayList<Vector3d> barricades = new ArrayList<Vector3d>();//障害物座標
    Vector3d loc;//自分の座標
    Vector3d front;//自分の前方方向
    Vector3d left;//自分の左方向
    CarData enemy;//敵の情報
    Vector3d syougai=null;//前方の障害物の座標
    Vector3d enemyDir = new Vector3d(0,0,1);//敵の方向
    Vector3d teitai = new Vector3d(); //停滞しているかどうか？
    Vector3d oldLoc = new Vector3d();//前の自分の座標
    Vector3d enemyOldLoc = new Vector3d();//前の敵の座標

    enum Mode {
        ATTACK, //攻撃モード
        TEITAI  //停滞脱出モード
    }
    Mode mode = Mode.ATTACK;//初期モードはATTACK
    Vector3d v1 = new Vector3d();//途中計算用Vector3d
    int counter=0; //単純カウンタ
    int stableCounter; //一定時間モードを変えないためのカウンタ

    static double[][] b = {//障害物の実際の座標
        {    0,0,   -2},{    2,0,    0},{    0,0,    2},{   -2,0,    0},
        { -8.5,0,  8.5},{ -8.5,0, 11.5},{-11.5,0, 11.5},{-11.5,0,  8.5},
        {  8.5,0, -8.5},{  8.5,0,-11.5},{ 11.5,0,-11.5},{ 11.5,0, -8.5},
        {-17.5,0,  7.5},{-17.5,0, 10.5},{-17.5,0, 13.5},{-17.5,0, 16.5},
        {-26.0,0,  7.5},{-26.0,0, 10.5},{-26.0,0, 13.5},{-26.0,0, 16.5},
        {-20.5,0,  7.5},{-23.5,0,  7.5},{-20.5,0, 16.5},{-23.5,0, 16.5},
        {-14.5,0, 20.0},{-14.5,0, 20.0},{-14.5,0, 20.0},{-14.5,0, 20.0},
        {-14.5,0, 20.0},{-14.5,0, 20.0},{-14.5,0, 20.0},{-14.5,0, 20.0}};

    public TestCar03() {
        for (double[] bb:b)
            barricades.add(new Vector3d(bb[0],bb[1],bb[2]));
    }

    public void exec() {
        counter++;
        stableCounter--;

        stateCheck();
        mode = selectMode();
        //System.out.println("mode:"+mode);

        switch (mode) {
        case ATTACK: calVecToEnemy(); break;
        case TEITAI: calVecForTeitai(); break;
        }

        if (Math.random()<0.03)//3%の確率で発射
            myShoot();

        oldLoc.set(loc);//現在の座標は次回の古い座標
        enemyOldLoc.set(enemy.loc);//敵も
    }

    //状況確認
    void stateCheck() {
        loc = getLoc();
        front = getUnitVecZ();
        left = getUnitVecX();
        enemy = getEnemyData();

        //敵の方角
        enemyDir.sub(enemy.loc,loc);

        //停滞判定
        v1.sub(oldLoc,loc);
        teitai.scale(0.9);
        teitai.add(v1);

        //障害物検知
        syougai = null;
        for (Vector3d b:barricades) {
            v1.sub(b,loc);
            if (v1.length()<enemyDir.length()) {
                v1.normalize();
                if (v1.dot(front)>0.8) {
                    syougai = b;
                }
            }
        }
    }

    Mode selectMode() {
        //stableCounterが正の時はモードを変えない
        if (stableCounter>0)
            return mode;

        //停滞していたら停滞モード
        if (teitai.length()<0.1) {
            stableCounter = 100;
            isBack = !isBack;
            return Mode.TEITAI;
        }

        //それ以外は攻撃
        return Mode.ATTACK;
    }

    //敵をおいかける時の方向計算
    void calVecToEnemy() {
        Vector3d v = new Vector3d(enemyDir);
        v.normalize();
        for (Vector3d vv:barricades) {
            Vector3d bv = new Vector3d(vv);//コピー
            bv.sub(loc);
            double l = bv.length();
            bv.normalize();
            bv.scale(3/l/l);
            v.sub(bv);
        }
        Vector3d bv = new Vector3d(enemy.loc);//コピー
        bv.sub(loc);
        double l = bv.length();
        bv.normalize();
        bv.scale(10/l/l);
        v.sub(bv);
        v.normalize();
        v.scale(3);

        if (v.length()<0.00001)
            return;
        double gEngineForce = 100*v.length();
        v.normalize();
        double gVehicleSteering = 0.5*v.dot(left);
        setForce(gEngineForce,gVehicleSteering,0);
    }

    //停滞脱出
    boolean isBack=false;
    void calVecForTeitai() {
        if (isBack)
            setForce(-300,0.3,0);
        else
            setForce(300,0.3,0);
    }

    //敵の情報を取り出す
    CarData getEnemyData() {
        ArrayList<CarData> cars = getAllCarData();
        if (cars.size()!=2)
            return null;
        CarData cd = cars.get(0);
        if (cd.carID==this.getCarID())
            cd = cars.get(1);
        return cd;
    }

    //なるべく無駄な弾丸は打たない
    //敵の動きを予測してうつ
    void myShoot() {
        if (syougai!=null)
            return;
        Vector3d v = new Vector3d(enemyDir);
        v1.sub(enemy.loc,enemyOldLoc);
        v1.scale(4.0*v.length());
        v.add(v1);
        if (v.length()<0.0001)
            return;
        v.normalize();
        if (front.dot(v)<0.707)//0.707=1/1.41421356
            return;
        shoot(v);
    }
}
