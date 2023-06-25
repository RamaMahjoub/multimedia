import PageSwitcher.PageSwitcher;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.UploadPage;

import java.io.IOException;

public class Main extends Application {

    
    public void start(Stage primaryStage) throws IOException {
        
        // Create the PageSwitcher instance
        PageSwitcher pageSwitcher = new PageSwitcher(primaryStage);


        // Switch to the first page and pass the ImageView instance
        pageSwitcher.switchToPage(new UploadPage(pageSwitcher));

        // Set the primary stage properties
        primaryStage.setTitle("MultiMedia Project");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
