package PageSwitcher;

import interfaces.Page;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PageSwitcher {

    private Stage primaryStage;

    public PageSwitcher(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchToPage(Page page) throws IOException {
        
        Scene scene = new Scene(page.getView());
        primaryStage.setScene(scene);
    }

    public File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }
}
