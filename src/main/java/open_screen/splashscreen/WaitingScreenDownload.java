package open_screen.splashscreen;

import clent1.main.firebasedb;
import clent1.recognition.Recognition;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WaitingScreenDownload extends JDialog {
    String url_s;

    public WaitingScreenDownload(Frame parent, boolean modal,String url_s) {
        super(parent, modal);
        this.url_s=url_s;

        try {
            initComponents();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        getContentPane().setBackground(new Color(221, 221, 221));
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() throws MalformedURLException {

        curvesPanel1 = new CurvesPanel();
        jLabel1 = new JLabel();
        pro = new ProgressBarCustom();
        lbStatus = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        lbStatus.setForeground(new Color(200, 200, 200));
        lbStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lbStatus.setText("Status");

        GroupLayout curvesPanel1Layout = new GroupLayout(curvesPanel1);
        curvesPanel1.setLayout(curvesPanel1Layout);
        curvesPanel1Layout.setHorizontalGroup(
            curvesPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(curvesPanel1Layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addGroup(curvesPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(pro, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbStatus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(278, Short.MAX_VALUE))
        );
        curvesPanel1Layout.setVerticalGroup(
            curvesPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(curvesPanel1Layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pro, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbStatus)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(curvesPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(curvesPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    doUpdate("The File Is Missing, Downloading From Web. this may take a while");
                    URL url = new URL(url_s);
                    URLConnection urlConnect = url.openConnection();
                    InputStream fileIn = urlConnect.getInputStream();
                    FileOutputStream fileOut = new FileOutputStream("src\\resources\\classifierLBPH.yml");
                    int x;
                    while ((x = fileIn.read()) != -1) {
                        fileOut.write(x);
                    }
                    fileOut.close();

                    firebasedb.getInstance().UpdateInModelStatus(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dispose();
                curvesPanel1.stop();    //  To Stop animation
                try {
                    new Recognition().Call_Recognizer();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }

    private void doUpdate(String taskName) throws Exception {
        lbStatus.setText(taskName);
        pro.setValue(100);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                WaitingScreenDownload dialog = new WaitingScreenDownload(new JFrame(), true,firebasedb.getInstance().fetModelURL());
                dialog.setVisible(true);
            }
        });
    }
    private CurvesPanel curvesPanel1;
    private JLabel jLabel1;
    private JLabel lbStatus;
    private ProgressBarCustom pro;
}
