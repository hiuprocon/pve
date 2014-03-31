package prototype;

import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.*;
import jp.sourceforge.acerola3d.a3.*;
import com.github.hiuprocon.pve.ui.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Position;

public class Simulator3GUI extends JFrame implements ActionListener, ChangeListener {
    private static final long serialVersionUID = 1L;
    Simulator3 simulator;
    CarInterface c1;
    CarInterface c2;
    A3Canvas canvas;
    JCheckBox oneCarCB;
    JButton resetWorldB;
    JCheckBox pauseCB;
    JSlider waitTimeS;
    JLabel timeL;
    JButton defaultViewB;
    JButton topViewB;
    JButton frontViewB;
    JButton sideViewB;
    JButton redViewB;
    JButton blueViewB;
    JCheckBox parallelCB;
    JCheckBox polygonizeCB;
    JButton snapshotB;
    JTextField seedTF;
    JTextArea textArea;
    Vector3d lookAt = new Vector3d(0.0, 0.0, 6.0);
    Vector3d camera = new Vector3d(0.0, 3.0, -6.0);
    Vector3d up = new Vector3d(0.0, 1.0, 0.0);

    public Simulator3GUI(Simulator3 simulator) {
        super("Simulator3");
        this.simulator = simulator;
        HBox box1 = new HBox();
        VBox box3 = new VBox();
        textArea = new JTextArea(20,40);
        textArea.setEditable(false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setMinimumSize(new Dimension(600,130));
        sp.setPreferredSize(new Dimension(600,130));
        box3.myAdd(sp, 0);
        canvas = (A3Canvas) simulator.w.getMainCanvas();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        defaultView();
        canvas.setPreferredSize(new Dimension(700, 650));
        canvas.setCanvasWidthInPWorld(2.0);
        canvas.addA3Listener(simulator);
        box3.myAdd(canvas, 1);

        VBox box2 = new VBox();
        box2.setPreferredSize(new Dimension(500,600));

        VBox controlBox = new VBox();
        box2.myAdd(controlBox, 0);
        oneCarCB = new JCheckBox("only one car");
        oneCarCB.addActionListener(this);
        controlBox.myAdd(oneCarCB,1);
        resetWorldB = new JButton("Reset World");
        resetWorldB.addActionListener(this);
        controlBox.myAdd(resetWorldB, 1);
        HBox timeBox = new HBox();
        timeBox.myAdd(new JLabel("pause"),0);
        pauseCB = new JCheckBox();
        pauseCB.addActionListener(this);
        timeBox.myAdd(pauseCB,0);
        timeBox.myAdd(new JLabel("wait time"),0);
        waitTimeS = new JSlider(0,100,51);
        waitTimeS.addChangeListener(this);
        timeBox.myAdd(waitTimeS,1);
        controlBox.myAdd(timeBox,1);
        timeL = new JLabel("");
        updateTime(0);
        controlBox.myAdd(timeL,1);
        defaultViewB = new JButton("Default View");
        defaultViewB.addActionListener(this);
        controlBox.myAdd(defaultViewB, 1);
        topViewB = new JButton("Top View");
        topViewB.addActionListener(this);
        controlBox.myAdd(topViewB, 1);
        frontViewB = new JButton("Front View");
        frontViewB.addActionListener(this);
        controlBox.myAdd(frontViewB, 1);
        sideViewB = new JButton("Side View");
        sideViewB.addActionListener(this);
        controlBox.myAdd(sideViewB, 1);
        redViewB = new JButton("Red Car View");
        redViewB.addActionListener(this);
        controlBox.myAdd(redViewB, 1);
        blueViewB = new JButton("Blue Car View");
        blueViewB.addActionListener(this);
        controlBox.myAdd(blueViewB, 1);
        parallelCB = new JCheckBox("parallel");
        parallelCB.addActionListener(this);
        controlBox.myAdd(parallelCB, 1);
        polygonizeCB = new JCheckBox("polygonize");
        polygonizeCB.addActionListener(this);
        controlBox.myAdd(polygonizeCB, 1);
        HBox seedBox = new HBox();
        seedBox.myAdd(new JLabel("seed of random number"), 0);
        seedTF = new JTextField("");
        seedBox.myAdd(seedTF, 1);
        controlBox.myAdd(seedBox, 1);
        snapshotB = new JButton("snapshot");
        snapshotB.addActionListener(this);
        controlBox.myAdd(snapshotB, 1);

        box1.myAdd(box2, 1);
        box1.myAdd(box3, 1);
        add(box1);
        //pack();
        setBounds(500,0,1000,800);
        setVisible(true);

        //for Java1.7.0_40ea bug of Mac
        if (System.getProperty("os.name").equals("Mac OS X")) {
            try{Thread.sleep(1000);}catch(Exception e){;}
            setBounds(500,0,1010,810);
            setBounds(500,0,1000,800);
        }
    }

    void defaultView() {
        double y = 50;
        double z = 150;
        canvas.setCameraLocImmediately(0, y, z);
        canvas.setCameraLookAtPointImmediately(0, 0, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,
                Math.sqrt(y * y + z * z));
    }

    void topView() {
        canvas.setCameraLocImmediately(0,100,0);
        canvas.setCameraRevImmediately(-90, 0, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,100.0);
    }

    void frontView() {
        canvas.setCameraLocImmediately(0,0,100);
        canvas.setCameraRevImmediately(0, 0, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,100.0);
    }

    void sideView() {
        canvas.setCameraLocImmediately(100,0,0);
        canvas.setCameraRevImmediately(0, 90, 0);
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.SIMPLE,100.0);
    }

