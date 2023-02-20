package recognition;

import org.example.AddNewUser;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CaptureProfileImage {
    VideoCapture capture;
    Mat mat;
    Boolean ch=true;
    Thread r;
    private JLabel cameraScreen;
    private JButton btnCapture,btnStopCapture;
    boolean Clicked = false;
    JFrame j;
    public void startCamera(AddNewUser addNewUser,int id){

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture=new VideoCapture(0);
        mat=new Mat();
        byte[] imageData;
        ImageIcon icon;
        while(ch){
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
                    if(faceDetected){
                        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_BGRA2GRAY);
                        Imgcodecs.imwrite("src\\resources\\TempPhotos\\"+id+".jpg",mat);
                        Clicked=false;
                        j.setVisible(false);
                        JFrame jf=new JFrame();
                        BufferedImage bufImage = null;
                        try {
                            bufImage = getTempImage();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        jf.setLayout(null);
                        jf.setSize(225,300);
                        JLabel l=new JLabel(new ImageIcon(bufImage));
                        l.setBounds(0,0,200,200);
                        jf.add(l);
                        JButton ok=new JButton("Ok");
                        ok.setBounds(0,200,100,50);
                        jf.add(ok);
                        JButton cancel=new JButton("Cancel");
                        cancel.setBounds(100,200,100,50);
                        jf.add(cancel);
                        cancel.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jf.dispose();
                                j.setVisible(true);
                            }
                        });
                        ok.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                jf.dispose();
                                j.dispose();
                                callDispone();
                                addNewUser.imageSet();
                            }
                            private void callDispone() {
                                //r.interrupt();
                                ch=false;
                                capture.release();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                        jf.setLocationRelativeTo(null);
                        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        jf.setVisible(true);
                    } else {
                        System.out.println("face not detected:"+System.currentTimeMillis());
                    }
            }
        }
    }
    public static BufferedImage getTempImage() throws IOException {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        String file = "src\\resources\\TempPhotos\\temp.jpg";
        Mat image = Imgcodecs.imread(file);

        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);

        byte[] byteArray = matOfByte.toArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage b= ImageIO.read(in);

        BufferedImage resizedImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(b, 0, 0, 200, 200, null);
        g2.dispose();
        return resizedImg;
    }

    private Rect[] detectFace(Mat matImage){
        MatOfRect faces = new MatOfRect();
        String HumanFace4 = "src\\resources\\haarcascades\\haarcascade_frontalface_alt2.xml";
        CascadeClassifier cascadeClassifier = new CascadeClassifier(HumanFace4);
        cascadeClassifier.detectMultiScale(matImage, faces , 1.1, 10, Objdetect.CASCADE_DO_CANNY_PRUNING, new Size(20, 20),
                matImage.size());
        return faces.toArray();
    }
   public void callFrame(){
       j=new JFrame();
       j.setLayout(null);
       cameraScreen=new JLabel();
       cameraScreen.setBounds(0,0,640,480);
       j.add(cameraScreen);

       btnCapture=new JButton("Capture");
       btnCapture.setBounds(200,480,80,40);
       j.add(btnCapture);

       btnStopCapture=new JButton("Cancel");
       btnStopCapture.setBounds(340,480,80,40);
       j.add(btnStopCapture);

       btnCapture.addActionListener(new AbstractAction() {
           @Override
           public void actionPerformed(ActionEvent e) {
               Clicked=true;
           }
       });
       btnStopCapture.addActionListener(new AbstractAction() {
           @Override
           public void actionPerformed(ActionEvent e) {
               j.setVisible(false);
               j.dispose();
               callDispone();
               j.dispose();
           }

           private void callDispone() {
               //r.interrupt();
               ch=false;
               capture.release();
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException ex) {
                   throw new RuntimeException(ex);
               }
           }
       });
       j.setSize(new Dimension(640,560));
       j.setLocationRelativeTo(null);
       j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       j.setVisible(true);
   }
    public void call(AddNewUser addNewUser,int id){
        final Boolean[] a = {false};
        callFrame();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                r=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CaptureProfileImage.this.startCamera(addNewUser,id);
                    }
                });
                r.start();
            }
        });
    }

}