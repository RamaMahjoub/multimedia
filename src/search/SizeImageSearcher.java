package search;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SizeImageSearcher {
    public static List<Image> searchImagesBySize(String width, String height, String folderPath) throws IOException {
        List<Image> matchingImages = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file)) {
                    Image image = new Image(file.toURI().toString());
                    double fileWidth = image.getWidth();
                    double fileHeight = image.getHeight();
                    double orginalWidth = Double.parseDouble(width);
                    double orginalHeight = Double.parseDouble(height);
                    if (compareSize(fileWidth, fileHeight, orginalWidth,orginalHeight)) {
                        matchingImages.add(image);
                    }
                }
            }
        }

        return matchingImages;
    }

    public static boolean compareSize(double fileWidth, double fileHeight, double orginalWidth, double orginalHeight) {
        if (fileWidth == orginalWidth && fileHeight == orginalHeight) return true;
        return false;
    }
    private static boolean isImageFile(File file) {
        String filename = file.getName().toLowerCase();
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
    }
}
