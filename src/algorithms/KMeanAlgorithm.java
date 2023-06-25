package algorithms;

import interfaces.Algorithm;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeanAlgorithm implements Algorithm {

    @Override
    public Image startAlgorithm(Image image, int k) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Read the pixels from the original image
        PixelReader pixelReader = image.getPixelReader();
        int[] pixels = new int[width * height];
        pixelReader.getPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getIntArgbInstance(), pixels, 0, width);

        // Create a list to store the RGB values
        List<Color> colors = initializeCentroids(pixels, k);

        boolean converged = false;

        while (!converged) {
            // Assign each pixel to the nearest centroid
            List<List<Integer>> clusters = assignPixelsToClusters(pixels, colors);

            // Update the centroids
            converged = updateCentroids(pixels, clusters, colors);
        }

        // Create a new image with quantized colors
        WritableImage quantizedImage = createQuantizedImage(width, height, pixels, colors);

        return quantizedImage;
    }

    private static List<Color> initializeCentroids(int[] pixels, int k) {
        List<Color> colors = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < k; i++) {
            int index = random.nextInt(pixels.length);
            int argb = pixels[index];
            Color color = Color.rgb((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);
            colors.add(color);
        }

        return colors;
    }

    private static List<List<Integer>> assignPixelsToClusters(int[] pixels, List<Color> colors) {
        List<List<Integer>> clusters = new ArrayList<>(colors.size());

        for (int i = 0; i < colors.size(); i++) {
            clusters.add(new ArrayList<>());
        }

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            Color pixelColor = Color.rgb((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);

            int nearestIndex = findNearestColorIndex(pixelColor, colors);
            clusters.get(nearestIndex).add(i);
        }

        return clusters;
    }

    private static boolean updateCentroids(int[] pixels, List<List<Integer>> clusters, List<Color> colors) {
        boolean converged = true;

        for (int i = 0; i < clusters.size(); i++) {
            List<Integer> cluster = clusters.get(i);
            int redSum = 0;
            int greenSum = 0;
            int blueSum = 0;

            for (int pixelIndex : cluster) {
                int argb = pixels[pixelIndex];
                redSum += (argb >> 16) & 0xFF;
                greenSum += (argb >> 8) & 0xFF;
                blueSum += argb & 0xFF;
            }

            int clusterSize = cluster.size();
            if (clusterSize > 0) {
                int redMean = redSum / clusterSize;
                int greenMean = greenSum / clusterSize;
                int blueMean = blueSum / clusterSize;

                Color newColor = Color.rgb(redMean, greenMean, blueMean);
                if (!colors.get(i).equals(newColor)) {
                    colors.set(i, newColor);
                    converged = false;
                }
            }
        }

        return converged;
    }

    private static int findNearestColorIndex(Color color, List<Color> colors) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = 0; i < colors.size(); i++) {
            Color centroid = colors.get(i);
            double distance = getColorDistance(color, centroid);

            if (distance < nearestDistance) {
                nearestIndex = i;
                nearestDistance = distance;
            }
        }

        return nearestIndex;
    }

    private static double getColorDistance(Color color1, Color color2) {
        double redDiff = color1.getRed() - color2.getRed();
        double greenDiff = color1.getGreen() - color2.getGreen();
        double blueDiff = color1.getBlue() - color2.getBlue();

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

    private static WritableImage createQuantizedImage(int width, int height, int[] pixels, List<Color> colors) {
        WritableImage quantizedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = quantizedImage.getPixelWriter();

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            Color pixelColor = Color.rgb((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);

            int nearestIndex = findNearestColorIndex(pixelColor, colors);
            Color nearestColor = colors.get(nearestIndex);

            pixelWriter.setColor(i % width, i / width, nearestColor);
        }

        return quantizedImage;
    }
}
