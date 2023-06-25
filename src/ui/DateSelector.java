package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import threads.ColorThreads;
import threads.DateThreads;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DateSelector implements Page {
    PageSwitcher pageSwitcher;

    public DateSelector(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }

    private Button selectFolderButton;
    private VBox layout;

    public Parent getView() {


        selectFolderButton = new Button("Select Folder");
        selectFolderButton.setOnAction(e -> {
            try {
                selectFolder();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(event -> {
            LocalDate selectedDate = datePicker.getValue();
            System.out.println("Selected Date: " + selectedDate);
        });

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
        layout.getChildren().addAll(datePicker, selectFolderButton);

        return layout;
    }


    private void selectFolder() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(layout.getScene().getWindow());
        boolean hasSubFolder = false;
        List<Callable<List<javafx.scene.image.Image>>> tasks = new ArrayList<>();
        if (selectedFolder != null) {
            File[] files = selectedFolder.listFiles();

            for (File folder : files) {
                if (folder.isFile()) {
                    continue;
                }
                if (folder.isDirectory()) {
                    hasSubFolder = true;
                    String folderPath = folder.getAbsolutePath();
                    LocalDate date = LocalDate.now();
                    Callable<List<javafx.scene.image.Image>> task = new DateThreads(folderPath, date);
                    tasks.add(task);
                }

            }
            if(!hasSubFolder){
                LocalDate date = LocalDate.now();
                Callable<List<Image>> selectedFolderTask = new DateThreads(selectedFolder.getAbsolutePath(), date);
                tasks.add(selectedFolderTask);
            }
            ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());
            List<Future<List<javafx.scene.image.Image>>> futures;
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
