package prototype;

public class CarController3 extends CarController {
    private static final long serialVersionUID = 1L;
    public CarController3(String name,int port,int x,int y,int w,int h) {
        super(name,port,x,y,w,h);
        engineF = 500;
        steeringF = -0.3;
    }
}
