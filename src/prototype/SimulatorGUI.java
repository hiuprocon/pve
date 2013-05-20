package prototype;

import java.awt.Dimension;
import javax.swing.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;
import com.github.hiuprocon.pve.ui.*;

public class SimulatorGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    A3Canvas canvas;
    A3SubCanvas sc1;
    A3SubCanvas sc2;
    A3SubCanvas sc3;
    JTextArea textArea;
    Vector3d lookAt = new Vector3d(0.0, 0.0, 6.0);
    Vector3d camera = new Vector3d(0.0, 3.0, -6.0);
    Vector3d up = new Vector3d(0.0, 1.0, 0.0);

    public SimulatorGUI(A3CanvasInterface c) {
        super("Simulator");
        HBox box1 = new HBox();
        VBox box3 = new VBox();
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setPreferredSize(new Dimension(800,150));
        canvas = (A3Canvas) c;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        double y = 50;
        double z = 150;
        canvas.setCameraLocImmediately(0, y, z);
        canvas.setCameraLookAtPointImmediately(0, 0, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,
                Math.sqrt(y * y + z * z));
        canvas.setSize(800, 550);

        VBox box2 = new VBox();

        sc1 = A3SubCanvas.createA3SubCanvas(200, 150);
        canvas.addA3SubCanvas(sc1);
        box2.myAdd(sc1, 1);

        sc2 = A3SubCanvas.createA3SubCanvas(200, 150);
        canvas.addA3SubCanvas(sc2);
        box2.myAdd(sc2, 1);

        sc3 = A3SubCanvas.createA3SubCanvas(200, 150);
        canvas.addA3SubCanvas(sc3);
        box2.myAdd(sc3, 1);

        box3.myAdd(sp,1);
        box3.myAdd(canvas,1);
        box1.myAdd(box2, 1);
        box1.myAdd(box3, 1);
        add(box1);
        //pack();
        setBounds(500,0,1000,700);
        setVisible(true);

        canvas.setBackground(new A3Background(0.1f, 0.3f, 0.5f));
    }

    public void setCarA1(CarA c) {
        sc1.setAvatar(c.getMainA3());
        sc1.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void setCarA2(CarA c) {
        sc2.setAvatar(c.getMainA3());
        sc2.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void setCarB(CarB c) {
        sc3.setAvatar(c.getMainA3());
        sc3.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void setCarC1(CarC c) {
        sc1.setAvatar(c.getMainA3());
        sc1.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void appendText(String s) {
        textArea.append(s+"\n");
    }
}
