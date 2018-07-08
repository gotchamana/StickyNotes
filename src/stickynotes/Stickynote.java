/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author shootingstar
 */
public class Stickynote {

    // Calculate the total number of stickynote
    static int number;

    // Store all stickynotes
    static List<Stickynote> stickynoteList = new ArrayList<>();

    // Set the default location and size of the stage
    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    static final double DEFAULT_WIDTH = 250, DEFAULT_HEIGHT = 250;
    static final double DEFAULT_X = screenBounds.getWidth() / 2 - DEFAULT_WIDTH / 2, DEFAULT_Y = screenBounds.getHeight() / 2 - DEFAULT_HEIGHT / 2;

    // For storing the stage data
    static Map<Integer, Map<Integer, Object>> stageDataMap = new HashMap<>();

    // Set the save file
    static File saveFile = new File(System.getProperty("user.dir") + "/StickynotesData");

    // Record the stage's id
    int id;

    // Create a new stage
    Stage stage = new Stage();

    StickynoteController controller;

    // Loading icon
    Image icon = new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm());

    public Stickynote() {
        this(number, DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
    }

    public Stickynote(int id, double x, double y, double width, double height, String text) {
        this.id = id;
        number++;

        initStage(x, y, width, height, text);
    }

    public void initStage(double x, double y, double width, double height, String text) {
        try {
            // Get the FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StickynoteUI.fxml"));

            controller = new StickynoteController(id, stage, text);
            loader.setController(controller);

            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Set the stage's title
            stage.setTitle("Stickynotes");

            // Set the stage's scene
            stage.setScene(scene);

            // Set the stage's minimum width and height
            stage.setMinWidth(150);
            stage.setMinHeight(150);

            // Set the stage's icon
            stage.getIcons().add(icon);

            // Set the stage's style
            stage.initStyle(StageStyle.UNDECORATED);

            // Set the stage's location
            stage.setX(x);
            stage.setY(y);

            // Set the stage's width and height
            stage.setWidth(width);
            stage.setHeight(height);

            // Show the stage
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
