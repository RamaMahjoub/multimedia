package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DisplayColorImageFinder implements Page {
    PageSwitcher pageSwitcher;
    List<Image> images = new ArrayList<>();

    private static final int MAX_COLUMNS = 3; // Maximum number of columns in the grid
    private static final double IMAGE_SIZE = 200;
    public DisplayColorImageFinder(PageSwitcher pageSwitcher, List<Image> images) {
        this.pageSwitcher = pageSwitcher;
        this.images = images;
    }

    public Parent getView() {
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
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int numImages = images.size();
        int numColumns = Math.min(numImages, MAX_COLUMNS);
        BorderPane root = new BorderPane();
        VBox selectedImage = new VBox();
        ArrayList<ImageView> imagesView = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {

            ImageView imageView = new ImageView(images.get(i));
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setPreserveRatio(true);

            int row = i / numColumns;
            int column = i % numColumns;
            gridPane.add(imageView, column, row);
        }

        selectedImage.getChildren().add(gridPane);
        root.setLeft(selectedImage);
        root.setBottom(vbox);
        return root;
    }
    public void goBack() throws IOException {
        pageSwitcher.switchToPage(new UploadPage(pageSwitcher));
    }
}