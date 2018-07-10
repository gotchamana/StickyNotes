/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.IOException;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
public class SettingDialog {

    // Store the settings data
    Map<String, String> settingsMap;

    // Create a new stage
    Stage stage = new Stage();

    public SettingDialog(Stage stickyNoteStage, String font, String fontColor, String backgroundColor, String textAreaColor) {
        initStage(stickyNoteStage, font, fontColor, backgroundColor, textAreaColor);
    }

    public void initStage(Stage stickyNoteStage, String font, String fontColor, String backgroundColor, String textAreaColor) {
        try {
            // Get the FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UI/SettingUI.fxml"));
            loader.setController(new SettingUIController(stage, font, fontColor, backgroundColor, textAreaColor));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.initOwner(stickyNoteStage);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set the stage's title
            stage.setTitle("Settings");

            // Set the stage's icon
            stage.getIcons().add(new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm()));

            // Set the stage's scene
            stage.setScene(scene);

            // Set the stage's resizable property
            stage.setResizable(false);

            // Set the stage's OnCloseRequest event
            stage.setOnCloseRequest((WindowEvent event) -> {
                SettingUIController controller = ((SettingUIController) loader.getController());
                settingsMap = controller.getSettings();
            });

            // Show the stage
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, String> getSettings() {
        return settingsMap;
    }
}
