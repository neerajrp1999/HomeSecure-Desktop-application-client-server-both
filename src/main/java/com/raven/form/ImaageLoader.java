package com.raven.form;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

public class ImaageLoader extends JFrame {
    public ImaageLoader(String url) throws MalformedURLException {
        this.setTitle("Picture Application");
        System.out.println(url);
        JPanel panel1 = new JPanel();
        ImageIcon pic = new ImageIcon(new URL(url));
        panel1.add(new JLabel(pic));
        this.add(panel1);
        this.pack();
        this.setVisible(true);
    }
}