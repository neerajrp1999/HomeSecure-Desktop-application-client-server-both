package clent1.login;


import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import clent1.swing.*;

public class Home_Screen_Client extends javax.swing.JPanel {

    private MigLayout layout;
    private Ring_the_bell ring_the_bell;
    private Enter_into_House enter_into_house;
    private Call_User callUser;
    private Animator animator;
    private boolean isLogin;
    public static Color mainColor = new Color(246, 207, 104);

    public void setAnimate(int animate) {
        layout.setComponentConstraints(ring_the_bell, "pos (50%)-290px-" + animate + " 0.4al n n");
        layout.setComponentConstraints(enter_into_house, "pos (50%)-10px+" + animate + " 0.4al n n");
        if (animate == 30) {
            if (isLogin) {
                setComponentZOrder(enter_into_house, 0);
            } else {
                setComponentZOrder(ring_the_bell, 0);
            }
        }
        revalidate();
    }

    public Home_Screen_Client() {
        initComponents();
        init();
        initAnimator();
    }

    private void initAnimator() {
        animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isLogin) {
                    ring_the_bell.setAlpha(fraction);
                    enter_into_house.setAlpha(1f - fraction);
                } else {
                    ring_the_bell.setAlpha(1f - fraction);
                    enter_into_house.setAlpha(fraction);
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
        ring_the_bell = new Ring_the_bell();
        enter_into_house = new Enter_into_House();
        callUser = new Call_User();
        applyEvent(ring_the_bell, false);
        applyEvent(enter_into_house, true);
        add(ring_the_bell, "pos (50%)-290px 0.4al n n");
        add(enter_into_house, "pos (50%)-10px 0.4al n n");
        add(callUser, "pos (40%)-10px 0.75al n n");
        ring_the_bell.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(false);
                }
            }
        });
        enter_into_house.addMouseListener(new MouseAdapter() {
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
