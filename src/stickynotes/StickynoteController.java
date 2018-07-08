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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author shootingstar
 */
public class StickynoteController implements Initializable {

    // For storing the data type's number
    static Map<String, Integer> dataTypeMap = new HashMap<>();

    // Set the variables about the stage
    int id;
    double xOffset, yOffset;

    String text;
    Stage stage;

    // Set the stage's images
    Image close, settings, add, lock, unlock, resize;

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
    }

    public StickynoteController(int id, Stage stage, String text) {
        this.id = id;
        this.stage = stage;
        this.text = text;
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
        deleteStickynote();

        // Close the stage
        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    // Handle the add button's OnAction event
    @FXML
    public void handleBtnAdd() {
        // Add a new stage
        Stickynote.stickynoteList.add(new Stickynote());
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
        // Decrease the total number
        Stickynote.number--;

        // Get the stage's data
        if (Stickynote.saveFile.exists()) {
            try (DataInputStream input = new DataInputStream(new FileInputStream(Stickynote.saveFile))) {
                // Read data
                while (input.available() != 0) {
                    // Get the id
                    int id = input.readInt();

                    // Get the data type
                    int dataType = input.readInt();

                    // Get the data
                    Object data;

                    if (dataType != StickynoteController.dataTypeMap.get("text")) {
                        data = input.readDouble();
                    } else {
                        data = input.readUTF();
                    }

                    Map<Integer, Object> innerMap = Stickynote.stageDataMap.get(id);

                    if (innerMap == null) {
                        innerMap = new HashMap<>();
                        Stickynote.stageDataMap.put(id, innerMap);
                    }

                    // Write data into innerMap
                    innerMap.put(dataType, data);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Delete the old data
        Stickynote.saveFile.delete();

        for (Map.Entry<Integer, Map<Integer, Object>> entry : Stickynote.stageDataMap.entrySet()) {
            int id = entry.getKey();
            Map<Integer, Object> innerMap = entry.getValue();

            for (Map.Entry<Integer, Object> innerEntry : innerMap.entrySet()) {
                int dataType = innerEntry.getKey();
                Object data = innerEntry.getValue();

                try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Stickynote.saveFile, true))) {
                    // Get rid of the stickynote wanting to delete
                    if (id != this.id) {
                        // Decrease the id greater than the deleted stickynote's id
                        if (id > this.id) {
                            id--;
                        }

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

        int tmp = this.id;
        for (Stickynote stickynote : Stickynote.stickynoteList) {

            // Decrease the id greater than the deleted stickynote's id
            if (stickynote.id > tmp) {
                stickynote.id--;
                stickynote.controller.id--;

                // Get rid of the stickynote wanting to delete
            } else if (stickynote.id == tmp) {
                stickynote.id = -1;
                stickynote.controller.id = -1;
                this.id = -1;
            }
        }
    }

    public void saveText() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Stickynote.saveFile, true))) {
            // Save the text
            output.writeInt(id);
            output.writeInt(dataTypeMap.get("text"));
            output.writeUTF(txtArea.getText());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveLocation() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Stickynote.saveFile, true))) {
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
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Stickynote.saveFile, true))) {
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
