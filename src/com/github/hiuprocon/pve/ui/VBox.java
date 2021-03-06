package com.github.hiuprocon.pve.ui;

import javax.swing.*;
import java.awt.*;

public class VBox extends JPanel {
    private static final long serialVersionUID = 1L;
    GridBagConstraints c;
    int index;

    public VBox() {
        this.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
    }

    public void myAdd(Component component, double weight) {
        c.gridy = index;
        c.weighty = weight;
        this.add(component, c);
        index++;
    }
}
