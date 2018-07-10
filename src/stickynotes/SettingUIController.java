/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.FontSelectorDialog;

/**
 * FXML Controller class
 *
 * @author shootingstar
 */
public class SettingUIController implements Initializable {

    // Store the OK-Cancel button's result
    boolean isPressedOK;

    // Store the CSS settings
    String fontCSS, fontColorCSS, backgroundColorCSS, textAreaColorCSS;
    Map<String, String> settingsMap = new HashMap<>();

    // Set the stage
    Stage stage;

    // Get the TextField
    @FXML
    TextField txtFld = new TextField();

    // Get the select font button
    @FXML
    Button btnSelectFont = new Button();

    // Get the select font color ColorPicker
    @FXML
    ColorPicker clrPkrFontColor = new ColorPicker();

    // Get the select background color ColorPicker
    @FXML
    ColorPicker clrPkrBackgroundColor = new ColorPicker();

    // Get the OK button
    @FXML
    Button btnOK = new Button();

    // Get the cancel button
    @FXML
    Button btnCancel = new Button();

    public SettingUIController(Stage settingStage, String font, String fontColor, String backgroundColor, String textAreaColor) {
        stage = settingStage;

        // Get the current settings
        fontCSS = font;
        fontColorCSS = fontColor;
        backgroundColorCSS = backgroundColor;
        textAreaColorCSS = textAreaColor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get the current font-family and font-size
        String fontFamily = fontCSS.substring(fontCSS.indexOf("\"") + 1, fontCSS.length() - 2);
        String fontSize = fontCSS.substring(fontCSS.lastIndexOf(" ", fontCSS.indexOf("\"") - 2), fontCSS.indexOf("\"") - 1);

        // Set the TextField's text
        txtFld.setText(fontFamily + ", " + fontSize);

        // Change the ColorPicker's mode and set the color value
        clrPkrFontColor.getStyleClass().add("button");
        clrPkrFontColor.setValue(Color.web(fontColorCSS.substring(fontColorCSS.indexOf(' ') + 1, fontColorCSS.length() - 1)));

        clrPkrBackgroundColor.getStyleClass().add("button");
        clrPkrBackgroundColor.setValue(Color.web(backgroundColorCSS.substring(backgroundColorCSS.indexOf(' ') + 1, backgroundColorCSS.length() - 1)));
    }

    // Handle the select font button's OnAction event
    @FXML
    public void handleBtnSelectFontAction() {
        // Get the current font-family and font-size
        String originalFamily = fontCSS.substring(fontCSS.indexOf("\"") + 1, fontCSS.length() - 2);
        double originalSize = Double.parseDouble(fontCSS.substring(fontCSS.lastIndexOf(" ", fontCSS.indexOf("\"") - 2), fontCSS.indexOf("\"") - 1));

        // Set the font selector dialog's default font
        FontSelectorDialog dialog = new FontSelectorDialog(Font.font(originalFamily, originalSize));

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        dialog.setHeaderText(null);
        dialog.getDialogPane().setStyle("-fx-graphic: null");

        // Show the dialog and get the selected font
        dialog.showAndWait().ifPresent((Font t) -> {
            String style = filterFontStyle(t.getStyle());
            String size = t.getSize() + " ";
            String family = "\"" + t.getFamily() + "\";";

            fontCSS = "-fx-font: " + style + size + family;
            txtFld.setText(t.getFamily() + ", " + t.getSize());
        });
    }

    // Handle the select font color ColorPicker's OnAction event
    @FXML
    public void handleClrPkrFontColorAction() {
        // Get the selected color
        fontColorCSS = "-fx-text-inner-color: " + clrPkrFontColor.getValue().toString().replaceFirst("0x", "#") + ";";
    }

    // Handle the select background color ColorPicker's OnAction event
    @FXML
    public void handleClrPkrBackgroundColor() {
        // Get the selected color
        String color = clrPkrBackgroundColor.getValue().toString().replaceFirst("0x", "#") + ";";

        backgroundColorCSS = "-fx-background-color: " + color;
        textAreaColorCSS = "-fx-control-inner-background: " + color;
    }

    // Handle the OK button's OnAction event
    @FXML
    public void handleBtnOKAction() {
        isPressedOK = true;

        // Save the settings
        settingsMap.put("font", fontCSS);
        settingsMap.put("font-color", fontColorCSS);
        settingsMap.put("background-color", backgroundColorCSS);
        settingsMap.put("textArea-color", textAreaColorCSS);

        // Close the stage
        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    // Handle the cancel button's OnAction event
    @FXML
    public void handleBtnCancelAction() {
        isPressedOK = false;

        // Close the stage
        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public String filterFontStyle(String style) {
        String rlt = "";

        if (style.contains("Bold") || style.contains("bold")) {
            rlt += "bold ";
        }

        if (style.contains("Italic") || style.contains("italic")) {
            rlt += "italic ";
        }

        return rlt;
    }

    // Return the pressed button's result
    public Map<String, String> getSettings() {
        return isPressedOK ? settingsMap : null;
    }
}
