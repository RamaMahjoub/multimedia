package ui;

import ImageResize.ImageResize;
import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageResized implements Page {
    static List<ImageView> images = new ArrayList<>();
    static PageSwitcher pageSwitcher;
    public ImageResized(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }
    private Button selectImageButton;
    static String wi;
    static String hei;
    static TextField width;
    static TextField height;

    public Parent getView() throws IOException {
        width = new TextField();
        width.setPromptText("new width:");
        wi = width.getText();
        height = new TextField();
        height.setPromptText("new height:");
        hei = height.getText();
        System.out.println("hhhhhhhhhhhh" + height.getText());
        selectImageButton = new Button("Select Image");
        selectImageButton.setOnAction(e -> {
            try {
                chooseImageFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox left = new VBox();
        left.setPadding(new Insets(10));
        left.setAlignment(Pos.CENTER);
        left.getChildren().addAll(width, height, selectImageButton);

//
        return left;
    }

    private static void chooseImageFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
//        System.out.println("hhhhhhhhhhhh" + height.getText());
        images = ImageResize.findSimilarImages(fileChooser.showOpenDialog(null), width.getText(),height.getText());
        DisplayImageView displayImageView = new DisplayImageView(pageSwitcher, images);
        pageSwitcher.switchToPage(displayImageView);
//        return fileChooser.showOpenDialog(null);
    }
}