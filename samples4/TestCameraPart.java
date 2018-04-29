/* カメラパーツの使い方 */
import com.github.hiuprocon.pve.core.*;
import com.github.hiuprocon.pve.obj.*;
import jp.sourceforge.acerola3d.a3.*;
import javax.vecmath.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Dimension;

class CameraCube extends PVEObject {
    int W,H;
    Box box;
    Camera camera;
    Compound cameraCube;

    public CameraCube(int w,int h) {
        this.W = w; this.H = h;
        init();
    }

    @Override
    protected PVEPart[] createParts() {
        box = new Box(Type.DYNAMIC, 1, new Vector3d(1,1,1));
        box.setInitLocRev(0,0,0, 0,0,0);
        camera = new Camera(Type.DYNAMIC,0.1,W,H,0.5);
        camera.setInitLocRev(0,0.75,0, 0,0,0);
        cameraCube = new Compound(Type.DYNAMIC,box,camera);
        return new PVEPart[] {cameraCube};
    }
    @Override
    public Constraint[] createConstraints() {
        return new Constraint[] {};
    }
    @Override
    protected PVEPart getMainPart() {
        return cameraCube;
    }
    public void renderOffscreenBuffer(BufferedImage bi) {
        camera.renderOffscreenBuffer(bi);
    }
}

class Test {
    public static void main(String args[]) {
        int W=100;
        int H=100;
        BufferedImage image = new BufferedImage(W,H,BufferedImage.TYPE_INT_RGB);
        JFrame f = new JFrame("camera");
        JPanel p = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(image,0,0,null);
            }
        };
        p.setPreferredSize(new Dimension(W,H));
        f.add(p);
        f.pack();
        f.setVisible(true);

        //仮想環境生成
        PVEWorld world = new PVEWorld(PVEWorld.A3WINDOW);
        world.resume();

        //表示用のウィンドウの処理
        A3Window window = (A3Window)world.getMainCanvas();
        window.setSize(800,500);
        window.setCameraLocNow(0,6,8);
        window.setCameraLookAtPointNow(0,0,0);
        window.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,10.0);

        //地面を生成して世界に追加
        Ground ground = new Ground();
        world.add(ground);

        for (int i=0;i<4;i++) {
            double t = i*2*Math.PI/4;
            CylinderObj b = new CylinderObj(Type.DYNAMIC,1,2,1);
            b.setLocRev(2*Math.cos(t),1.0,2*Math.sin(t), 0,0,0);
            world.add(b);
        }

        CameraCube cc = new CameraCube(W,H);
        cc.setLocRev(0,10,0, 5,5,5);
        world.add(cc);
        Util.sleep(100); //ちょっと待ってからでないとcc.renderOffscreenBuffer(image);のところでNullPointerExceptionが出ます．

        for (;;) {
            cc.setAngVel(0,0.3,0);
            cc.renderOffscreenBuffer(image);
            p.repaint();
            Util.sleep(33);
        }
    }
}
