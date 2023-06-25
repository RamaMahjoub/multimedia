package search;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ColorImageSearcher {
    private static final double COLOR_SIMILARITY_THRESHOLD = 0.1;

    public static List<Image> searchImagesByColors(List<Color> targetColors, String folderPath) {
        List<Image> matchingImages = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file)) {
                    Image image = new Image(file.toURI().toString());

                    if (containsColors(image, targetColors)) {
                        matchingImages.add(image);
                    }
                }
            }
        }

        return matchingImages;
    }
    

    private static boolean isImageFile(File file) {
        String filename = file.getName().toLowerCase();
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
    }

    private static boolean containsColors(Image image, List<Color> targetColors) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = image.getPixelReader().getColor(x, y);

                for (Color targetColor : targetColors) {
                    if (areColorsSimilar(pixelColor, targetColor)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean areColorsSimilar(Color color1, Color color2) {
        double redDiff = Math.abs(color1.getRed() - color2.getRed());
        double greenDiff = Math.abs(color1.getGreen() - color2.getGreen());
        double blueDiff = Math.abs(color1.getBlue() - color2.getBlue());

        return redDiff <= COLOR_SIMILARITY_THRESHOLD &&
                greenDiff <= COLOR_SIMILARITY_THRESHOLD &&
                blueDiff <= COLOR_SIMILARITY_THRESHOLD;
    }
}
