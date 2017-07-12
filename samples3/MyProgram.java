import etrobo.*;
import jp.etrobo.ev3.balancer.Balancer;

public class MyProgram extends Program {
    public MyProgram() {
        Balancer.init();
    }

    public void exec4ms() {
        float engine = 0.0f;
        float steering = 0.0f;

        if (keyUp) engine += 50.0f;
        if (keyDown) engine -= 50.0f;

        if (keyRight) steering -= 50.0f;
        if (keyLeft)  steering += 50.0f;

        Balancer.control(engine,steering,(float)angleVelocity,
                         0.0f,leftWheelCount,rightWheelCount,5000);
        double pwmR = Balancer.getPwmR();
        double pwmL = Balancer.getPwmL();
        //適当に調整
        pwmR *= 0.02f;
        pwmL *= 0.02f;
        //駆動させる
        drive(pwmR,pwmL,0);
    }
    public void exec4msBAK() {
        float engine = 0.0f;
        float steering = 0.0f;

        if (keyUp) engine += 15.0f;
        if (keyDown) engine -= 15.0f;

        if (keyRight) steering -= 5.0f;
        if (keyLeft)  steering += 5.0f;

        //33.333ms周期を4msにするのに
        //調整．とりあえず適当な調整
        engine*=(1.0*3.0/25.0);
        steering*=(1.0*3.0/25.0);
        //駆動させる
        drive(engine+steering,engine-steering,0);
    }
}
