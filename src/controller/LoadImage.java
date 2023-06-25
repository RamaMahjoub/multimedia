package controller;

import PageSwitcher.PageSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.*;

import java.io.File;
import java.io.IOException;

public class LoadImage {

    private PageSwitcher pageSwitcher;

    public LoadImage(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }

    @FXML
    Button chooseImageButton;

    @FXML
    Button searchImage;

    @FXML
    private void upload() throws IOException {
        File selectedFile = pageSwitcher.showFileChooser();
        if (selectedFile != null) {
            DisplayPage displayPage = new DisplayPage(pageSwitcher,selectedFile.toURI().toString());
            pageSwitcher.switchToPage(displayPage);
        }
        return;
    }

    @FXML
    private void search() throws IOException {
        DisplaySimilarImages displaySimilarImages = new DisplaySimilarImages(pageSwitcher);
        pageSwitcher.switchToPage(displaySimilarImages);
    }


    @FXML
    private void resizeImage() throws IOException {
        ImageResized resizeImage = new ImageResized(pageSwitcher);
        pageSwitcher.switchToPage(resizeImage);
    }

    @FXML
    private void useColors() throws IOException {
        ColorSelector colorSelector = new ColorSelector(pageSwitcher);
        pageSwitcher.switchToPage(colorSelector);
    }

    @FXML
    private void useDate() throws IOException {
        DateSelector dateSelector = new DateSelector(pageSwitcher);
        pageSwitcher.switchToPage(dateSelector);
    }

    @FXML
    private void useSize() throws IOException {
        SizeSelector sizeSelector = new SizeSelector(pageSwitcher);
        pageSwitcher.switchToPage(sizeSelector);
    }
}
