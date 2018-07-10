/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
public class StickyNoteController implements Initializable {

    // For storing the data type's number
    static Map<String, Integer> dataTypeMap = new HashMap<>();

    // Set the variables about the stage
    int id;
    double xOffset, yOffset;

    String text;
    String font, fontColor, backgroundColor, textAreaColor;

    Stage stage;

    // Set the stage's images
    Image close, settings, add, lock, unlock, resize;

    // Get the BorderPane
    @FXML
    BorderPane borderPane = new BorderPane();

    // Get the title bar (GridPane)
    @FXML
    GridPane titleBar = new GridPane();

    // Get the close button
    @FXML
    Button btnClose = new Button();

    // Get the lock ToggleButton
    @FXML
    ToggleButton tgeBtnLock = new ToggleButton();

    // Get the settings button
    @FXML
    Button btnSettings = new Button();

    // Get the add button
    @FXML
    Button btnAdd = new Button();

    // Get the TextArea
    @FXML
    TextArea txtArea = new TextArea();

    // Get the resizer ImageView
    @FXML
    ImageView imgViewResizer = new ImageView();

    static {
        // Initialize the dataTypeMap
        dataTypeMap.put("x", 0);
        dataTypeMap.put("y", 1);
        dataTypeMap.put("width", 2);
        dataTypeMap.put("height", 3);
        dataTypeMap.put("text", 4);

        dataTypeMap.put("font", 5);
        dataTypeMap.put("font-color", 6);
        dataTypeMap.put("background-color", 7);
        dataTypeMap.put("textArea-color", 8);
    }

