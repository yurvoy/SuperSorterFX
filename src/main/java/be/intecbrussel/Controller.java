package be.intecbrussel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Controller {

    // OPTIONS
    @FXML
    private RadioButton radio0;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    private ToggleGroup toggleOptions;
    // PATHS
    @FXML
    private TextField oldPath;
    @FXML
    private TextField newPath;
    // SORTING
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        toggleOptions = new ToggleGroup();
        radio0.setToggleGroup(toggleOptions);
        radio1.setToggleGroup(toggleOptions);
        radio2.setToggleGroup(toggleOptions);
        radio0.setSelected(true);
    }

    @FXML
    public void setUnsortedFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            oldPath.setText(directory.getAbsolutePath());
            Config.oldPath = directory.getAbsolutePath();
        }
    }

    @FXML
    public void setDestination() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            newPath.setText(directory.getAbsolutePath());
            Config.newPath = directory.getAbsolutePath();
        }
    }

    @FXML
    public void sortNow() {
        if (Config.newPath != null && Config.oldPath != null) {
            Summarizer summarizer = new Summarizer();
            if (Config.option == 0) {
                Sorter sorter = new Sorter();
                sorter.sortingFolder(Config.oldPath, Config.newPath);
                summarizer.summarize0(Config.newPath, sorter.filesListMaker(Config.newPath));
            }
            if (Config.option == 1) {
                TimeLine timeline = new TimeLine();
                timeline.timelineFolder(Config.oldPath, Config.newPath);
                summarizer.summarize1(Config.newPath, timeline.filesListMaker(Config.newPath));
            }
            if (Config.option == 2) {
                LastTimeLine lastTimeline = new LastTimeLine();
                lastTimeline.timelineFolder(Config.oldPath, Config.newPath);
                summarizer.summarize1(Config.newPath, lastTimeline.filesListMaker(Config.newPath));
            }
        }
    }

    @FXML
    public void radioButtonChanged(ActionEvent event) {
        if (radio0.isSelected()) {
            Config.option = 0;
        }
        if (radio1.isSelected()) {
            Config.option = 1;
        }
        if (radio2.isSelected()) {
            Config.option = 2;
        }
    }
}

