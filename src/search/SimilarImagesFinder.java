package search;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimilarImagesFinder {
    private static final double SIMILARITY_THRESHOLD = 0.1; // Adjust this threshold as needed

    public static List<Image> findSimilarImages() throws IOException {
        List<Image> similarImages = new ArrayList<>();

        File selectedImageFile = chooseImageFile();
        if (selectedImageFile != null) {
            BufferedImage selectedImageBI = ImageIO.read(selectedImageFile);
            double[] chosenHistogram = calculateColorHistogram(selectedImageBI);
            File directory = selectedImageFile.getParentFile(); // Assuming the images are in the same directory
            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isFile() && !file.equals(selectedImageFile) && hasImageExtension(file)) {
                    BufferedImage image = ImageIO.read(file);
                    double[] currentHistogram = calculateColorHistogram(image);
                    double similarity = compareHistograms(chosenHistogram, currentHistogram);;
                    if (similarity >= SIMILARITY_THRESHOLD) {
                        Image similarImage = new Image(file.toURI().toString());
                        similarImages.add(similarImage);
                    }
                }
            }
        }
        return similarImages;
    }


     public static boolean hasImageExtension(File file) {
        String filename = file.getName();
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        // List of recognized image file extensions
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};

        for (String imageExtension : imageExtensions) {
            if (extension.equals(imageExtension)) {
                return true;
            }
        }

        return false;
    }

    private static double[] calculateColorHistogram(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int numPixels = width * height;

        double[] histogram = new double[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (red + green + blue) / 3;
                histogram[gray]++;
            }
        }

        // Normalize histogram for better comparison
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] /= numPixels;
        }

        return histogram;
    }

    private static double compareHistograms(double[] hist1, double[] hist2) {
        double distance = 0;
        for (int i = 0; i < hist1.length; i++) {
            distance += Math.sqrt(hist1[i] * hist2[i]);
        }
        return distance;
    }
    private static File chooseImageFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

}
