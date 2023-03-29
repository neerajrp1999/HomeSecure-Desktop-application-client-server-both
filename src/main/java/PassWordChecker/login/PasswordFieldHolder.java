package PassWordChecker.login;

import PassWordChecker.main.Main;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PasswordFieldHolder extends javax.swing.JPanel {

    private MigLayout layout;
    private Holder register;
    public static Color mainColor = new Color(246, 207, 104);

    public void setAnimate(int animate) {
        layout.setComponentConstraints(register, "pos (50%)-290px-" + animate + " 0.5al n n");
        revalidate();
    }
    ArrayList<String> arr;
    Main main;
    public PasswordFieldHolder(ArrayList<String> arr, Main main) {
        this.main=main;
        this.arr=arr;
        initComponents();
        init();
    }

    private void init() {
        setBackground(mainColor);
        layout = new MigLayout("fill", "fill", "fill");
        setLayout(layout);
        register = new Holder(arr,main);
        add(register, "pos (50%)-100px 0.5al n n");

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
