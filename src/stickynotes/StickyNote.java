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
public class StickyNote {

    // Calculate the total number of stickynote
    static int number;

    // Store all stickynotes
    static List<StickyNote> stickynoteList = new ArrayList<>();

    // Set the default location and size of the stage
    static Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    static final double DEFAULT_WIDTH = 250, DEFAULT_HEIGHT = 250;
    static final double DEFAULT_X = screenBounds.getWidth() / 2 - DEFAULT_WIDTH / 2, DEFAULT_Y = screenBounds.getHeight() / 2 - DEFAULT_HEIGHT / 2;

    // Set the default CSS of the stage
    static final String DEFAULT_FONT = "-fx-font: 18.0 \"System\";", DEFAULT_FONT_COLOR = "-fx-text-inner-color: black;",
            DEFAULT_BACKGROUND_COLOR = "-fx-background-color: #FFD200;", DEFAULT_TEXTARAE_COLOR = "-fx-control-inner-background: #FFD200;",
            TXTAREA_BACKGROUND_COLOR = "-fx-background-color:  transparent;";

    // For storing the stage data
    static Map<Integer, Map<Integer, Object>> stageDataMap = new HashMap<>();

    // Set the save file
    static File saveFile = new File(System.getProperty("user.dir") + "/StickynotesData");

    // Record the stage's id
    int id;

    // Create a new stage
    Stage stage = new Stage();

    // Loading icon
    Image icon = new Image(getClass().getResource("Icons/indicator-stickynotes.png").toExternalForm());

    public StickyNote() {
        this(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT,
                "", DEFAULT_FONT, DEFAULT_FONT_COLOR, DEFAULT_BACKGROUND_COLOR, DEFAULT_TEXTARAE_COLOR);
    }

    public StickyNote(double x, double y, double width, double height,
            String text, String font, String fontColor, String backgroundColor, String textAreaColor) {
        this.id = number;
        number++;

        initStage(x, y, width, height, text, font, fontColor, backgroundColor, textAreaColor);
        updateStageData(x, y, width, height, text, font, fontColor, backgroundColor, textAreaColor);
    }

    public void initStage(double x, double y, double width, double height,
            String text, String font, String fontColor, String backgroundColor, String textAreaColor) {
        try {
            // Get the FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UI/StickyNoteUI.fxml"));

            StickyNoteController controller = new StickyNoteController(id, stage, text, font, fontColor, backgroundColor, textAreaColor);
            loader.setController(controller);

            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Set the stage's title
            stage.setTitle("Sticky-Notes");

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
            if (width <= 150) {
                width = 151;
            }

            if (height <= 150) {
                height = 151;
            }

            stage.setWidth(width);
            stage.setHeight(height);

            // Show the stage
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateStageData(double x, double y, double width, double height,
            String text, String font, String fontColor, String backgroundColor, String textAreaColor) {
        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(saveFile, true))) {
            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("x"));
            output.writeDouble(x);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("y"));
            output.writeDouble(y);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("width"));
            output.writeDouble(width);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("height"));
            output.writeDouble(height);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("text"));
            output.writeUTF(text);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("font"));
            output.writeUTF(font);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("font-color"));
            output.writeUTF(fontColor);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("background-color"));
            output.writeUTF(backgroundColor);

            output.writeInt(id);
            output.writeInt(StickyNoteController.dataTypeMap.get("textArea-color"));
            output.writeUTF(textAreaColor);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
