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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
public class SettingDialog {

    Map<String, String> settingsMap;
    Stage stage = new Stage();

    public SettingDialog(Stage stickyNoteStage, String font, String fontColor, String backgroundColor, String textAreaColor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingUI.fxml"));
            loader.setController(new SettingUIController(stage, font, fontColor, backgroundColor, textAreaColor));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.initOwner(stickyNoteStage);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.setResizable(false);

            stage.setOnCloseRequest((WindowEvent event) -> {
                SettingUIController controller = ((SettingUIController) loader.getController());
                settingsMap = controller.getSettings();
            });

            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, String> getSettings() {
        return settingsMap;
    }
}
