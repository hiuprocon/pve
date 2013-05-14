package com.github.hiuprocon.pve.ui;

import javax.swing.*;
import java.awt.*;

public class HBox extends JPanel {
    private static final long serialVersionUID = 1L;
    GridBagConstraints c;
    int index;

    public HBox() {
        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
    }

    public void myAdd(Component component, double weight) {
        c.gridx = index;
        c.weightx = weight;
        this.add(component, c);
        index++;
    }
}
