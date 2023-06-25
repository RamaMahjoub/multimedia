
package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import threads.DateThreads;
import threads.SizeThreads;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SizeSelector implements Page {
    PageSwitcher pageSwitcher;

    public SizeSelector(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }
    private Button selectFolderButton;
    private VBox layout;
    String wi, hei;
    TextField width, height;

    public Parent getView() {

        width = new TextField();
        width.setPromptText("width:");
        wi = width.getText();
        height = new TextField();
        height.setPromptText("height:");
        hei = height.getText();
        System.out.println("hhhhhhhhhhhh" + height.getText());
        selectFolderButton = new Button("Select Folder");
        selectFolderButton.setOnAction(e -> {
            try {
                selectFolder();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(width, height, selectFolderButton);

        return layout;
    }


    private void selectFolder() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(layout.getScene().getWindow());
        boolean hasSubFolder = false;

        List<Callable<List<Image>>> tasks = new ArrayList<>();
        if (selectedFolder != null) {
            File[] files = selectedFolder.listFiles();

            for (File folder : files) {
                if (folder.isFile()) {
                    continue;
                }
                if (folder.isDirectory()) {
                    hasSubFolder = true;
                    String folderPath = folder.getAbsolutePath();
                    Callable<List<javafx.scene.image.Image>> task = new SizeThreads(folderPath, width.getText(), height.getText());
                    tasks.add(task);
                }
            }
            if(!hasSubFolder){
                Callable<List<Image>> selectedFolderTask = new SizeThreads(selectedFolder.getAbsolutePath(), width.getText(), height.getText());
                tasks.add(selectedFolderTask);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
            List<Future<List<Image>>> futures;
            try {
                futures = executorService.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            executorService.shutdown();

            List<javafx.scene.image.Image> results = new ArrayList<>();

            for (Future<List<javafx.scene.image.Image>> future : futures) {
                try {
                    List<Image> result = future.get();
                    System.out.println(result.size());
                    results.addAll(result);
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Here");
                    e.printStackTrace();
                }
                System.out.println(results.size());
                pageSwitcher.switchToPage(new DisplayColorImageFinder(pageSwitcher, results));
            }
        }

    }

}
