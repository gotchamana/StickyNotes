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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    boolean isPressedOK;

    String fontCSS, fontColorCSS, backgroundColorCSS, textAreaColorCSS;

    Map<String, String> settingsMap = new HashMap<>();

    Stage stage;

    @FXML
    TextField txtFld = new TextField();

    @FXML
    Button btnSelectFont = new Button();

    @FXML
    ColorPicker clrPkrFontColor = new ColorPicker();

    @FXML
    ColorPicker clrPkrBackgroundColor = new ColorPicker();

    @FXML
    Button btnOK = new Button();

    @FXML
    Button btnCancel = new Button();

    public SettingUIController(Stage settingStage, String font, String fontColor, String backgroundColor, String textAreaColor) {
        stage = settingStage;

        fontCSS = font;
        fontColorCSS = fontColor;
        backgroundColorCSS = backgroundColor;
        textAreaColorCSS = textAreaColor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String fontFamily = fontCSS.substring(fontCSS.indexOf("\"") + 1, fontCSS.length() - 2);
        String fontSize = fontCSS.substring(fontCSS.lastIndexOf(" ", fontCSS.indexOf("\"") - 2), fontCSS.indexOf("\"") - 1);

        txtFld.setText(fontFamily + ", " + fontSize);

        clrPkrFontColor.getStyleClass().add("button");
        clrPkrFontColor.setValue(Color.web(fontColorCSS.substring(fontColorCSS.indexOf(' ') + 1, fontColorCSS.length() - 1)));

        clrPkrBackgroundColor.getStyleClass().add("button");
        clrPkrBackgroundColor.setValue(Color.web(backgroundColorCSS.substring(backgroundColorCSS.indexOf(' ') + 1, backgroundColorCSS.length() - 1)));
    }

    @FXML
    public void handleBtnSelectFontAction() {
        String originalFamily = fontCSS.substring(fontCSS.indexOf("\"") + 1, fontCSS.length() - 2);
        double originalSize = Double.parseDouble(fontCSS.substring(fontCSS.lastIndexOf(" ", fontCSS.indexOf("\"") - 2), fontCSS.indexOf("\"") - 1));

        FontSelectorDialog dialog = new FontSelectorDialog(Font.font(originalFamily, originalSize));

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        dialog.setHeaderText(null);
        dialog.getDialogPane().setStyle("-fx-graphic: null");

        dialog.showAndWait().ifPresent((Font t) -> {
            String style = filterFontStyle(t.getStyle());
            String size = t.getSize() + " ";
            String family = "\"" + t.getFamily() + "\";";

            fontCSS = "-fx-font: " + style + size + family;

            txtFld.setText(t.getFamily() + ", " + t.getSize());
        });
    }

    @FXML
    public void handleClrPkrFontColorAction() {
        fontColorCSS = "-fx-text-inner-color: " + clrPkrFontColor.getValue().toString().replaceFirst("0x", "#") + ";";
    }

    @FXML
    public void handleClrPkrBackgroundColor() {
        String color = clrPkrBackgroundColor.getValue().toString().replaceFirst("0x", "#") + ";";
        backgroundColorCSS = "-fx-background-color: " + color;
        textAreaColorCSS = "-fx-control-inner-background: " + color;
    }

    @FXML
    public void handleBtnOKAction() {
        isPressedOK = true;

        settingsMap.put("font", fontCSS);
        settingsMap.put("font-color", fontColorCSS);
        settingsMap.put("background-color", backgroundColorCSS);
        settingsMap.put("textArea-color", textAreaColorCSS);

        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML
    public void handleBtnCancelAction() {
        isPressedOK = false;

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

    public Map<String, String> getSettings() {
        return isPressedOK ? settingsMap : null;
    }
}
