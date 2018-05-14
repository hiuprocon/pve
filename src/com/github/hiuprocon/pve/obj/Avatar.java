package com.github.hiuprocon.pve.obj;

import com.github.hiuprocon.pve.core.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.Util;
import com.bulletphysics.collision.shapes.*;//CapsuleShape;      
import com.bulletphysics.linearmath.Transform;

//当たり判定はカプセル型
public class Avatar extends PVEObject {
    String a3url;
    double height;
    double width;
    PVEPart myBody;
    double jumpForce = 10.0;
    double velY = 0.0;

    public Avatar(String a3url,double height,double width) {
        this.a3url = a3url;
        this.height = height;
        this.width = width;
        init();
    }

    public Avatar(String a3url) {
        this(a3url,1.65,0.5);
    }

    public void setJumpForce(double jf) {
        this.jumpForce = jf;
    }

    public double getJumpForce() {
        return this.jumpForce;
    }

    @Override
    protected PVEPart[] createParts() {
        CapsuleShape cs1 = new CapsuleShape((float)(width/2.0),(float)(height-width));
        CompoundShape cs2 = new CompoundShape();
        Matrix4f m0 = new Matrix4f();
        m0.setIdentity();
        m0.set(new Vector3f(0,(float)(height/2.0),0));
        Transform t = new Transform(m0);
        cs2.addChildShape(t,cs1);

        myBody = new FreeShapeD(Type.DYNAMIC,50,a3url,cs2);
        myBody.setInitLocRev(0,0,0,  0,0,0);
        myBody.disableDeactivation(true);
        myBody.setDamping(0.1,0.1);
        myBody.setAngularFactor(0);
        return new PVEPart[]{myBody};
    }

    @Override
    protected Constraint[] createConstraints() {
        return new Constraint[]{};
    }

    @Override
    protected PVEPart getMainPart() {
        return myBody;
    }

    public void control(double goForward,
                        double goRight,
                        double turnRight,
                        boolean jump, boolean grounded) {
        //アバターはVELでコントロールすべし．
        //重力の効果が無くなってしまうので，
        //Y方向のVELを重力を考慮して自分で計算すべし．
        //今のところあまり汎用性はなくて二段ジャンプとか
        //やるなら自前でこのメソッドのような物を書く
        //必要がある．
        if (grounded) {
            if (jump) {
                velY = jumpForce;
                grounded = false;
            } else {
                velY = 0.0;
            }
        } else {
            velY -= 1.0;
        }
        Vector3d vel = new Vector3d(goRight,velY,goForward);
        Vector3d angVel = new Vector3d(0,-turnRight,0);
        setVelInLocal(vel);
        setAngVelInLocal(angVel);
    }
}
