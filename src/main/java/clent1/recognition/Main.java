package clent1.recognition;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JFrame {
    VideoCapture capture;
    Mat mat;
    private JLabel cameraScreen;
    private JButton btnCapture,btnStopCapture;
    boolean Clicked = false;
    public Main(){
        setLayout(null);
        cameraScreen=new JLabel();
        cameraScreen.setBounds(0,0,640,480);
        add(cameraScreen);

        btnCapture=new JButton("Capture");
        btnCapture.setBounds(300,480,80,40);
        add(btnCapture);

        btnStopCapture=new JButton("Stop Capture");
        btnStopCapture.setBounds(390,480,80,40);
        add(btnStopCapture);

        btnCapture.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clicked=true;
            }
        });
        btnStopCapture.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clicked=false;
            }
        });
        setSize(new Dimension(640,560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void startCamera(){
        capture=new VideoCapture(0);
        mat=new Mat();
        byte[] imageData;
        String name=JOptionPane.showInputDialog(this,"Enter Image name").toString().trim();
        ImageIcon icon;
        while(true){
            capture.read(mat);
            Core.flip(mat,mat,1);
            final MatOfByte buf=new MatOfByte();

            Imgcodecs.imencode(".jpg",mat,buf);

            imageData=buf.toArray();
            icon= new ImageIcon(imageData);

            cameraScreen.setIcon(icon);
            if(Clicked){
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

                        Imgcodecs.imwrite("src\\resources\\photos\\"+name+"."+time+".jpg",mat);
                    } else {
                        System.out.println("face not detected:"+System.currentTimeMillis());
                    }
            }
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
    public static void main(String[] arg){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main tester1= new Main();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tester1.startCamera();
                    }
                }).start();

            }
        });
    }

}