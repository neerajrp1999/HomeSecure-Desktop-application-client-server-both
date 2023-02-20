package recognition;

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
import org.example.client;
import org.example.server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	public static void main(String[] args){
		new Recognition().Call_Recognizer();
	}
	public synchronized void Call_Recognizer(){

		new Thread(new Runnable() {
			public void run() {
				int RecognizeId=-1;
				try {
					RecognizeId=new Recognition().Recognize();
					System.out.println(RecognizeId);
					afterRecognazation(RecognizeId);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		}).start();
	}
	public synchronized void Call_Admin_Recognizer(server s){

		new Thread(new Runnable() {
			public void run() {
				int RecognizeId=-1;
				try {
					RecognizeId=new Recognition().Recognize();
					System.out.println(RecognizeId);
					if(RecognizeId!=1){
						JOptionPane pane = new JOptionPane("You are not admin !!");
						s.callJFrame_server();
					}else{
						s.callJFrame_server_page2();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		}).start();
	}
	private void afterRecognazation(int RecognizeId) {
		try{
			if(RecognizeId==-1 || RecognizeId==0){
				JOptionPane pane = new JOptionPane("Invalid face !!");
				final JDialog dialog = pane.createDialog("Error");
				Timer timer = new Timer(3000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.setVisible(false);
					}
				});
				timer.setRepeats(false);
				timer.start();
				dialog.setVisible(true);
				new client().callFrame();
			}else{
				String pass = JOptionPane.showInputDialog("Hi "+people[RecognizeId]+"!!\nGive Password:");
				if (pass.equals("yes")) {
					//JOptionPane.showMessageDialog(null, "welome to home!\nOpening Door.", "Gas", JOptionPane.INFORMATION_MESSAGE);
					//new client().callFrame();

					JOptionPane pane = new JOptionPane("Welcome !!");
					final JDialog dialog = pane.createDialog("Welome to home!\nOpening Door.");
					Timer timer = new Timer(5000, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
						}
					});
					timer.setRepeats(false);
					timer.start();
					dialog.setVisible(true);
					new client().callFrame();
				}else{
					JOptionPane pane = new JOptionPane("Invalid !!");
					final JDialog dialog = pane.createDialog("Error");
					Timer timer = new Timer(3000, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dialog.setVisible(false);
						}
					});
					timer.setRepeats(false);
					timer.start();
					dialog.setVisible(true);
					new client().callFrame();
				}
			}
			//JOptionPane.showMessageDialog(this, "Invalide face!!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e){

		}

	}
	OpenCVFrameConverter.ToMat convertMat;
	OpenCVFrameGrabber camera1;
	String[] people = {"", "Neeraj", "Suraj", "Priscila","Neeraj"};
	CascadeClassifier faceDetector;
	FaceRecognizer recognizer;
	CanvasFrame cFrame;
	public synchronized int Recognize() throws Exception {
		convertMat = new OpenCVFrameConverter.ToMat(); // convert image to Mat
		camera1 = new OpenCVFrameGrabber(0); // capturing webcam images

		camera1.start();
		faceDetector = new CascadeClassifier("src\\resources\\haarcascades\\haarcascade_frontalface_alt.xml");

		//FaceRecognizer recognizer = createEigenFaceRecognizer();  // classifier
		//recognizer.load("src\\resources\\classifierEigenFaces.yml");
		// recognizer.setThreshold(8000);      // trust number

		//FaceRecognizer recognizer = createFisherFaceRecognizer();
		//recognizer.load("src\\resources\\classifierFisherFaces.yml");

		recognizer = createLBPHFaceRecognizer();
		recognizer.load("src\\resources\\classifierLBPH.yml");
		recognizer.setThreshold(65.0);

		cFrame = new CanvasFrame("Recognition", CanvasFrame.getDefaultGamma() / camera1.getGamma()); // drawing a window


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
					name= people[selection] + " - " + confidence.get(0);
					faceCaunted++;
					if(faceCaunted>=50){
						faceRecognizeID=selection;

						break;
					}

				}

				// entering the name on the screen
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


		cFrame.dispose(); // free memory
		camera1.stop();
		camera1.close();
		colorImage.close();
		faceDetector.close();

		return faceRecognizeID;
	}
}