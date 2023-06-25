package indexedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

public class ImageToIndexedConverter {
    public static void convertToIndexed(Image sourceImage, String indexedImagePath) {
        try {

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(sourceImage, null);
       
            // Convert source image to indexed image
            BufferedImage indexedImage = createIndexedBufferedImage(bufferedImage);

            // Save the indexed image
            ImageIO.write(indexedImage, "png", new File(indexedImagePath));

            System.out.println("Indexed image saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static BufferedImage createIndexedBufferedImage(BufferedImage sourceImage) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        // Create indexed image with 256 colors (8-bit color depth)
        BufferedImage indexedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);

        // Create color palette for indexed image
        Color[] palette = new Color[256];
        for (int i = 0; i < 256; i++) {
            palette[i] = new Color(i, i, i);
        }
        indexedImage = applyColorPalette(indexedImage, palette);

        // Copy the source image to the indexed image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = sourceImage.getRGB(x, y);
                indexedImage.setRGB(x, y, rgb);
            }
        }

        return indexedImage;
    }

    private static BufferedImage applyColorPalette(BufferedImage image, Color[] palette) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Create color index lookup table
        byte[] colorIndex = new byte[256];
        for (int i = 0; i < 256; i++) {
            colorIndex[i] = (byte) i;
        }

        // Create color model with the given palette
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        for (int i = 0; i < 256; i++) {
            reds[i] = (byte) palette[i].getRed();
            greens[i] = (byte) palette[i].getGreen();
            blues[i] = (byte) palette[i].getBlue();
        }
        ColorModel colorModel = new IndexColorModel(8, 256, reds, greens, blues, 0);

        // Create a compatible writable raster
        BufferedImage indexedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, (IndexColorModel) colorModel);
        indexedImage.setData(image.getRaster());

        return indexedImage;
    }
}