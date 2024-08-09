package org.siyaka;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class Main {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);
        TakePhoto takePhoto = new TakePhoto();
        if(takePhoto.finishRunPhoto()) {
            int absoluteFaceSize = 0;
            //Reading the Image from the file and storing it in to a Matrix object
            String file = "src/main/resources/test-images/test-blur.jpg";
            Mat src = Imgcodecs.imread(file);
            if (src.empty()) {
                System.out.println("Could not open or find the image!");
                throw new RuntimeException("No image to work with!!!");
            }

            //Instantiating the CascadeClassifier
            String xmlFile = "src/main/resources/standard-xml/haarcascade_frontalface_alt.xml";
            CascadeClassifier classifier = new CascadeClassifier(xmlFile);

            //Detecting the face in the snap
            MatOfRect faceDetections = new MatOfRect();
            Mat grayFrame = new Mat();
//        classifier.detectMultiScale(src, faceDetections);
            Imgproc.cvtColor(src, grayFrame, Imgproc.COLOR_BGR2GRAY);

            // Apply the Laplacian operator
            Mat laplacianImage = new Mat();
            Imgproc.Laplacian(grayFrame, laplacianImage, CvType.CV_64F);

            // Compute the variance of the Laplacian
            MatOfDouble mean = new MatOfDouble();
            MatOfDouble stdDev = new MatOfDouble();
            Core.meanStdDev(laplacianImage, mean, stdDev);

            double variance = stdDev.get(0, 0)[0] * stdDev.get(0, 0)[0];

            // Threshold for blurriness (this value can be adjusted), it can be adjusted based on camera quality and lighting available
            double threshold = 100.0;
            System.out.println("blur threshold = " + variance);

            if (variance < threshold) {
                System.out.println("The image is blurry.");
            } else {
                System.out.println("The image is not blurry.");
            }


            Imgproc.equalizeHist(grayFrame, grayFrame);
            if (absoluteFaceSize == 0) {
                int height = grayFrame.rows();
                if (Math.round(height * 0.2f) > 0) {
                    absoluteFaceSize = Math.round(height * 0.2f);
                }
            }
            classifier.detectMultiScale(grayFrame, faceDetections, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(absoluteFaceSize, absoluteFaceSize), new Size());


            System.out.println(String.format("Detected %s faces",
                    faceDetections.toArray().length));

            //Drawing boxes
            for (Rect rect : faceDetections.toArray()) {
                Imgproc.rectangle(src,       //where to draw the box
                        new Point(rect.x, rect.y),   //bottom left
                        new Point(rect.x + rect.width, rect.y + rect.height),  //top right
                        new Scalar(0, 0, 255),
                        3);    //RGB color
            }
            //Writing the image
            Imgcodecs.imwrite("facedetect_output1.jpg", src);

            System.out.println("Image Processed");
        }
    }
}