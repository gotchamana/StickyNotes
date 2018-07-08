/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
public class Stickynote {

    // Calculate the total number of stickynote
    static int number;

    int id;

    // Create a new stage
    Stage stage = new Stage();

    // Loading icon
    Image icon = new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm());

    public Stickynote() throws Exception {
        // Id base on zero
        id = number;
        number++;

        initStage(loadFXML(id, stage));

        // Show the stage
        stage.show();
    }

    public Stickynote(int id, double x, double y, double width, double height, String text) throws Exception {
        this.id = id;
        number++;

        initStage(loadFXML(this.id, stage, text), x, y, width, height);

        // Show the stage
        stage.show();
    }

    public void initStage(Parent root) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        double width = 250, height = width;
        double x = screenBounds.getWidth() / 2 - width / 2, y = screenBounds.getHeight() / 2 - height / 2;

        initStage(root, x, y, width, height);
    }

    public void initStage(Parent root, double x, double y, double width, double height) throws Exception {
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

        // Set the stage's OnCloseRequest event
        /*stage.setOnCloseRequest((WindowEvent event) -> {
            // Set the parent directory
            File parentDir = new File(System.getProperty("user.dir") + "/StickynotesData");

            // Save the stage's location and size
            try (DataOutputStream output = new DataOutputStream(new FileOutputStream(new File(parentDir, "StageData")))) {
                // Save the location
                output.writeDouble(stage.getX());
                output.writeDouble(stage.getY());

                // Save the size
                output.writeDouble(stage.getWidth());
                output.writeDouble(stage.getHeight());
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });*/

        // Set the stage's location
        stage.setX(x);
        stage.setY(y);

        // Set the stage's width and height
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public Parent loadFXML(int id, Stage stage) throws Exception {
        Parent root = loadFXML(id, stage, "");

        return root;
    }

    public Parent loadFXML(int id, Stage stage, String text) throws Exception {
        StickynoteController controller = new StickynoteController(id, stage, text);

        // Get the FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StickynoteUI.fxml"));
        loader.setController(controller);

        Parent root = loader.load();

        return root;
    }
}
