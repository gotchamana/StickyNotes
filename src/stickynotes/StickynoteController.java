/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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

    int stageId;

    // Set the variables about the stage's movement
    double xOffset = 0, yOffset = 0;

    String text;

    // Set the stage's images
    Image close, settings, add, lock, unlock, resize;

    // Set the stage variable
    Stage stage;

    // Get the title bar
    @FXML
    GridPane titleBar = new GridPane();

    // Get the close button
    @FXML
    Button btnClose = new Button();

    // Get the settings button
    @FXML
    Button btnSettings = new Button();

    // Get the add button
    @FXML
    Button btnAdd = new Button();

    // Get the lock toggle button
    @FXML
    ToggleButton tgeBtnLock = new ToggleButton();

    // Get the textarea
    @FXML
    TextArea txtArea = new TextArea();

    // Get the resizer imageview
    @FXML
    ImageView imgViewResizer = new ImageView();

    public StickynoteController(int id, Stage stage, String text) {
        stageId = id;
        this.stage = stage;
        this.text = text;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Loading the images
        close = new Image(getClass().getResource("Icons/close.png").toExternalForm());
        settings = new Image(getClass().getResource("Icons/gear.png").toExternalForm());
        add = new Image(getClass().getResource("Icons/add.png").toExternalForm());
        lock = new Image(getClass().getResource("Icons/lock.png").toExternalForm());
        unlock = new Image(getClass().getResource("Icons/unlock.png").toExternalForm());
        resize = new Image(getClass().getResource("Icons/resizer.png").toExternalForm());

        // Set the images
        btnClose.setGraphic(new ImageView(close));
        btnSettings.setGraphic(new ImageView(settings));
        btnAdd.setGraphic(new ImageView(add));
        tgeBtnLock.setGraphic(new ImageView(lock));
        imgViewResizer.setImage(resize);

        // Set the textarea's text
        txtArea.setText(text);

        txtArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
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
//        getStageRef();

        // Close the stage
        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    // Handle the add button's OnAction event
    @FXML
    public void handleBtnAdd() {
        try {
            new Stickynote();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    // Handle the lock toggle button's OnAction event
    @FXML
    public void handleTgeBtnLock() {
        // Change the button's image and the textarea's editable property
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
//        getStageRef();

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
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Main.savaData, true))) {
            // Save the location
            output.writeInt(stageId);
            output.writeInt(0);
            output.writeDouble(stage.getX());

            output.writeInt(stageId);
            output.writeInt(1);
            output.writeDouble(stage.getY());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    // Handle the resizer's (ImageView) OnMousePressed event
    @FXML
    public void handleImgViewResizerMousePress() {
//        getStageRef();
    }

    // Handle the resizer's (ImageView) OnMouseDragged event
    @FXML
    public void handleImgViewResizerMouseDrag(MouseEvent e
    ) {
        // Change the stage's size
        stage.setWidth(e.getSceneX());
        stage.setHeight(e.getSceneY());
    }

    // Handle the resizer's (ImageView) OnMouseReleased event
    @FXML
    public void handleImgViewResizerMouseRelease() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Main.savaData, true))) {
            // Save the size
            output.writeInt(stageId);
            output.writeInt(2);
            output.writeDouble(stage.getWidth());
            
            output.writeInt(stageId);
            output.writeInt(3);
            output.writeDouble(stage.getHeight());
        } catch (IOException ex) {

        }
    }

    /*public String loadTextData() {
        String text = "";

        // Get the text data
        File file = new File(parentDir, "TextData");

        if (file.exists()) {
            try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {

                if (input.available() != 0) {
                    // Get the text
                    text = input.readUTF();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        return text;
    }*/
    public void saveText() {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Main.savaData, true))) {
            // Save the text
            output.writeInt(stageId);
            output.writeInt(4);
            output.writeUTF(txtArea.getText());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void getStageRef() {
        // Get the stage's reference
        stage = (Stage) btnClose.getScene().getWindow();
    }
}
