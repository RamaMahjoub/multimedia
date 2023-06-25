package ui;

import PageSwitcher.PageSwitcher;
import controller.LoadImage;
import interfaces.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class UploadPage implements Page {

    private PageSwitcher pageSwitcher;

    public UploadPage(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }

    public Parent getView() {
        

        Parent root = null;
        try {  
         
         FXMLLoader loader = new FXMLLoader(getClass().getResource("loadImage.fxml"));
         LoadImage loadImage = new LoadImage(pageSwitcher);
         loader.setController(loadImage);
         root = loader.load();
            
        } catch (IOException e1) {
            System.out.println("Error");
            e1.printStackTrace();
        }

        return root;

    }

   
    
}
