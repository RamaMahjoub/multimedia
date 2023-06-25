package ui;

import EditedClasses.Image;
import PageSwitcher.PageSwitcher;
import algorithms.KMeanAlgorithm;
import algorithms.MedianCut;
import algorithms.Simple;
import colors.ColorPlateGenerator;
import indexedImage.ImageToIndexedConverter;
import interfaces.Algorithm;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayPage implements Page {
    PageSwitcher pageSwitcher;

    private ArrayList<Image> images = new ArrayList<>();
    int algorithmsNumber;
    //    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    double fitWidth = 600;
    double fitHeight = 600;

    public DisplayPage(PageSwitcher pageSwitcher, String imageUrl) {

        this.pageSwitcher = pageSwitcher;
        File directory = new File("D:\\4th\\multimedia\\javaFx\\src\\algorithms");
        File[] files = directory.listFiles();
        algorithmsNumber = files.length;

        fitWidth /= Math.max(1, algorithmsNumber);
        fitHeight /= Math.max(1, algorithmsNumber);

        Algorithm[] algorithm = new Algorithm[algorithmsNumber];
        algorithm[0] = new KMeanAlgorithm();
        algorithm[1] = new MedianCut();
        algorithm[2] = new Simple();
        int[] k = new int[algorithmsNumber];
        k[0] = 10;
        k[1] = 256;
        k[2] = 256;
        for (int i = 0; i < algorithmsNumber; i++) {
            Image image = new Image(imageUrl);
            image.setImage(algorithm[i].startAlgorithm(image.getImage(), k[i]));
            images.add(image);
            ImageToIndexedConverter.convertToIndexed(image.getImage(), "D:\\4th\\multimedia\\javaFx\\savedImage/" + (i + 1) + "Image.png");
        }

    }


    public Parent getView() {
        BorderPane root = new BorderPane();
        Button goBack = new Button("back");
        goBack.setPadding(new Insets(10));
        goBack.setStyle("-fx-text-fill: white; -fx-background-color: #ff6f00;");
        goBack.setOnAction(e -> {
            try {
                goBack();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox vbox = new VBox();
        VBox.setMargin(goBack, new Insets(5)); // Set margin specifically for the button
        vbox.getChildren().add(goBack);
        ArrayList<ImageView> imagesView = new ArrayList<>();
        Pane[] colorPlate = new Pane[algorithmsNumber];

        for (int i = 0; i < algorithmsNumber; i++) {
            ColorPlateGenerator colorPlateGenerator = new ColorPlateGenerator(images.get(i).getImage(), fitWidth * 2, fitHeight);
            colorPlate[i] = colorPlateGenerator.generateColorPlate();

            ImageView imageView = new ImageView(images.get(i).getImage());
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
            imagesView.add(imageView);
        }


        // Create a VBox to stack the image views vertically
        VBox imageContainer = new VBox();
        imageContainer.getChildren().addAll(imagesView);

        VBox colorPlateContainer = new VBox();
        colorPlateContainer.setMinHeight(fitHeight);
        colorPlateContainer.getChildren().addAll(colorPlate);

        root.setBottom(vbox);
        root.setLeft(imageContainer);
        root.setRight(colorPlateContainer);

        return root;
    }

    public void goBack() throws IOException {
        pageSwitcher.switchToPage(new UploadPage(pageSwitcher));
    }
}
