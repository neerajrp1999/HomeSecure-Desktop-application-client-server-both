package clent1.recognition;

import PassWordChecker.main.Main;
import clent1.swing.EventOpenTheDoor;
import open_screen.splashscreen.WaitingScreen;
import open_screen.splashscreen.WaitingScreenDownload;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import clent1.main.firebasedb;
//import org.example.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;


public class Recognition {
	/*
	public static void main(String[] args) throws Exception {
		OpenCVFrameConverter.ToMat convertMat = new OpenCVFrameConverter.ToMat(); // convert image to Mat
		OpenCVFrameGrabber camera1 = new OpenCVFrameGrabber(0); // capturing webcam images
		String[] people = {"", "Neeraj", "Suraj", "Priscila","Neeraj"};
		camera1.start();
		CascadeClassifier faceDetector = new CascadeClassifier("src\\resources\\haarcascades\\haarcascade_frontalface_alt.xml");
		
		//FaceRecognizer recognizer = createEigenFaceRecognizer();  // classifier
		//recognizer.load("src\\resources\\classifierEigenFaces.yml");
		// recognizer.setThreshold(8000);      // trust number  
		
		//FaceRecognizer recognizer = createFisherFaceRecognizer();
		//recognizer.load("src\\resources\\classifierFisherFaces.yml");
		
		FaceRecognizer recognizer = createLBPHFaceRecognizer();
		recognizer.load("src\\resources\\classifierLBPH.yml");
		recognizer.setThreshold(65.0);
		
		CanvasFrame cFrame = new CanvasFrame("Recognition", CanvasFrame.getDefaultGamma() / camera1.getGamma()); // drawing a window

		cFrame.setSize(640,560);
		Frame capturedFrame = null;
		Mat colorImage = new Mat();

		int sampleNumber = 30;
		int sample = 1;

		while ((capturedFrame = camera1.grab()) != null) {
			colorImage = convertMat.convert(capturedFrame);
			Mat grayImage = new Mat();
			opencv_core.flip(colorImage,colorImage,1);
			opencv_imgproc.cvtColor(colorImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);
			RectVector detectedFaces = new RectVector();
			faceDetector.detectMultiScale(grayImage, detectedFaces, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));

			for (int i = 0; i < detectedFaces.size(); i++) {
				Rect faceData = detectedFaces.get(i);
				opencv_imgproc.rectangle(colorImage, faceData, new Scalar(0, 0, 255, 0));

				Mat capturedface = new Mat(grayImage, faceData);
				opencv_imgproc.resize(capturedface, capturedface, new Size(160, 160));
				
				IntPointer label = new IntPointer(1);
				DoublePointer confidence = new DoublePointer(1); 
				recognizer.predict(capturedface, label, confidence);
				int selection = label.get(0);
				String name;
				if (selection == -1) {
					name="Unknown";
				} else {
					name= people[selection] + " - " + confidence.get(0);
				}
				
				// entering the name on the screen
				int x = Math.max(faceData.tl().x() -10, 0); 
				int y = Math.max(faceData.tl().y() -10, 0);
				opencv_imgproc.putText(colorImage, name, new Point(x,y), opencv_core.FONT_HERSHEY_PLAIN, 1.4, new Scalar(0,255,0,0));

			}

			if (cFrame.isVisible()) {
				cFrame.showImage(capturedFrame);
			}

			if (sample > sampleNumber) {
				break;
			}
			if(!cFrame.isActive()){
				camera1.stop();
				break;
			}
		}


		cFrame.dispose(); // free memory
		camera1.stop();
		camera1.close();
		colorImage.close();
		faceDetector.close();
	}
	*/

	OpenCVFrameConverter.ToMat convertMat;
	OpenCVFrameGrabber camera1;
	CascadeClassifier faceDetector;
	FaceRecognizer recognizer;
	CanvasFrame cFrame;
	firebasedb db=firebasedb.getInstance();
	public static void main(String[] args){
		new Recognition().Call_Recognizer();
	}
	static EventOpenTheDoor eventTakePassword;

	public static void setEventTakePassword(EventOpenTheDoor eventTakePassword) {
		eventTakePassword=eventTakePassword;
	}

