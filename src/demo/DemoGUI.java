package demo;

import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Vector3d;
import com.github.hiuprocon.pve.core.PVEObject;
import jp.sourceforge.acerola3d.a3.A3Canvas;
import jp.sourceforge.acerola3d.a3.A3CanvasInterface;

public class DemoGUI extends JFrame implements ChangeListener {
    private static final long serialVersionUID = 1L;
    A3Canvas canvas;
    JRadioButton aRB, bRB, cRB, dRB, eRB, fRB, gRB;
    KeyListener aKL, bKL, cKL, dKL, eKL, fKL, gKL;
    KeyListener activeKL = null;

    public DemoGUI(A3Canvas c) {
        super("Debug");
        setLayout(new FlowLayout());
        canvas = c;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setCameraLocImmediately(0, 5, 30);
        // c.setCameraLocImmediately(5,2,5);
        c.setCameraLookAtPointImmediately(0, 0, 0);
        Vector3d lookAt = new Vector3d(0.0, 0.0, 6.0);
        Vector3d camera = new Vector3d(0.0, 3.0, -6.0);
        Vector3d up = new Vector3d(0.0, 1.0, 0.0);
        c.setNavigationMode(A3CanvasInterface.NaviMode.CHASE, lookAt, camera,
                up, 1.0);
        c.setSize(500, 300);
        add(c);

        JPanel p = new JPanel();
        Box b = Box.createVerticalBox();
        p.add(b);
        aRB = new JRadioButton("A");
        aRB.addChangeListener(this);
        b.add(aRB);
        bRB = new JRadioButton("B");
        bRB.addChangeListener(this);
        b.add(bRB);
        cRB = new JRadioButton("C");
        cRB.addChangeListener(this);
        b.add(cRB);
        dRB = new JRadioButton("D");
        dRB.addChangeListener(this);
        b.add(dRB);
        eRB = new JRadioButton("E");
        eRB.addChangeListener(this);
        b.add(eRB);
        fRB = new JRadioButton("F");
        fRB.addChangeListener(this);
        b.add(fRB);
        gRB = new JRadioButton("G");
        gRB.addChangeListener(this);
        b.add(gRB);
        ButtonGroup bg = new ButtonGroup();
        bg.add(aRB);
        bg.add(bRB);
        bg.add(cRB);
        bg.add(dRB);
        bg.add(eRB);
        bg.add(fRB);
        bg.add(gRB);
        add(p);

        pack();
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == aRB)
            change(aKL);
        else if (e.getSource() == bRB)
            change(bKL);
        else if (e.getSource() == cRB)
            change(cKL);
        else if (e.getSource() == dRB)
            change(dKL);
        else if (e.getSource() == eRB)
            change(eKL);
        else if (e.getSource() == fRB)
            change(fKL);
        else if (e.getSource() == gRB)
            change(gKL);
    }

    void change(KeyListener kl) {
        if (activeKL != kl) {
            if (kl == null)
                return;
            activeKL = kl;
            canvas.addKeyListener(kl);
            canvas.setAvatar(((PVEObject) kl).getMainA3());
        } else {
            activeKL = null;
            canvas.removeKeyListener(kl);
            canvas.setAvatar(null);
        }
    }
}
