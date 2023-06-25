package search;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DateImageSearcher {

    public static List<Image> searchImagesByDate(LocalDate date, String folderPath) throws IOException {
        List<Image> matchingImages = new ArrayList<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file)) {
                    Image image = new Image(file.toURI().toString());
                    LocalDate date2 = getFileDateAsLocalDate(file);
                    if (compareDate(date, date2)) {
                        matchingImages.add(image);
                    }
                }
            }
        }

        return matchingImages;
    }

    public static boolean compareDate(LocalDate date1, LocalDate date2) {
        if (date2.compareTo(date1) == 0) return true;
        return false;
    }

    private static LocalDate getFileDateAsLocalDate(File file) {
        long lastModified = file.lastModified();
        Instant instant = Instant.ofEpochMilli(lastModified);
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static boolean isImageFile(File file) {
        String filename = file.getName().toLowerCase();
        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
    }
}
