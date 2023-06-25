package colorPlate;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ColorPlateGenerator {


    private static final int NUM_COLORS = 10; // Number of colors to display
    private double WIDTH, HEIGHT;

    private Image quantizedImage;

    public ColorPlateGenerator(Image image, double WIDTH, double HEIGHT) {
        this.quantizedImage = image;
        this.HEIGHT = HEIGHT;
        this.WIDTH = WIDTH;
    }

    public Pane generateColorPlate() {
        HBox colorPlate = new HBox();
        colorPlate.setPrefSize(WIDTH, HEIGHT);
        // Extract dominant colors from the quantized image
        List<Color> dominantColors = extractDominantColors(quantizedImage, NUM_COLORS);

        // Create a color plate using rectangles
        for (int i = 0; i < dominantColors.size(); i++) {
            Rectangle colorRectangle = new Rectangle(WIDTH / NUM_COLORS, HEIGHT);
            colorRectangle.setFill(dominantColors.get(i));
            colorPlate.getChildren().add(colorRectangle);
        }

        return colorPlate;
    }
   


    private List<Color> extractDominantColors(Image image, int numColors) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Count the occurrences of each color
        int[][][] colorCounts = new int[256][256][256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = image.getPixelReader().getColor(x, y);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);
                colorCounts[red][green][blue]++;
            }
        }

        // Find the most frequent colors
        List<Color> dominantColors = new ArrayList<>();
        for (int i = 0; i < numColors; i++) {
            int maxCount = 0;
            Color dominantColor = Color.WHITE; // Default color if no dominant color found
            for (int r = 0; r < 256; r++) {
                for (int g = 0; g < 256; g++) {
                    for (int b = 0; b < 256; b++) {
                        if (colorCounts[r][g][b] > maxCount) {
                            maxCount = colorCounts[r][g][b];
                            dominantColor = Color.rgb(r, g, b);
                        }
                    }
                }
            }
            dominantColors.add(dominantColor);
            // Set the count of the dominant color to zero to find the next most frequent color
            colorCounts[(int) (dominantColor.getRed() * 255)][(int) (dominantColor.getGreen() * 255)][(int) (dominantColor.getBlue() * 255)] = 0;
        }

        return dominantColors;
    }

    
}
