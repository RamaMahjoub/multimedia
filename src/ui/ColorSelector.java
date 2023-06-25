package ui;

import PageSwitcher.PageSwitcher;
import interfaces.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import threads.ColorThreads;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ColorSelector implements Page{

    PageSwitcher pageSwitcher;
    public ColorSelector(PageSwitcher pageSwitcher) {
        this.pageSwitcher = pageSwitcher;
    }

    private ListView<Color> colorListView;
    private ObservableList<Color> selectedColors;
    private VBox layout;
    private Button selectFolderButton;
    public Parent getView() {
        selectedColors = FXCollections.observableArrayList();
        colorListView = new ListView<>();
        colorListView.setPrefSize(200, 150);
        colorListView.setItems(selectedColors);

        Button selectColorButton = new Button("Select Color");
        selectColorButton.setOnAction(e -> showColorPicker());

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
        layout.getChildren().addAll(colorListView, selectColorButton, selectFolderButton);

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
                if(folder.isFile()){
                    continue;
                }
                if(folder.isDirectory()) {
                    hasSubFolder = true;
                    String folderPath = folder.getAbsolutePath();
                    Callable<List<Image>> task = new ColorThreads(folderPath, getSelectedColors());
                    tasks.add(task);
                }
            }
            if(!hasSubFolder){
                Callable<List<Image>> selectedFolderTask = new ColorThreads(selectedFolder.getAbsolutePath(), getSelectedColors());
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

        List<Image> results = new ArrayList<>();

        for (Future<List<Image>> future : futures) {
            try {
                List<Image> result = future.get();
                System.out.println(result.size());
                results.addAll(result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Here");
                e.printStackTrace();
            }
            System.out.println(results.size());
            pageSwitcher.switchToPage(new DisplayColorImageFinder(pageSwitcher,results));
        }
        }

    }
    private void showColorPicker() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            Color selectedColor = colorPicker.getValue();
            if (!selectedColors.contains(selectedColor)) {
                selectedColors.add(selectedColor);
            }
        });

        Stage colorPickerStage = new Stage();
        colorPickerStage.setTitle("Select Color");
        colorPickerStage.setScene(new Scene(colorPicker));
        colorPickerStage.show();
    }


    public List<Color> getSelectedColors() {
        return new ArrayList<>(selectedColors);
    }
}
