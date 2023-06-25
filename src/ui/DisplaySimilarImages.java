package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import search.SimilarImagesFinder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplaySimilarImages implements Page {
    List<Image> images = new ArrayList<>();
    private static final int MAX_COLUMNS = 3;
    private static final double IMAGE_SIZE = 200;

    PageSwitcher pageSwitcher ;
    public DisplaySimilarImages(PageSwitcher pageSwitcher) throws IOException {
        this.pageSwitcher = pageSwitcher;
        images = SimilarImagesFinder.findSimilarImages();
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
        for (int i = 0; i < numImages; i++) {
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
