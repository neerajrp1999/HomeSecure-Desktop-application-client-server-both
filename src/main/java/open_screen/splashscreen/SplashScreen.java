package open_screen.splashscreen;

import clent1.main.Socket_Client;
import server.main.Socket_Server;

import java.awt.Color;
import java.net.MalformedURLException;
import javax.swing.JDialog;

public class SplashScreen extends JDialog {
    boolean side;
    Socket_Client socket_client;
    Socket_Server socket_server;
    public SplashScreen(java.awt.Frame parent, boolean modal,boolean side) {
        super(parent, modal);
        this.side=side;
        try {
            if(side){
                socket_server= Socket_Server.getInstance();
                socket_server.startReadingServer();
            }else{
                socket_client= Socket_Client.getInstance();
                socket_client.startReading();
            }
            initComponents();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        getContentPane().setBackground(new Color(221, 221, 221));
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() throws MalformedURLException {

        curvesPanel1 = new open_screen.splashscreen.CurvesPanel();
        jLabel1 = new javax.swing.JLabel();
        pro = new open_screen.splashscreen.ProgressBarCustom();
        lbStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        if(side) {
            jLabel1.setIcon(new javax.swing.ImageIcon("C:/Users/morev/Desktop/project/swing/swingGUITester/src/main/resources/11.png")); // NOI18N
        }else{
            jLabel1.setIcon(new javax.swing.ImageIcon("C:/Users/morev/Desktop/project/swing/swingGUITester/src/main/resources/12.png")); // NOI18N
        }
        lbStatus.setForeground(new Color(200, 200, 200));
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbStatus.setText("Status");

        javax.swing.GroupLayout curvesPanel1Layout = new javax.swing.GroupLayout(curvesPanel1);
        curvesPanel1.setLayout(curvesPanel1Layout);
        curvesPanel1Layout.setHorizontalGroup(
            curvesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(curvesPanel1Layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addGroup(curvesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(278, Short.MAX_VALUE))
        );
        curvesPanel1Layout.setVerticalGroup(
            curvesPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(curvesPanel1Layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pro, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(curvesPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(curvesPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doTask("Connect To Database ...", 10);
                    doTask("Loading 10% ...", 20);
                    doTask("Loading 20% ...", 30);
                    doTask("Loading 30% ...", 40);
                    doTask("Loading 40% ...", 50);
                    doTask("Loading 50% ...", 60);
                    doTask("Loading 60% ...", 70);
                    doTask("Loading 70% ...", 80);
                    doTask("Loading 80% ...", 90);
                    doTask("Loading 90% ...", 100);
                    doTask("Done ...", 100);
                    dispose();
                    curvesPanel1.stop();    //  To Stop animation
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doTask(String taskName, int progress) throws Exception {
        lbStatus.setText(taskName);
        Thread.sleep(500); //  For Test
        pro.setValue(progress);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //SplashScreen dialog = new SplashScreen(new javax.swing.JFrame(), true);
                //dialog.setVisible(true);
            }
        });
    }
    private open_screen.splashscreen.CurvesPanel curvesPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbStatus;
    private open_screen.splashscreen.ProgressBarCustom pro;
}
