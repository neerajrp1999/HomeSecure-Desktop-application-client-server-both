package clent1.recognition;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_face.*;

public class Training {

	public static void main(String[] args) {
		File directory = new File ("src\\resources\\photos");

		FilenameFilter imageFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") ;
			}
		}; 

		File[] files = directory.listFiles(imageFilter);
		MatVector photos = new MatVector(files.length);
		Mat labels = new Mat(files.length, 1 , opencv_core.CV_32SC1);
		IntBuffer bufferLabels = labels.createBuffer();
		int counter = 0;

		for( File image : files) {
			Mat photo = opencv_imgcodecs.imread(image.getAbsolutePath(), opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			int personId = Integer.parseInt(image.getName().split("\\.")[1]);

			opencv_imgproc.resize(photo, photo, new Size(160,160));
			photos.put(counter, photo);
			bufferLabels.put(counter, personId);
			System.out.println("fiel"+counter);
			counter++;
		}

		//classifiers
		FaceRecognizer eigenFaces = createEigenFaceRecognizer();
		FaceRecognizer fisherFaces = createFisherFaceRecognizer();
		FaceRecognizer lbph = createLBPHFaceRecognizer();
		System.out.println("fiel");

		// classifiers -> learning-training to generate yml codes
		/*
		eigenFaces.train(photos, labels);
		eigenFaces.save("src\\resources\\classifierEigenFaces.yml");
		System.out.println("1");
		fisherFaces.train(photos, labels);
		fisherFaces.save("src\\resources\\classifierFisherFaces.yml");
		System.out.println("2");
		*/
		lbph.train(photos, labels);
		lbph.save("src\\resources\\classifierLBPH.yml");
		System.out.println("done");
	}
}