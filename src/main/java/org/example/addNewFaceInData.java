package org.example;

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
 import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
 import java.util.Date;

import static org.bytedeco.javacpp.opencv_face.*;

public class addNewFaceInData extends JFrame {
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

    public void startCamera(int id,String name,String pass){
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

                    Imgcodecs.imwrite("src\\resources\\photos\\"+name+"."+id+"."+time+".jpg",mat);
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
            new Training().call();
            firebasedb.getInstance().addData(String.valueOf(id),name,pass);
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
    public void mainCaller(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startCamera(id,name,pass);
                    }
                }).start();

            }
        });
    }

}
 class Training {

    synchronized void call() {

        File directory = new File ("src\\resources\\photos");

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
        opencv_face.FaceRecognizer eigenFaces = createEigenFaceRecognizer();
        opencv_face.FaceRecognizer fisherFaces = createFisherFaceRecognizer();
        opencv_face.FaceRecognizer lbph = createLBPHFaceRecognizer();

        // classifiers -> learning-training to generate yml codes
        //eigenFaces.train(photos, labels);
        //eigenFaces.save("src\\resources\\classifierEigenFaces.yml");
        //fisherFaces.train(photos, labels);
        //fisherFaces.save("src\\resources\\classifierFisherFaces.yml");
        lbph.train(photos, labels);
        lbph.save("src\\resources\\classifierLBPH.yml");

        System.out.println("done");

        new server().callJFrame_server_page2();
    }
}