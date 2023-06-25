package threads;

import javafx.scene.image.Image;
import search.DateImageSearcher;
import search.SizeImageSearcher;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;

public class SizeThreads implements Callable<List<Image>> {

    String path;
    String width;
    String height;
    public SizeThreads(String path, String width, String height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }
    @Override
    public List<Image> call() throws Exception {
        return SizeImageSearcher.searchImagesBySize(width, height, path);
    }
}