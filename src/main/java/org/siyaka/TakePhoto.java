package org.siyaka;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class TakePhoto {
    public boolean finishRunPhoto(){
        // Open the default camera (usually the first camera)
        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.out.println("Error: Camera is not available.");
        }

        // Capture a frame from the camera
        Mat frame = new Mat();
        if (camera.read(frame)) {
            // Define the file path where the image will be saved
            String filePath = "src/main/resources/captured/photo.jpeg";

            // Save the captured frame to a file
            if (Imgcodecs.imwrite(filePath, frame)) {
                System.out.println("Photo saved successfully at " + filePath);
            } else {
                System.out.println("Error: Failed to save the photo.");
            }
        } else {
            System.out.println("Error: Failed to capture a frame from the camera.");
        }

        // Release the camera
        camera.release();
        return true;
    }
}
