package threads;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import search.ColorImageSearcher;
import search.DateImageSearcher;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;

public class DateThreads implements Callable<List<Image>> {

    String path;
    LocalDate date;
    public DateThreads(String path, LocalDate date) {
        this.path = path;
        this.date = date;
    }
    @Override
    public List<Image> call() throws Exception {
        return DateImageSearcher.searchImagesByDate(date, path);
    }
}
