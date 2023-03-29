package com.raven.form;

import clent1.main.firebasedb;
import clent1.recognition.CaptureProfileImage;
import com.raven.main.Main;
import open_screen.splashscreen.WaitingScreenTraining;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;

public class Form_2 extends javax.swing.JPanel {
    firebasedb db=firebasedb.getInstance();
    int newGeneratedUserID;
    Main main;
    public Form_2(Main main) {
        this.main=main;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        err = new javax.swing.JLabel();
        userId = new com.raven.ShowUser.swing.TextField();
        username = new com.raven.ShowUser.swing.TextField();
        password = new com.raven.ShowUser.swing.TextField();
        button1 = new com.raven.ShowUser.swing.Button();
        imgLabel = new javax.swing.JLabel();

        this.newGeneratedUserID=db.getNewID();
        setBackground(new java.awt.Color(247, 247, 247));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(76, 76, 76));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ADD NEW USER DATA");

        userId.setForeground(new java.awt.Color(76, 76, 76));
        userId.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        userId.setHint("User Id:");
        userId.setEditable(false);
        userId.setText(String.valueOf(newGeneratedUserID));

        username.setForeground(new java.awt.Color(76, 76, 76));
        username.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        username.setHint("USER NAME:");

        password.setForeground(new java.awt.Color(76, 76, 76));
        password.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        password.setHint("password:");

        imgLabel.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        imgLabel.setForeground(new java.awt.Color(255, 0, 0));
        imgLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BufferedImage bufImage = null;
        try {
            bufImage = CaptureProfileImage.getTempPersonImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgLabel.setIcon(new ImageIcon(bufImage));
        imgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("double clicked");
                    face=true;
                    new CaptureProfileImage().call(newGeneratedUserID,imgLabel);
                }
            }
        });

        button1.setBackground(new java.awt.Color(86, 142, 255));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Start Scan");
        button1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        err.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        err.setForeground(new java.awt.Color(76, 76, 76));
        err.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        err.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(userId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                        .addComponent(imgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                        .addComponent(err, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                        .addComponent(button1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(30, 30, 30))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(imgLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1)
                                .addGap(40, 40, 40)
                                .addComponent(userId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(err, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        ))
                                .addGap(40, 40, 40))
        );
    }// </editor-fold>//GEN-END:initComponents
    boolean face=false;
    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        String name_s=username.getText().toString().trim();
        String pass_s=password.getText().toString().trim();
        if(name_s.length()<=0){
            err.setText("Write a proper name");
            return;
        }
        if(pass_s.length()<=0){
            err.setText("Write a proper password");
            return;
        }
        if(!face){
            err.setText("Upload Profile Pic first.....");
            return;
        }
        int result = JOptionPane.showConfirmDialog(null,"Sure? You Want to Add this user?", "ADD", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION){
            new addNewFaceInData(newGeneratedUserID,name_s,pass_s).mainCaller(main);
            //frame.dispose();
            //new AddNewUser.addNewFaceInData(id,name_s,pass_s).mainCaller();

                    /*
                    JOptionPane pane = new JOptionPane("Yes !!");
                    JOptionPane.showMessageDialog(pane, "My Goodness, this is so concise");
                     */
        }
    }


    private com.raven.ShowUser.swing.Button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel err;
    private javax.swing.JLabel imgLabel;
    private com.raven.ShowUser.swing.TextField password;
    private com.raven.ShowUser.swing.TextField userId;
    private com.raven.ShowUser.swing.TextField username;
    // End of variables declaration//GEN-END:variables




}
class addNewFaceInData extends JFrame {
    VideoCapture capture;
    Mat mat;
    private JLabel cameraScreen;
    private JButton btnCapture,btnStopCapture;
    int count=200;
    int id;
    String name;
    String pass;
    public addNewFaceInData(int id,String name,String pass){
        this.id=id;
        this.pass=pass;
        this.name=name;
    }
    public void callFrame(){

        setLayout(null);
        cameraScreen=new JLabel();
        cameraScreen.setBounds(0,0,640,480);
        add(cameraScreen);

        setSize(new Dimension(640,560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    public void startCamera(int id, String name, String pass, Main main){
        main.dispose();
        callFrame();
        capture=new VideoCapture(0);
        mat=new Mat();
        byte[] imageData;
        ImageIcon icon;
        while(true){
            setTitle("Fetch Capturing Remaining:"+count);
            capture.read(mat);
            Core.flip(mat,mat,1);
            final MatOfByte buf=new MatOfByte();
            Imgcodecs.imencode(".jpg",mat,buf);
            imageData=buf.toArray();
            icon= new ImageIcon(imageData);

            cameraScreen.setIcon(icon);
            if(count>0){
                Rect[] rectFace = detectFace(mat);
                int rectFaceLength = rectFace.length;
                Mat[] matFace = new Mat[rectFaceLength];
                for(int i=0; i<rectFaceLength; i++){
                    matFace[i] = mat.submat(rectFace[i]);
                    Imgproc.resize(matFace[i], matFace[i], new Size(200,200));
                }
                Boolean faceDetected=false;
                try{mat=matFace[0];
                    faceDetected=true;}
                catch (ArrayIndexOutOfBoundsException e){
                    faceDetected=false;
                }
                if(name==null){
                    name=new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
                }
                if(faceDetected){
                    Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGRA2GRAY);
                    long time=System.currentTimeMillis();

                    Imgcodecs.imwrite("src\\resources\\photos\\"+name+"."+id+"."+count+".jpg",mat);
                    count--;
                } else {
                    System.out.println("face not detected:"+System.currentTimeMillis());
                }
            }
            else {
                break;
            }
        }
        if(count<=0){
            dispose();
            capture.release();
            //new Training().call();
            firebasedb.getInstance().addData(String.valueOf(id),name,pass);
            WaitingScreenTraining dialog = new WaitingScreenTraining(new javax.swing.JFrame(), true);
            dialog.setVisible(true);

        }
    }


    private Rect[] detectFace(Mat matImage){
        MatOfRect faces = new MatOfRect();
        String HumanFace4 = "src\\resources\\haarcascades\\haarcascade_frontalface_alt2.xml";
        CascadeClassifier cascadeClassifier = new CascadeClassifier(HumanFace4);
        cascadeClassifier.detectMultiScale(matImage, faces , 1.1, 10, Objdetect.CASCADE_DO_CANNY_PRUNING, new Size(20, 20),
                matImage.size());
        return faces.toArray();
    }
    public void mainCaller(Main main){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startCamera(id,name,pass,main);
                    }
                }).start();

            }
        });
    }

}
class Training {

    synchronized void call() {

        File directory = new File("src\\resources\\photos");

        FilenameFilter imageFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") ;
            }
        };

        File[] files = directory.listFiles(imageFilter);
        opencv_core.MatVector photos = new opencv_core.MatVector(files.length);
        opencv_core.Mat labels = new opencv_core.Mat(files.length, 1 , opencv_core.CV_32SC1);
        IntBuffer bufferLabels = labels.createBuffer();
        int counter = 0;

        for( File image : files) {
            opencv_core.Mat photo = opencv_imgcodecs.imread(image.getAbsolutePath(), opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            int personId = Integer.parseInt(image.getName().split("\\.")[1]);
            System.out.println("file"+counter);

            opencv_imgproc.resize(photo, photo, new opencv_core.Size(160,160));
            photos.put(counter, photo);
            bufferLabels.put(counter, personId);
            counter++;
        }

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

    }
}