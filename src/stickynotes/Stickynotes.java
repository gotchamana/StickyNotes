/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
class NewStickynote {

    // Set variables about stage
    double stageX, stageY, stageWidth, stageHeight;

    Stage stage = new Stage();

    // Set the parent directory
    File parentDir = new File(System.getProperty("user.dir") + "/StickynotesData");

    // Loading icon
    Image icon = new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm());

    public NewStickynote() throws Exception {
        // Get the FXML document
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        // Set the scene
        Scene scene = new Scene(root);

        // Check the parent directory if exist
        // if not, create it
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        loadStageData();
        initStage(stage, scene);
    }

    public void loadStageData() throws IOException {
        // Get the stage's data
        File file = new File(parentDir, "StageData");

        if (file.exists()) {
            DataInputStream input = new DataInputStream(new FileInputStream(file));

            if (input.available() != 0) {
                // Get the previous stage's location
                stageX = input.readDouble();
                stageY = input.readDouble();

                // Get the previous stage's width  and height
                stageWidth = input.readDouble();
                stageHeight = input.readDouble();
            }
        }
    }

    public void initStage(Stage stage, Scene scene) {
        // Set the stage's title
        stage.setTitle("Stickynotes");

        // Set the stage's scene
        stage.setScene(scene);

        // Set the stage's location
        if (stageX != 0 && stageY != 0) {
            stage.setX(stageX);
            stage.setY(stageY);
        }

        // Set the stage's width and height
        if (stageWidth != 0 && stageHeight != 0) {
            stage.setWidth(stageWidth);
            stage.setHeight(stageHeight);
        }

        // Set the stage's minimum width and height
        stage.setMinWidth(150);
        stage.setMinHeight(150);

        // Set the stage's icon
        stage.getIcons().add(icon);

        // Set the stage's style
        stage.initStyle(StageStyle.UNDECORATED);

        // Set the stage's OnCloseRequest event
        stage.setOnCloseRequest((WindowEvent event) -> {
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
        });

        // Show the stage
        stage.show();
    }
}

public class Stickynotes extends Application {
/*
    // Set variables about stage
    double stageX, stageY, stageWidth, stageHeight;

    // Set the parent directory
    File parentDir = new File(System.getProperty("user.dir") + "/StickynotesData");

    // Loading icon
    Image icon = new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm());*/

    @Override
    public void start(Stage stage) throws Exception {
       /* // Get the FXML document
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        // Set the scene
        Scene scene = new Scene(root);

        // Check the parent directory if exist
        // if not, create it
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

//        loadStageData();
//        initStage(stage, scene);*/
        new NewStickynote();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*public void loadStageData() throws IOException {
        // Get the stage's data
        File file = new File(parentDir, "StageData");

        if (file.exists()) {
            DataInputStream input = new DataInputStream(new FileInputStream(file));

            if (input.available() != 0) {
                // Get the previous stage's location
                stageX = input.readDouble();
                stageY = input.readDouble();

                // Get the previous stage's width  and height
                stageWidth = input.readDouble();
                stageHeight = input.readDouble();
            }
        }
    }

    public void initStage(Stage stage, Scene scene) {
        // Set the stage's title
        stage.setTitle("Stickynotes");

        // Set the stage's scene
        stage.setScene(scene);

        // Set the stage's location
        if (stageX != 0 && stageY != 0) {
            stage.setX(stageX);
            stage.setY(stageY);
        }

        // Set the stage's width and height
        if (stageWidth != 0 && stageHeight != 0) {
            stage.setWidth(stageWidth);
            stage.setHeight(stageHeight);
        }

        // Set the stage's minimum width and height
        stage.setMinWidth(150);
        stage.setMinHeight(150);

        // Set the stage's icon
        stage.getIcons().add(icon);

        // Set the stage's style
        stage.initStyle(StageStyle.UNDECORATED);

        // Set the stage's OnCloseRequest event
        stage.setOnCloseRequest((WindowEvent event) -> {
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
        });

        // Show the stage
        stage.show();
    }*/
}
