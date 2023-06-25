package algorithms;

import interfaces.Algorithm;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class Simple implements Algorithm {
    @Override
    public Image startAlgorithm(Image image, int k) {
        return simple(image);
    }

    public Image simple(Image image) {
//        int Mask3 = 0x00800000;
        // int Mask3 = 0xff808080;
        int Mask3 = 0xffc0c0c0;
        // int Mask3 = 0xffe0e0e0;
        // int Mask3 = 0xfff0f0f0;
        BufferedImage originalImage = SwingFXUtils.fromFXImage(image, null);
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                newImage.setRGB(x, y, originalImage.getRGB(x, y) & Mask3);
            }
        }
        Image result = SwingFXUtils.toFXImage(newImage, null);
        return result;
    }
}