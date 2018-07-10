/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author shootingstar
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Change the default locale
        Locale.setDefault(Locale.ENGLISH);

        loadStageData();

        if (StickyNote.stageDataMap.isEmpty()) {
            StickyNote.stickynoteList.add(new StickyNote());
        } else {
            for (Map.Entry<Integer, Map<Integer, Object>> entry : StickyNote.stageDataMap.entrySet()) {
                // Set the default value
                double x = StickyNote.DEFAULT_X, y = StickyNote.DEFAULT_Y;
                double width = StickyNote.DEFAULT_WIDTH, height = StickyNote.DEFAULT_HEIGHT;
                String text = "", font = StickyNote.DEFAULT_FONT, fontColor = StickyNote.DEFAULT_FONT_COLOR,
                        backgroundColor = StickyNote.DEFAULT_BACKGROUND_COLOR, textAreaColor = StickyNote.DEFAULT_TEXTARAE_COLOR;

                Map<Integer, Object> dataMap = entry.getValue();

                for (Map.Entry<Integer, Object> innerEntry : dataMap.entrySet()) {
                    Integer dataType = innerEntry.getKey();
                    Object data = innerEntry.getValue();

                    // Filter the data
                    // 0 represents X's coordinate, 1 represents Y's coordinate, 2 represents width, 3 represents height, 4 represents text
                    switch (dataType) {
                        case 0:
                            x = (Double) data;
                            break;

                        case 1:
                            y = (Double) data;
                            break;

                        case 2:
                            width = (Double) data;
                            break;

                        case 3:
                            height = (Double) data;
                            break;

                        case 4:
                            text = (String) data;
                            break;

                        case 5:
                            font = (String) data;
                            break;

                        case 6:
                            fontColor = (String) data;
                            break;

                        case 7:
                            backgroundColor = (String) data;
                            break;

                        case 8:
                            textAreaColor = (String) data;
                            break;
                    }
                }

                StickyNote.stickynoteList.add(new StickyNote(x, y, width, height, text, font, fontColor, backgroundColor, textAreaColor));
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void loadStageData() {
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
                    
                    // Filter the data by data type
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

            } finally {
                // Delete the old data
                StickyNote.saveFile.delete();
            }
        }
    }
}