	public synchronized void Call_Recognizer(){
		new Thread(new Runnable() {
			public void run() {
				int RecognizeId=-1;
				long is=db.checkUpdateInModel();
				if(is==0||is==1){
					try {
						RecognizeId=new Recognition().Recognize();
						System.out.println(RecognizeId);

						afterRecognazation(RecognizeId);
					} catch (Exception e) {
						//throw new RuntimeException(e);
						System.out.println(e.toString());
						WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Something Is Wrong Please Check",4000);
						dialog.setVisible(true);
					}
				}else if(is==2){

					WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Error: Internet Access Problem !!",5000);
					dialog.setVisible(true);
				}else{
					URL url = null;
					try {
						String url_s=db.fetModelURL();
						if(url_s==null){
							//firebasedb.Internet_Access_Problem();
							WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Error: Internet Access Problem !!",5000);
							dialog.setVisible(true);
						} else {
							WaitingScreenDownload dialog = new WaitingScreenDownload(new javax.swing.JFrame(), true,url_s);
							dialog.setVisible(true);
						}

					} catch (Exception e) {
						//throw new RuntimeException(e);
						System.out.println(e.toString());
						WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Something Is Wrong Please Check",4000);
						dialog.setVisible(true);
					}

				}

			}
		}).start();
	}
	public synchronized void Call_Admin_Recognizer(server.main.Main main){
		new Thread(new Runnable() {
			public void run() {
				int RecognizeId=-1;
				try {
					RecognizeId=new Recognition().Recognize();
					System.out.println(RecognizeId);
					if(RecognizeId==1 || RecognizeId==2){
						//s.Admin_Page();
						//main.setVisible(false);
						main.dispose();
						new com.raven.main.Main().setVisible(true);
					}else{
						WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true," You are not admin !!",4000);
						dialog.setVisible(true);
					}
				} catch (Exception e) {
					//throw new RuntimeException(e);
					System.out.println(e.toString());
					WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"Something Is Wrong Please Check",4000);
					dialog.setVisible(true);
				}
			}
		}).start();
	}

	private void afterRecognazation(int RecognizeId) {
		try{
			if(RecognizeId==-1 || RecognizeId==0 || !db.checkIdNo(RecognizeId)){
				WaitingScreen dialog = new WaitingScreen(new javax.swing.JFrame(), true,"  Invalid face !!",4000);
				dialog.setVisible(true);
			}else{
				//eventTakePassword.OpenDoor();4
				System.out.println("inter");
				ArrayList<String> data= db.readDataOfUserByID(RecognizeId);
				new Main(data).setVisible(true);

				/*
				ArrayList<String> data= db.readDataOfUserByID(RecognizeId);
				String pass = JOptionPane.showInputDialog("Hi "+data.get(0)+"!!\nGive Password:");
				JOptionPane pane;
				Timer timer;
				JDialog dialog;
				if (pass.equals(data.get(1))) {

					pane = new JOptionPane("Welcome to home!\nOpening Door.");
					dialog = pane.createDialog("Welcome !!");
					timer = new Timer(5000, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
						}
					});

				}else{
					WaitingScreen dialog1 = new WaitingScreen(new javax.swing.JFrame(), true,"  Invalid, Password is wrong!!",4000);
					dialog1.setVisible(true);
				}
				*/
			}
		} catch (Exception e){
			JOptionPane pane;
			Timer timer;
			JDialog dialog;
			pane = new JOptionPane("Invalid  something is wrong!!");
			dialog = pane.createDialog("Error");
			timer = new Timer(3000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.setVisible(false);
				}
			});
			timer.setRepeats(false);
			timer.start();
			dialog.setVisible(true);
			//frame.setVisible(true);
		}

	}
	public synchronized int Recognize() throws Exception {
		convertMat = new OpenCVFrameConverter.ToMat();
		camera1 = new OpenCVFrameGrabber(0);

		camera1.start();
		faceDetector = new CascadeClassifier("src\\resources\\haarcascades\\haarcascade_frontalface_alt.xml");



		recognizer = createLBPHFaceRecognizer();
		recognizer.load("src\\resources\\classifierLBPH.yml");
		recognizer.setThreshold(65.0);

		cFrame = new CanvasFrame("Recognition", CanvasFrame.getDefaultGamma() / camera1.getGamma());

		cFrame.setSize(640,560);
		Frame capturedFrame = null;
		Mat colorImage = new Mat();
		int faceCaunted =0;
		int faceRecognizeID =0;
		int sampleNumber = 30;
		int sample = 1;
		long endTime = System.currentTimeMillis() + 15000;
		while ((capturedFrame = camera1.grab()) != null && System.currentTimeMillis() < endTime) {
			cFrame.setTitle("Recognition("+(endTime-System.currentTimeMillis())/1000+" seconds left)");
			colorImage = convertMat.convert(capturedFrame);
			Mat grayImage = new Mat();
			opencv_core.flip(colorImage,colorImage,1);
			opencv_imgproc.cvtColor(colorImage, grayImage, opencv_imgproc.COLOR_BGRA2GRAY);
			RectVector detectedFaces = new RectVector();
			faceDetector.detectMultiScale(grayImage, detectedFaces, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));

			for (int i = 0; i < detectedFaces.size(); i++) {
				Rect faceData = detectedFaces.get(i);
				opencv_imgproc.rectangle(colorImage, faceData, new Scalar(0, 0, 255, 0));

				Mat capturedface = new Mat(grayImage, faceData);
				opencv_imgproc.resize(capturedface, capturedface, new Size(160, 160));

				IntPointer label = new IntPointer(1);
				DoublePointer confidence = new DoublePointer(1);
				recognizer.predict(capturedface, label, confidence);
				int selection = label.get(0);
				String name;
				if (selection == -1 ) {
					faceRecognizeID= selection;
					name="Unknown";
				} else {
					name= " " + " - " + confidence.get(0);
					faceCaunted++;
					if(faceCaunted>=50){
						faceRecognizeID=selection;

						break;
					}

				}

				int x = Math.max(faceData.tl().x() -10, 0);
				int y = Math.max(faceData.tl().y() -10, 0);
				opencv_imgproc.putText(colorImage, name, new Point(x,y), opencv_core.FONT_HERSHEY_PLAIN, 1.4, new Scalar(0,255,0,0));

			}
			if(!(faceRecognizeID== -1 || faceRecognizeID == 0)){
				System.out.println("faceRecognizeID"+faceRecognizeID);
				break;
			}

			if (cFrame.isVisible()) {
				cFrame.showImage(capturedFrame);
			}

			if (sample > sampleNumber) {
				break;
			}
			if(!cFrame.isActive()){
				camera1.stop();
				break;
			}
		}


		cFrame.dispose();
		camera1.stop();
		camera1.close();
		colorImage.close();
		faceDetector.close();

		return faceRecognizeID;
	}
}