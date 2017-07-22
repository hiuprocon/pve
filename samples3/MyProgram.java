import etrobo.*;
import jp.etrobo.ev3.balancer.Balancer;

public class MyProgram extends Program {
    public MyProgram() {
        Balancer.init();
    }

    public void exec4ms() {
        float engine = 0.0f;
        float steering = 0.0f;

        if (keyUp) engine += 40.0f;
        if (keyDown) engine -= 40.0f;

        if (keyRight) steering -= 40.0f;
        if (keyLeft)  steering += 40.0f;

        Balancer.control(engine,steering,(float)angleVelocity,
                         0.0f,leftWheelCount,rightWheelCount,5000);
        double pwmR = Balancer.getPwmR();
        double pwmL = Balancer.getPwmL();
        double pwmT = 0.0;//-1.0*tailCount;
        //駆動させる
        drive(pwmR,pwmL,0);
    }
}
