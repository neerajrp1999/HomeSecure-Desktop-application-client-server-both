package com.raven.form;

import com.raven.ShowUser.login.ImageViewer;
import com.raven.ShowUser.login.UserDataViewer;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Form_3 extends javax.swing.JPanel {
    private MigLayout layout;
    private ImageViewer imageViewerFrame;
    private UserDataViewer login;
    private Animator animator;
    private boolean isLogin;
    public static Color mainColor = new Color(246, 207, 104);

    public void setAnimate(int animate) {
        layout.setComponentConstraints(imageViewerFrame, "pos (50%)-290px-" + animate + " 0.5al n n");
        layout.setComponentConstraints(login, "pos (50%)-10px+" + animate + " 0.5al n n");
        if (animate == 30) {
            if (isLogin) {
                setComponentZOrder(login, 0);
            } else {
                setComponentZOrder(imageViewerFrame, 0);
            }
        }
        revalidate();
    }

    com.raven.main.Main main;
    ArrayList<String> data;
    public Form_3(com.raven.main.Main main, ArrayList<String> data) {
        this.data=data;
        //this.main= main;
        this.main= main;
        initComponents();
        init();
        initAnimator();
    }

    private void initAnimator() {
        animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isLogin) {
                    imageViewerFrame.setAlpha(fraction);
                    login.setAlpha(1f - fraction);
                } else {
                    imageViewerFrame.setAlpha(1f - fraction);
                    login.setAlpha(fraction);
                }
            }
        });
        animator.addTarget(new PropertySetter(this, "animate", 0, 30, 0));
        animator.setResolution(0);
    }

    private void init() {
        setBackground(mainColor);
        layout = new MigLayout("fill", "fill", "fill");
        setLayout(layout);
        imageViewerFrame = new ImageViewer(data);
        login = new UserDataViewer(main,data);
        applyEvent(imageViewerFrame, false);
        applyEvent(login, true);
        add(imageViewerFrame, "pos (50%)-290px 0.5al n n");
        add(login, "pos (50%)-10px 0.5al n n");
        imageViewerFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(false);
                }
            }
        });
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(true);
                }
            }
        });
    }

    public void showLogin(boolean show) {
        if (show != isLogin) {
            if (!animator.isRunning()) {
                isLogin = show;
                animator.start();
            }
        }
    }

    private void applyEvent(JComponent panel, boolean login) {
        for (Component com : panel.getComponents()) {
            com.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    showLogin(login);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 698, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 452, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}