    public StickyNoteController(int id, Stage stage, String text,
            String font, String fontColor, String backgroundColor, String textAreaColor) {
        this.id = id;
        this.stage = stage;
        this.text = text;

        this.font = font;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        this.textAreaColor = textAreaColor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Loading the images
        close = new Image(getClass().getResource("Icons/close.png").toExternalForm());
        lock = new Image(getClass().getResource("Icons/lock.png").toExternalForm());
        unlock = new Image(getClass().getResource("Icons/unlock.png").toExternalForm());
        settings = new Image(getClass().getResource("Icons/gear.png").toExternalForm());
        add = new Image(getClass().getResource("Icons/add.png").toExternalForm());
        resize = new Image(getClass().getResource("Icons/resizer.png").toExternalForm());

        // Set the images
        btnClose.setGraphic(new ImageView(close));
        tgeBtnLock.setGraphic(new ImageView(lock));
        btnSettings.setGraphic(new ImageView(settings));
        btnAdd.setGraphic(new ImageView(add));
        imgViewResizer.setImage(resize);

        // Set the textarea's text
        txtArea.setText(text);

        // Set the CSS style
        txtArea.setStyle(font + fontColor + backgroundColor + textAreaColor + StickyNote.TXTAREA_BACKGROUND_COLOR);
        borderPane.setStyle(backgroundColor);

        // Handle the TextArea's OnFocused event
        txtArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    // If TextArea losed the focus, save the text
                    saveText();
                }
            }
        });
    }

    // Handle the button's OnMouseEntered event
    @FXML
    public void handleBtnMouseEnter(Event e) {
        ColorAdjust clrAdj = new ColorAdjust();
        clrAdj.setBrightness(0.3);

        // Change the brightness of the button's image
        ImageView imgView = (ImageView) ((ButtonBase) e.getSource()).getGraphic();
        imgView.setEffect(clrAdj);
    }

    // Handle the button's OnMouseExited event
    @FXML
    public void handleBtnMouseExit(Event e) {
        ColorAdjust clrAdj = new ColorAdjust();
        clrAdj.setBrightness(0);

        // Change the brightness of the button's image
        ImageView imgView = (ImageView) ((ButtonBase) e.getSource()).getGraphic();
        imgView.setEffect(clrAdj);
    }

    // Handle the close button's OnAction event
    @FXML
    public void handleBtnClose() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to delete this Sticky-Note?",
                ButtonType.YES, ButtonType.NO);

        alert.setTitle("Delete this Sticky-Note");
        alert.setHeaderText(null);
        alert.initOwner(stage);

        alert.showAndWait().ifPresent((ButtonType t) -> {
            if (t == ButtonType.YES) {
                // Close the stage
                Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

                deleteStickynote();
            }
        });
    }

    // Handle the add button's OnAction event
    @FXML
    public void handleBtnAdd() {
        // Add a new stage
        StickyNote.stickynoteList.add(new StickyNote());
    }

    // Handle the lock ToggleButton's OnAction event
    @FXML
    public void handleTgeBtnLock() {
        // Change the button's image and the TextArea's editable property
        if (tgeBtnLock.isSelected()) {
            tgeBtnLock.setGraphic(new ImageView(unlock));
            txtArea.setEditable(true);
        } else {
            tgeBtnLock.setGraphic(new ImageView(lock));
            txtArea.setEditable(false);
        }
    }

    // Handle the settiings button's OnAction event
    @FXML
    public void handleBtnSettings() {
        // Show the setting dialog and get settings from it
        Map<String, String> settingsMap = new SettingDialog(stage, font, fontColor, backgroundColor, textAreaColor).getSettings();

        // If the data is not null, change the controllers' styles and write them into the file
        if (settingsMap != null) {
            // Get the settings from the map
            font = settingsMap.get("font");
            fontColor = settingsMap.get("font-color");
            textAreaColor = settingsMap.get("textArea-color");
            backgroundColor = settingsMap.get("background-color");

            // Set the style
            txtArea.setStyle(font + fontColor + textAreaColor + StickyNote.TXTAREA_BACKGROUND_COLOR);
            borderPane.setStyle(backgroundColor);

            // Write the style into the file
            try (DataOutputStream output = new DataOutputStream(new FileOutputStream(StickyNote.saveFile, true))) {
                output.writeInt(id);
                output.writeInt(dataTypeMap.get("font"));
                output.writeUTF(font);

                output.writeInt(id);
                output.writeInt(dataTypeMap.get("font-color"));
                output.writeUTF(fontColor);

                output.writeInt(id);
                output.writeInt(dataTypeMap.get("background-color"));
                output.writeUTF(backgroundColor);

                output.writeInt(id);
                output.writeInt(dataTypeMap.get("textArea-color"));
                output.writeUTF(textAreaColor);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Handle the title bar's (GridPane) OnMouseEntered event
    @FXML
    public void handleTitleBarMouseEnter() {
        // Display the controllers
        for (Node node : titleBar.getChildren()) {
            node.setVisible(true);
        }
    }

    // Handle the title bar's (GridPane) OnMouseExited event
    @FXML
    public void handleTitleBarMouseExit() {
        // Hide the controllers
        for (Node node : titleBar.getChildren()) {
            node.setVisible(false);
        }
    }

    // Handle the title bar's (GridPane) OnMousePressed event
    @FXML
    public void handleTitleBarMousePress(MouseEvent e) {
        // Get the mouse's location on the scene
        xOffset = e.getSceneX();
        yOffset = e.getSceneY();
    }

    // Handle the title bar's (GridPane) OnMouseDragged event
    @FXML
    public void handleTitleBarMouseDrag(MouseEvent e) {
        // Change the stage's location
        stage.setX(e.getScreenX() - xOffset);
        stage.setY(e.getScreenY() - yOffset);
    }

    // Handle the title bar's (GridPane) OnMouseReleased event
    @FXML
    public void handleTitleBarMouseRelease(MouseEvent e) {
        saveLocation();
    }

    // Handle the resizer's (ImageView) OnMouseDragged event
    @FXML
    public void handleImgViewResizerMouseDrag(MouseEvent e) {
        // Change the stage's size
        stage.setWidth(e.getSceneX());
        stage.setHeight(e.getSceneY());
    }

    // Handle the resizer's (ImageView) OnMouseReleased event
    @FXML
    public void handleImgViewResizerMouseRelease() {
        saveSize();
    }

    public void deleteStickynote() {
        StickyNote.stageDataMap = new HashMap<>();

        // Get the stage's data
        if (StickyNote.saveFile.exists()) {
            try (DataInputStream input = new DataInputStream(new FileInputStream(StickyNote.saveFile))) {
                // Read data
                while (input.available() != 0) {
                    // Get the id
                    int id = input.readInt();

                    // Get the data type
                    int dataType = input.readInt();

                    // Get the data
                    Object data;

                    if (dataType < StickyNoteController.dataTypeMap.get("text")) {
                        data = input.readDouble();
                    } else {
                        data = input.readUTF();
                    }

                    Map<Integer, Object> innerMap = StickyNote.stageDataMap.get(id);

                    if (innerMap == null) {
                        innerMap = new HashMap<>();
                        StickyNote.stageDataMap.put(id, innerMap);
                    }

                    // Write data into innerMap
                    innerMap.put(dataType, data);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Delete the old data
        StickyNote.saveFile.delete();

        for (Map.Entry<Integer, Map<Integer, Object>> entry : StickyNote.stageDataMap.entrySet()) {
            Integer id = entry.getKey();
            Map<Integer, Object> innerMap = entry.getValue();

            for (Map.Entry<Integer, Object> innerEntry : innerMap.entrySet()) {
                int dataType = innerEntry.getKey();
                Object data = innerEntry.getValue();

                try (DataOutputStream output = new DataOutputStream(new FileOutputStream(StickyNote.saveFile, true))) {
                    // Get rid of the stickynote wanting to delete
                    if (id != this.id) {
                        // Rewrite the new data
                        output.writeInt(id);
                        output.writeInt(dataType);

                        if (data instanceof Double) {
                            output.writeDouble((Double) data);
                        } else {
                            output.writeUTF((String) data);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void saveText() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(StickyNote.saveFile, true))) {
            // Save the text
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("text"));
            output.writeUTF(txtArea.getText());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveLocation() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(StickyNote.saveFile, true))) {
            // Save the X's coordinate
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("x"));
            output.writeDouble(stage.getX());

            // Save the Y's coordinate
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("y"));
            output.writeDouble(stage.getY());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveSize() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(StickyNote.saveFile, true))) {
            // Save the width
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("width"));
            output.writeDouble(stage.getWidth());

            // Save the height
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("height"));
            output.writeDouble(stage.getHeight());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
