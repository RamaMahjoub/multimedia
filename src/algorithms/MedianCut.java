package algorithms;

import interfaces.Algorithm;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MedianCut implements Algorithm {

    @Override
    public Image startAlgorithm(Image image, int numColors) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Read the pixels from the original image
        PixelReader pixelReader = image.getPixelReader();
        int[] pixels = new int[width * height];
        pixelReader.getPixels(0, 0, width, height, javafx.scene.image.PixelFormat.getIntArgbInstance(), pixels, 0, width);

        // Create a list to store the RGB values
        List<Color> colorList = new ArrayList<>();

        // Add all the colors from the image to the list
        for (int argb : pixels) {
            Color color = Color.rgb((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);
            colorList.add(color);
        }

        // Create the initial color cube
        ColorCube colorCube = new ColorCube(colorList);

        // Perform the Median Cut algorithm
        List<Color> quantizedColors = performMedianCut(colorCube, numColors);

        // Create a new image with quantized colors
        WritableImage quantizedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = quantizedImage.getPixelWriter();
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
            Color pixelColor = Color.rgb((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);

            Color nearestColor = findNearestColor(pixelColor, quantizedColors);

            pixelWriter.setColor(i % width, i / width, nearestColor);
        }

        return quantizedImage;
    }

    private List<Color> performMedianCut(ColorCube colorCube, int numColors) {
        List<Color> quantizedColors = new ArrayList<>();
        List<ColorCube> cubes = new ArrayList<>();
        cubes.add(colorCube);

        while (quantizedColors.size() < numColors && !cubes.isEmpty()) {
            // Sort the cubes by volume (size)
            cubes.sort(Comparator.comparingInt(ColorCube::getVolume).reversed());

            // Take the largest cube and split it along the longest dimension
            ColorCube largestCube = cubes.remove(0);
            List<ColorCube> subCubes = largestCube.splitCube();
            cubes.addAll(subCubes);

            // Remove any cubes that are smaller than the desired number of colors
            cubes.removeIf(cube -> cube.getVolume() < numColors - quantizedColors.size());

            // Get the average color of each split cube and add it to the quantized colors
            for (ColorCube cube : subCubes) {
                quantizedColors.add(cube.getAverageColor());
            }
        }

        return quantizedColors.subList(0, Math.min(numColors, quantizedColors.size()));
    }


    private Color findNearestColor(Color targetColor, List<Color> colors) {
        Color nearestColor = colors.get(0);
        double nearestDistance = getColorDistance(targetColor, nearestColor);

        for (Color color : colors) {
            double distance = getColorDistance(targetColor, color);
            if (distance < nearestDistance) {
                nearestColor = color;
                nearestDistance = distance;
            }
        }

        return nearestColor;
    }

    private double getColorDistance(Color color1, Color color2) {
        double redDiff = color1.getRed() - color2.getRed();
        double greenDiff = color1.getGreen() - color2.getGreen();
        double blueDiff = color1.getBlue() - color2.getBlue();

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

    private static class ColorCube {
        private final List<Color> colors;
        private double minRed, maxRed;
        private double minGreen, maxGreen;
        private double minBlue, maxBlue;

        public ColorCube(List<Color> colors) {
            this.colors = colors;
            initializeBounds();
        }

        public int getVolume() {
            return (int) ((maxRed - minRed + 1) * (maxGreen - minGreen + 1) * (maxBlue - minBlue + 1));
        }

        public Color getAverageColor() {
            double totalRed = 0, totalGreen = 0, totalBlue = 0;
            for (Color color : colors) {
                totalRed += color.getRed();
                totalGreen += color.getGreen();
                totalBlue += color.getBlue();
            }

            int numColors = colors.size();
            double averageRed = totalRed / numColors;
            double averageGreen = totalGreen / numColors;
            double averageBlue = totalBlue / numColors;

            return Color.color(averageRed, averageGreen, averageBlue);
        }

        public List<ColorCube> splitCube() {
            List<Color> sortedColors = new ArrayList<>(colors);
            sortedColors.sort(Comparator.comparingDouble(Color::getRed));

            int mid = sortedColors.size() / 2;
            List<Color> leftColors = sortedColors.subList(0, mid);
            List<Color> rightColors = sortedColors.subList(mid, sortedColors.size());

            double splitRed = (leftColors.get(leftColors.size() - 1).getRed() + rightColors.get(0).getRed()) / 2.0;

            ColorCube leftCube = createSubCube(leftColors, minRed, splitRed, minGreen, maxGreen, minBlue, maxBlue);
            ColorCube rightCube = createSubCube(rightColors, splitRed, maxRed, minGreen, maxGreen, minBlue, maxBlue);

            List<ColorCube> subCubes = new ArrayList<>();
            subCubes.add(leftCube);
            subCubes.add(rightCube);

            sortedColors = new ArrayList<>(colors);
            sortedColors.sort(Comparator.comparingDouble(Color::getGreen));

            mid = sortedColors.size() / 2;
            leftColors = sortedColors.subList(0, mid);
            rightColors = sortedColors.subList(mid, sortedColors.size());

            double splitGreen = (leftColors.get(leftColors.size() - 1).getGreen() + rightColors.get(0).getGreen()) / 2.0;

            leftCube = createSubCube(leftColors, minRed, maxRed, minGreen, splitGreen, minBlue, maxBlue);
            rightCube = createSubCube(rightColors, minRed, maxRed, splitGreen, maxGreen, minBlue, maxBlue);

            subCubes.add(leftCube);
            subCubes.add(rightCube);

            sortedColors = new ArrayList<>(colors);
            sortedColors.sort(Comparator.comparingDouble(Color::getBlue));

            mid = sortedColors.size() / 2;
            leftColors = sortedColors.subList(0, mid);
            rightColors = sortedColors.subList(mid, sortedColors.size());

            double splitBlue = (leftColors.get(leftColors.size() - 1).getBlue() + rightColors.get(0).getBlue()) / 2.0;

            leftCube = createSubCube(leftColors, minRed, maxRed, minGreen, maxGreen, minBlue, splitBlue);
            rightCube = createSubCube(rightColors, minRed, maxRed, minGreen, maxGreen, splitBlue, maxBlue);

            subCubes.add(leftCube);
            subCubes.add(rightCube);

            return subCubes;
        }

        private ColorCube createSubCube(List<Color> colors, double minRed, double maxRed, double minGreen, double maxGreen, double minBlue, double maxBlue) {
            ColorCube subCube = new ColorCube(colors);
            subCube.minRed = minRed;
            subCube.maxRed = maxRed;
            subCube.minGreen = minGreen;
            subCube.maxGreen = maxGreen;
            subCube.minBlue = minBlue;
            subCube.maxBlue = maxBlue;
            return subCube;
        }

        private void initializeBounds() {
            minRed = Double.MAX_VALUE;
            maxRed = Double.MIN_VALUE;
            minGreen = Double.MAX_VALUE;
            maxGreen = Double.MIN_VALUE;
            minBlue = Double.MAX_VALUE;
            maxBlue = Double.MIN_VALUE;

            for (Color color : colors) {
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                minRed = Math.min(minRed, red);
                maxRed = Math.max(maxRed, red);
                minGreen = Math.min(minGreen, green);
                maxGreen = Math.max(maxGreen, green);
                minBlue = Math.min(minBlue, blue);
                maxBlue = Math.max(maxBlue, blue);
            }
        }
    }
}
 