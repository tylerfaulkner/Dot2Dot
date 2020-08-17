/*
 * Course: CS 2852 - 71
 * Spring 2020
 * Lab 2: Dot 2 Dot
 * Name: Tyler Faulkner
 * Created: 03/24/2020
 */
package faulknert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application that allows the user to load .dot file which are drawn on the canvas.
 */
public class Dot2Dot extends Application {
    private static final int STAGE_HEIGHT = 500;
    private static final int STAGE_WIDTH = 500;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("dot2dot.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(STAGE_HEIGHT);
        primaryStage.setWidth(STAGE_WIDTH);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
