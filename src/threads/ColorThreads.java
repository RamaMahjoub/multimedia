package threads;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import search.ColorImageSearcher;

import java.util.List;
import java.util.concurrent.Callable;

public class ColorThreads implements Callable<List<Image>> {

    String path;
    List <Color> color;
    public ColorThreads(String path, List <Color> color) {
        this.path = path;
        this.color = color;
    }
    @Override
    public List<Image> call() throws Exception {
        return ColorImageSearcher.searchImagesByColors(color, path);
    }
    
}