    void redView() {
        canvas.setAvatar(c1.getMainA3());
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    void blueView() {
        canvas.setAvatar(c2.getMainA3());
        canvas.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 10.0);
    }

    public void setCar1(CarInterface c) {
        c1 = c;
    }

    public void setCar2(CarInterface c) {
        c2 = c;
    }

    public void appendText(String s) {
        textArea.append(s+"\n");
        Document d = textArea.getDocument();
        Position p = d.getEndPosition();
        Caret c = textArea.getCaret();
        c.setDot(p.getOffset());
    }

    public void updateTime(double t) {
        timeL.setText(String.format("time: %9.2f",t));
    }
    public String getSeed() {
        return seedTF.getText();
    }
    public void setSeed(long l) {
        seedTF.setText(""+l);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source==oneCarCB) {
            if (oneCarCB.isSelected()==true)
                simulator.deactivateOneCar();
            else
                simulator.activateTwoCars();
        } else if (source==resetWorldB) {
            try{simulator.initWorld();}catch(Exception e){;}
            oneCarCB.setSelected(false);
            waitTimeS.setValue(51);
            updateTime(0);
            textArea.setText("");
            parallelCB.setSelected(false);
            polygonizeCB.setSelected(false);
            canvas.setProjectionMode(ProjectionMode.PERSPECTIVE);
            simulator.w.unpolygonize();
        } else if (source==pauseCB) {
            if (pauseCB.isSelected())
                simulator.pause();
            else
                simulator.resume();
        } else if (source==defaultViewB) {
            defaultView();
        } else if (source==topViewB) {
            topView();
        } else if (source==frontViewB) {
            frontView();
        } else if (source==sideViewB) {
            sideView();
        } else if (source==redViewB) {
            redView();
        } else if (source==blueViewB) {
            blueView();
        } else if (source==parallelCB) {
            if (parallelCB.isSelected()==true) 
                canvas.setProjectionMode(ProjectionMode.PARALLEL);
            else
                canvas.setProjectionMode(ProjectionMode.PERSPECTIVE);
        } else if (source==polygonizeCB) {
            if (polygonizeCB.isSelected()==true) {
                simulator.w.polygonize();
            } else
                simulator.w.unpolygonize();
        } else if (source==snapshotB) {
            BufferedImage bi = canvas.snapshot();
            JFileChooser fc = new JFileChooser();
            int selected = fc.showSaveDialog(canvas);
            if (selected == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                try {
                    ImageIO.write(bi,"png",file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        int wt = waitTimeS.getValue();
        double x = Math.pow(1001.0,1.0/100.0);
        int i = (int)Math.pow(x,wt)-1;
        System.out.println("wait time="+i+"ms");
        simulator.setWaitTime(i);
    }
}
