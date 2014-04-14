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

public class Simulator3GUI extends JFrame implements ActionListener, ChangeListener, A3Listener {
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
    Vector3d lookAt = new Vector3d(0.0, 0.0, 6.0);
    Vector3d camera = new Vector3d(0.0, 3.0, -6.0);
    Vector3d up = new Vector3d(0.0, 1.0, 0.0);
    JButton configB;
    JFrame configFrame;
    TextAreaComponent2D taComp;
    Action3D timerI;
    Action3D redViewI;
    Action3D blueViewI;
    VRML defaultViewI;
    Action3D[] jewelsI = new Action3D[20];

    public Simulator3GUI(Simulator3 simulator) {
        super("Simulator3");
        this.simulator = simulator;
        HBox box1 = new HBox();
        VBox box3 = new VBox();
        configB = new JButton("Config");
        configB.addActionListener(this);
        box3.myAdd(configB, 0);
        canvas = (A3Canvas) simulator.w.getMainCanvas();
        canvas.addA3Listener(this);
        taComp = new TextAreaComponent2D(4);
        canvas.add(taComp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initIndicator();
        defaultView();
        
        canvas.setPreferredSize(new Dimension(700, 650));
        canvas.setCanvasWidthInPWorld(2.0);
        box3.myAdd(canvas, 1);

        configFrame = new JFrame("Config");
        VBox box2 = new VBox();
        //box2.setPreferredSize(new Dimension(500,600));

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

        configFrame.add(box2);
        configFrame.pack();

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
    void initIndicator() {
        try {
            if (timerI==null) {
                timerI = new Action3D("x-res:///res/prototype/ProgressBar01.a3");
                timerI.setMode(Motion.Mode.PAUSE);
                timerI.setLabelLoc(0,0);
                timerI.setScale(0.1);
                timerI.setLoc(0.3,0.2,-1.0);
                redViewI = new Action3D("x-res:///res/prototype/ChoroQred.a3");
                redViewI.setLabelLoc(0,0);
                redViewI.setScale(0.02);
                redViewI.setLoc(0.3,0.25,-1.0);
                redViewI.setLabel("redView");
                blueViewI = new Action3D("x-res:///res/prototype/ChoroQblue.a3");
                blueViewI.setLabelLoc(0,0);
                blueViewI.setScale(0.02);
                blueViewI.setLoc(-0.3,0.25,-1.0);
                blueViewI.setLabel("blueView");
                defaultViewI = new VRML("x-res:///res/SonyZ1U.wrl");
                defaultViewI.setLabelLoc(0,0);
                defaultViewI.setScale(0.3);
                defaultViewI.setRev(0,90,0);
                defaultViewI.setLoc(0.0,0.25,-1.0);
                defaultViewI.setLabel("defaultView");
                canvas.addLockedA3(timerI);
                canvas.addLockedA3(redViewI);
                canvas.addLockedA3(blueViewI);
                canvas.addLockedA3(defaultViewI);
                for (int i=0;i<10;i++) {
                    jewelsI[i] = new Action3D("x-res:///res/prototype/box.a3");
                    jewelsI[i].setScale(0.1);
                    jewelsI[i].setLoc(i/30.0,0.3,-1.0);
                    canvas.addLockedA3(jewelsI[i]);
                }
                for (int i=10;i<20;i++) {
                    jewelsI[i] = new Action3D("x-res:///res/prototype/box.a3");
                    jewelsI[i].setScale(0.1);
                    jewelsI[i].setLoc((i-10)/30.0,0.28,-1.0);
                    canvas.addLockedA3(jewelsI[i]);
                }
            }
            timerI.setLabel("time: 0.0");
            for (int i=0;i<20;i++) {
                jewelsI[i].change("haltWhite");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    void defaultView() {
        double y = 100;
        double z = 300;
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
        taComp.appendText(s);
    }

    public void updateTime(double t) {
        timeL.setText(String.format("time: %9.2f",t));
        timerI.setLabel(String.format("time: %9.2f",t));
        timerI.setPauseTime(t/5000.0);
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
            try{simulator.initWorld();}catch(Exception e){e.printStackTrace();}
            initIndicator();
            oneCarCB.setSelected(false);
            waitTimeS.setValue(51);
            updateTime(0);
            taComp.clear();
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
        } else if (source==configB) {
            configFrame.setVisible(true);
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
    @Override
    public void mouseClicked(A3Event arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseDoubleClicked(A3Event arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseDragged(A3Event arg0) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mousePressed(A3Event arg0) {
        A3Object a3 = arg0.getA3Object();
        if (a3==redViewI) {
            redView();
        } else if (a3==blueViewI) {
            blueView();
        } else if (a3==defaultViewI) {
            defaultView();
        }
    }
    @Override
    public void mouseReleased(A3Event arg0) {
        // TODO Auto-generated method stub
        
    }
}
