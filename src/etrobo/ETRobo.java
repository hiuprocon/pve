package etrobo;

import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.github.hiuprocon.pve.core.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ETRobo extends PVEObject {
    SimEnv env;
    PVEPart myBody;
    Box box;
    Cylinder tireR;
    Cylinder tireL;
    Camera camera;
    Hinge hingeR;
    Hinge hingeL;
    Hinge hingeT;
    Fix fix;
    Fix fix2;
    Radar radar;
    Box tail;
    //Slider fix;
    private double preAngle; //boxのx軸周りの角度(deg)？
    JPanel p;
    BufferedImage image;
    int W=30;
    int H=30;
    Program prog;

    public ETRobo(Program prog) {
        this.prog = prog;
        image = new BufferedImage(W,H,BufferedImage.TYPE_INT_RGB);
        JFrame f = new JFrame("camera");
        p = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(image,0,0,null);
            }
        };
        p.setPreferredSize(new Dimension(W,H));
        f.add(p);
        f.pack();
        f.setVisible(true);
        init();
        drive(0,0,0);
    }

    void setSimEnv(SimEnv env) {
        this.env = env;
        this.prog.setSimEnv(env);
    }

    @Override
    protected PVEPart[] createParts() {
        box = new Box(Type.DYNAMIC,2,new Vector3d(1,2,0.5),"x-res:///res/Box.wrl");
        box.setInitLocRev(0,0,0, 0,0,0);
        camera = new Camera(Type.DYNAMIC,0.01,W,H,0.1);
        camera.setInitLocRev(0,-1.05,0, -90,0,0);
        radar = new Radar(Type.DYNAMIC,0.01);
        radar.setInitLocRev(0,1.05,0, 0,0,0);
        myBody = new Compound(Type.DYNAMIC,box,camera,radar);
        myBody.setInitLocRev(0.0, 1.5,   0, 0,0, 0);
        tireR = new Cylinder(Type.DYNAMIC,0.1,0.1,1);
        tireR.setInitLocRev( 0.65,0.5,0.0, 0,0,90);
        tireL = new Cylinder(Type.DYNAMIC,0.1,0.1,1);
        tireL.setInitLocRev(-0.65,0.5,0.0, 0,0,90);
        tail = new Box(Type.DYNAMIC,0.1,new Vector3d(0.1,0.7,0.1),"x-res:///res/Box.wrl");
        tail.setInitLocRev(0.0,0.55,-0.3, 45,0,0);
        myBody.disableDeactivation(true);
        tireR.disableDeactivation(true);
        tireL.disableDeactivation(true);
        tireR.setFriction(20);
        tireL.setFriction(20);
        //myBody.setDamping(0.0,0.0);
        //myBody.setAngularFactor(0);
        return new PVEPart[]{myBody,tireR,tireL,tail};
    }

    @Override
    protected Constraint[] createConstraints() {
        hingeR = new Hinge(myBody,tireR,new Vector3d( 0.55,0.5,0),new Vector3d( 1,0,0));
        hingeL = new Hinge(myBody,tireL,new Vector3d(-0.55,0.5,0),new Vector3d( 1,0,0));
        //fix = new Fix(box,camera,new Vector3d(0.0,0.5,0.0));
        //fix = new Slider(box,camera,new Vector3d(0.0,0.5,0.0),new Vector3d(0,-1,0));fix.setLowerLinLimit(0);fix.setUpperLinLimit(0.00001);
        //fix = new Hinge(box,camera,new Vector3d(0.0,0.5,0.0),new Vector3d(0,-1,0));fix.setLimit(-0.001,0.001);
        hingeT = new Hinge(myBody,tail,new Vector3d(0,0.7,-0.3),new Vector3d(1,0,0));
        hingeR.disableCollisionsBetweenLinkedBodies = true;
        hingeL.disableCollisionsBetweenLinkedBodies = true;
        hingeT.disableCollisionsBetweenLinkedBodies = true;
        //fix.disableCollisionsBetweenLinkedBodies = true;
        //return new Constraint[]{hingeR,hingeL,fix};
        return new Constraint[]{hingeR,hingeL,hingeT};
    }

    @Override
    protected PVEPart getMainPart() {
        return myBody;
        //return box;
    }

    public void drive(double right,double left,double tail) {
        hingeR.enableAngularMotor(true,right,10.0);
        hingeL.enableAngularMotor(true,left,10.0);
        hingeT.enableAngularMotor(true,tail,10.0);
    }

    @Override
    protected void postSimulation() {
        //カラーセンサー
        camera.renderOffscreenBuffer(image);
        p.repaint();
        int sum=0;
        for (int x=0;x<W;x++) {
            for (int y=0;y<H;y++) {
                int c = image.getRGB(x,y);
                int[] rgb = new int[3];
                rgb[0] = c&255;
                rgb[1] = (c&(255<<8))/256;
                rgb[2] = (c&(255<<16))/256/256;;
                sum += rgb[0] + rgb[1] + rgb[2];
            }
        }
        prog.brightness = 100.0 * sum / (W*H*255.0*3.0);
        //超音波センサー
        //環境が実際の10倍で作ってあって，もともと
        //メートル単位のやつをセンチメートル単位にするので．．．
        prog.distance = 0.1*100.0*radar.getDistance();
        //モーターのエンコーダー
        prog.rightWheelCount = (int)(180.0*hingeR.getHingeAngle()/Math.PI);
        prog.leftWheelCount = (int)(180.0*hingeL.getHingeAngle()/Math.PI);
        prog.tailCount = (int)(180.0*hingeT.getHingeAngle()/Math.PI);
        //ジャイロの角速度
        double angle = box.getRev().x;
        prog.angleVelocity = (angle-preAngle)/0.004;//4ms周期として
        preAngle = angle;
        if (prog!=null) {
            prog.exec4ms();
        } else {
            System.out.println("GAHA2!");
        }
    }
}
