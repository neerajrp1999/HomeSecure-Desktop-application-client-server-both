package open_screen.splashscreen;

import clent1.main.firebasedb;
import com.raven.form.Form_Home;
import com.raven.main.Main;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;

public class WaitingScreenTraining extends JDialog {

    public WaitingScreenTraining(Frame parent, boolean modal) {
        super(parent, modal);
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

                doUpdate("The Model Training Process is started, this will take will ",5);


                File directory = new File("src\\resources\\photos");

                FilenameFilter imageFilter = new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") ;
                    }
                };
                doUpdate("The Model Training Process is still on, this will take will ",35);
                File[] files = directory.listFiles(imageFilter);
                opencv_core.MatVector photos = new opencv_core.MatVector(files.length);
                opencv_core.Mat labels = new opencv_core.Mat(files.length, 1 , opencv_core.CV_32SC1);
                IntBuffer bufferLabels = labels.createBuffer();
                int counter = 0;
                doUpdate("The Model Training Process is still on, this will take will ",40);
                for( File image : files) {
                    opencv_core.Mat photo = opencv_imgcodecs.imread(image.getAbsolutePath(), opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
                    int personId = Integer.parseInt(image.getName().split("\\.")[1]);
                    System.out.println("file"+counter);

                    opencv_imgproc.resize(photo, photo, new opencv_core.Size(160,160));
                    photos.put(counter, photo);
                    bufferLabels.put(counter, personId);
                    counter++;
                }
                doUpdate("The Model Training Process is still on, this will take will ",65);
                //classifiers
                //opencv_face.FaceRecognizer eigenFaces = createEigenFaceRecognizer();
                //opencv_face.FaceRecognizer fisherFaces = createFisherFaceRecognizer();
                opencv_face.FaceRecognizer lbph = createLBPHFaceRecognizer();

                // classifiers -> learning-training to generate yml codes
                //eigenFaces.train(photos, labels);
                //eigenFaces.save("src\\resources\\classifierEigenFaces.yml");
                //fisherFaces.train(photos, labels);
                //fisherFaces.save("src\\resources\\classifierFisherFaces.yml");
                lbph.train(photos, labels);
                lbph.save("src\\resources\\classifierLBPH.yml");

                System.out.println("done");
                doUpdate("The Model Training Process is still on, this will take will ",100);

                dispose();
                curvesPanel1.stop();    //  To Stop animation
                firebasedb.getInstance().UpdateInModelStatus(1);
                try {
                    String url1="http://127.0.0.1:7000/apicall/noti_push/";
                    Desktop.getDesktop().browse(URI.create(url1));
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Model Is Created, Updation/Uploading Of Model is Started. this may take minutes. so wait for 2 minutes!!!!",6000);
                dialog.setVisible(true);
                new Main().setVisible(true);

            }
        }).start();
    }

    private void doUpdate(String taskName,int i)  {
        lbStatus.setText(taskName);
        pro.setValue(i);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                WaitingScreenTraining dialog = new WaitingScreenTraining(new JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }
    private CurvesPanel curvesPanel1;
    private JLabel jLabel1;
    private JLabel lbStatus;
    private ProgressBarCustom pro;
}
