package lukas.wais.smart.mirror.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.joda.time.DateTime;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

/**
 * Class for detecting if a person is present
 */
public class ImageDetection {
    /** boolean that is true*/
    public static boolean detected;
    /** */
    private static VideoCapture capture;
    private static DateTime lastDetected = DateTime.now();
    private static long timeout = 4000;
    private static CascadeClassifier cascadeClassifier;

    public static Mat loadImage(String path){
        Imgcodecs imgcodecs = new Imgcodecs();
        return  imgcodecs.imread(path);
    }

    public static void init(CascadeClassifier c, VideoCapture vc){
        cascadeClassifier = c;
        capture = vc;
    }

    public static Image mat2Img(Mat m) {
        if (!m.empty()) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (m.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = m.channels() * m.cols() * m.rows();
            byte[] b = new byte[bufferSize];
            m.get(0, 0, b); // get all the pixels
            BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);

            return convertToFxImage(image);
        }

        return null;

    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
    public static Mat detectFace(Mat inputImage) {
        MatOfRect facesDetected = new MatOfRect();
        int minFaceSize = Math.round(inputImage.rows() * 0.1f);
        cascadeClassifier.detectMultiScale(inputImage,
                facesDetected,
                1.1,
                3,
                Objdetect.CASCADE_SCALE_IMAGE,
                new Size(minFaceSize, minFaceSize),
                new Size()
        );
        Rect[] facesArray =  facesDetected.toArray();
        if(facesArray.length>0){
            detected = true;
            lastDetected = DateTime.now();
        }else if((DateTime.now().getMillis()- lastDetected.getMillis())>timeout) {
            detected = false;
        }
        for(Rect face : facesArray) {
            Imgproc.rectangle(inputImage, face.tl(), face.br(), new Scalar(0, 0, 255), 3 );
        }
        return inputImage;
    }

    public static Image getCapture() {
        Mat mat = new Mat();
        capture.read(mat);
        return mat2Img(mat);
    }
    public static Image getCaptureWithFaceDetection() {
        Mat mat = new Mat();
        capture.read(mat);
        Mat haarClassifiedImg = detectFace(mat);
        return mat2Img(haarClassifiedImg);
    }

}
