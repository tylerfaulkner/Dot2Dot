/*
 * Course: CS 2852 - 71
 * Spring 2020
 * Lab 2: Dot 2 Dot
 * Name: Tyler Faulkner
 * Created: 03/24/2020
 */
package faulknert;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * Controller class for the the dot 2 dot application.
 */
public class Dot2DotController {
    private static final double NANO_TO_MILLISECONDS = 10000;
    private boolean opened = false;
    private String strategy = "";
    private String removal = "index";
    private File file;
    private Picture picture;

    @FXML
    private Canvas canvas;
    @FXML
    private MenuItem dotsOnly;
    @FXML
    private MenuItem linesOnly;
    @FXML
    private MenuItem removeDots;
    @FXML
    private MenuItem restoreItem;
    @FXML
    private MenuItem saveItem;
    @FXML
    private Label dotCount;
    @FXML
    private Label time;

    @FXML
    private void open() {
        if (!strategy.equals("")) {
            if (strategy.equals("array")) {
                picture = new Picture(new ArrayList<>());
            } else {
                picture = new Picture(new LinkedList<>());
            }
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Dot File",
                        "*.dot"));
                file = fileChooser.showOpenDialog(null);
                //code forced to wait until user selects a file
                picture.load(file.toPath());
                canvas.getGraphicsContext2D().clearRect(0, 0,
                        canvas.getWidth(), canvas.getHeight());
                picture.drawDots(canvas);
                picture.drawLines(canvas);
                dotsOnly.setDisable(false);
                linesOnly.setDisable(false);
                removeDots.setDisable(false);
                saveItem.setDisable(false);
                restoreItem.setDisable(false);
                dotCount.setText(Integer.toString(picture.getDotCount()));
                opened = true;
            } catch (NumberFormatException | InputMismatchException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "The file is not formatted correctly.");
                alert.setHeaderText("Invalid Format in File");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "The file contains values that are not between 0 and 1.");
                alert.setHeaderText("Illegal Value in File");
                alert.showAndWait();
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "The file has been moved or deleted.");
                alert.setHeaderText("File Not Found");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Please select a strategy before opening a file.");
            alert.setHeaderText("No Strategy Selected");
            alert.showAndWait();
        }
    }

    @FXML
    private void save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a save location.");
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("Dot File", "*.dot"));
        picture.save(fileChooser.showSaveDialog(null).toPath());
    }

    @FXML
    private void remove() {
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText("Enter desired amount of Dots after removal:");
        input.showAndWait();
        picture.removeDots(Integer.parseInt(input.getEditor().getText()), removal);
        canvas.getGraphicsContext2D().clearRect(0, 0,
                canvas.getWidth(), canvas.getHeight());
        picture.drawDots(canvas);
        picture.drawLines(canvas);
        dotCount.setText(Integer.toString(picture.getDotCount()));
        time.setText(Double.toString(picture.getRemovalTime()/NANO_TO_MILLISECONDS) + " ms");
    }

    @FXML
    private void restore(){
        try {
            picture.load(file.toPath());
            canvas.getGraphicsContext2D().clearRect(0, 0,
                    canvas.getWidth(), canvas.getHeight());
            picture.drawDots(canvas);
            picture.drawLines(canvas);
            dotCount.setText(Integer.toString(picture.getDotCount()));
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "The file has been moved or deleted since it was initially loaded.");
            alert.setHeaderText("File Not Found");
            alert.showAndWait();
        }
    }

    @FXML
    private void close() {
        Platform.exit();
    }

    @FXML
    private void lines() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawLines(canvas);
    }

    @FXML
    private void dots() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        picture.drawDots(canvas);
    }

    @FXML
    private void selectArrayList() {
        strategy = "array";
        if (opened) {
            picture = new Picture(picture, new ArrayList<>());
        }
    }

    @FXML
    private void selectLinkedList() {
        strategy = "linked";
        if (opened) {
            picture = new Picture(picture, new LinkedList<>());
        }
    }

    @FXML
    private void selectIndexRemove(){
        removal = "index";
    }

    @FXML
    private void selectIteratorRemove(){
        removal = "iterator";
    }
}
