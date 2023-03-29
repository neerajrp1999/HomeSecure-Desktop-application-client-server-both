package server.login;


import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import server.main.Main;
import server.swing.EventLogin;
import server.swing.EventOpenTheDoor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home_Screen_Server extends JPanel {

    private MigLayout layout;
    private Admin_Login admin_login;
    private Talk_To_Person talk;
    private Open_Door open_door;
    private Animator animator;
    private boolean isLogin;
    public static Color mainColor = new Color(246, 207, 104);

    public void setAnimate(int animate) {
        layout.setComponentConstraints(admin_login, "pos (50%)-290px-" + animate + " 0.4al n n");
        layout.setComponentConstraints(talk, "pos (50%)-10px+" + animate + " 0.4al n n");
        if (animate == 30) {
            if (isLogin) {
                setComponentZOrder(talk, 0);
            } else {
                setComponentZOrder(admin_login, 0);
            }
        }
        revalidate();
    }
    Main main;
    public Home_Screen_Server(Main main) {
        this.main=main;
        initComponents();
        init();
        initAnimator();
    }

    private void initAnimator() {
        animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isLogin) {
                    admin_login.setAlpha(fraction);
                    talk.setAlpha(1f - fraction);
                } else {
                    admin_login.setAlpha(1f - fraction);
                    talk.setAlpha(fraction);
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
        admin_login = new Admin_Login(main);
        talk = new Talk_To_Person();
        open_door = new Open_Door();
        applyEvent(admin_login, false);
        applyEvent(talk, true);
        add(admin_login, "pos (50%)-290px 0.4al n n");
        add(talk, "pos (50%)-10px 0.4al n n");
        add(open_door, "pos (40%)-10px 0.75al n n");
        admin_login.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(false);
                }
            }
        });
        talk.addMouseListener(new MouseAdapter() {
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

    public void setEventLogin(EventLogin event) {
        talk.setEventLogin(event);
    }

    public void setEventOpenDoor(EventOpenTheDoor event) {
        open_door.setEventOpenTheDoor(event);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
