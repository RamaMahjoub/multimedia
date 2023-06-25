package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayImageView implements Page {

    List<ImageView> images = new ArrayList<>();
    private static final int MAX_COLUMNS = 2;
    private static final double IMAGE_SIZE = 200;
    private ImageView selectedImageView;
    private Label selectedImageDimensionsLabel;
    PageSwitcher pageSwitcher;

    public DisplayImageView(PageSwitcher pageSwitcher, List<ImageView> images) {
        this.pageSwitcher = pageSwitcher;
        this.images = images;
    }

    public Parent getView() throws IOException {
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
        selectedImageView = images.get(0);
        selectedImageView.setFitWidth(IMAGE_SIZE);
        selectedImageView.setPreserveRatio(true);

        selectedImageDimensionsLabel = new Label("Image Dimensions");
        selectedImageDimensionsLabel.setText((int) selectedImageView.getImage().getWidth() + " x " +
                (int) selectedImageView.getImage().getHeight());

        VBox left = new VBox();
        left.setPadding(new Insets(10));
        left.setAlignment(Pos.CENTER);
        left.getChildren().addAll(selectedImageView, selectedImageDimensionsLabel);

        GridPane right = new GridPane();
        right.setPadding(new Insets(10));
        right.setHgap(10);
        right.setVgap(10);

        int numImages = images.size();
        int numColumns = Math.min(numImages, MAX_COLUMNS);

        for (int i = 1; i < numImages; i++) {
            ImageView imageView = images.get(i);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setPreserveRatio(true);

            int row = i / numColumns;
            int column = i % numColumns;
            right.add(imageView, column, row);
        }

        root.setLeft(left);
        root.setRight(right);
        root.setBottom(vbox);
        VBox view = new VBox(root);

        return view;
    }

    public void goBack() throws IOException {
        pageSwitcher.switchToPage(new UploadPage(pageSwitcher));
    }
}
