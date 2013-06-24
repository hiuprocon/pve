package prototype;

import java.awt.Dimension;
import javax.swing.*;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;
import com.github.hiuprocon.pve.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Simulator2GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    Simulator2 simulator;
    A3Canvas canvas;
    A3SubCanvas sc1;
    A3SubCanvas sc2;
    JCheckBox cb1;
    JCheckBox cb2;
    JTextArea textArea;
    Vector3d lookAt = new Vector3d(0.0, 0.0, 6.0);
    Vector3d camera = new Vector3d(0.0, 3.0, -6.0);
    Vector3d up = new Vector3d(0.0, 1.0, 0.0);

    public Simulator2GUI(Simulator2 simulator) {
        super("Simulator2");
        this.simulator = simulator;
        HBox box1 = new HBox();
        VBox box3 = new VBox();
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setPreferredSize(new Dimension(800,150));
        canvas = (A3Canvas) simulator.w.getMainCanvas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        double y = 50;
        double z = 150;
        canvas.setCameraLocImmediately(0, y, z);
        canvas.setCameraLookAtPointImmediately(0, 0, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,
                Math.sqrt(y * y + z * z));
        canvas.setSize(800, 550);

        VBox box2 = new VBox();

        VBox box2_1 = new VBox();
        box2_1.setBorder(new LineBorder(Color.black));
        cb1 = new JCheckBox("deactivate");
        cb1.addActionListener(this);
        box2_1.myAdd(cb1,1);
        sc1 = A3SubCanvas.createA3SubCanvas(200, 150);
        canvas.addA3SubCanvas(sc1);
        box2_1.myAdd(sc1,1);
        box2.myAdd(box2_1, 1);

        VBox box2_2 = new VBox();
        box2_2.setBorder(new LineBorder(Color.black));
        cb2 = new JCheckBox("deactivate");
        cb2.addActionListener(this);
        box2_2.myAdd(cb2,1);
        sc2 = A3SubCanvas.createA3SubCanvas(200, 150);
        canvas.addA3SubCanvas(sc2);
        box2_2.myAdd(sc2, 1);
        box2.myAdd(box2_2, 1);

        box3.myAdd(sp,1);
        box3.myAdd(canvas,1);
        box1.myAdd(box2, 1);
        box1.myAdd(box3, 1);
        add(box1);
        //pack();
        setBounds(500,0,1000,700);
        setVisible(true);

        canvas.setBackground(new A3Background(0.1f, 0.3f, 0.5f));

        //for Java1.7.0_40ea bug of Mac
        if (System.getProperty("os.name").equals("Mac OS X")) {
            try{Thread.sleep(1000);}catch(Exception e){;}
            setBounds(500,0,1010,710);
            setBounds(500,0,1000,700);
        }
    }

    public void setCar1(CarInterface c) {
        sc1.setAvatar(c.getMainA3());
        sc1.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void setCar2(CarInterface c) {
        sc2.setAvatar(c.getMainA3());
        sc2.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void appendText(String s) {
        textArea.append(s+"\n");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource()==cb1) {
            if (cb1.isSelected()==true)
                simulator.deactivateC1();
            else
                simulator.activateC1();
        } else if (ae.getSource()==cb2) {
            if (cb2.isSelected()==true)
                simulator.deactivateC2();
            else
                simulator.activateC2();
        }
    }
}
