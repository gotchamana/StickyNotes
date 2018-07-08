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
import java.util.HashMap;
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
        loadStageData();

        if (Stickynote.stageDataMap.isEmpty()) {
            Stickynote.stickynoteList.add(new Stickynote());
        } else {
            for (Map.Entry<Integer, Map<Integer, Object>> entry : Stickynote.stageDataMap.entrySet()) {
                // Set the default value
                double x = Stickynote.DEFAULT_X, y = Stickynote.DEFAULT_Y;
                double width = Stickynote.DEFAULT_WIDTH, height = Stickynote.DEFAULT_HEIGHT;
                String text = "";

                int id = entry.getKey();
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
                    }
                }

                if (id != -1) {
                    Stickynote.stickynoteList.add(new Stickynote(id, x, y, width, height, text));
                }
            }
            overrideOldStageData();
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
    }

    public void overrideOldStageData() {
        // Delete the old data
        Stickynote.saveFile.delete();

        for (Map.Entry<Integer, Map<Integer, Object>> entry : Stickynote.stageDataMap.entrySet()) {
            int id = entry.getKey();
            Map<Integer, Object> innerMap = entry.getValue();

            for (Map.Entry<Integer, Object> innerEntry : innerMap.entrySet()) {
                int dataType = innerEntry.getKey();
                Object data = innerEntry.getValue();

                try (DataOutputStream output = new DataOutputStream(new FileOutputStream(Stickynote.saveFile, true))) {
                    // Rewrite the new data
                    output.writeInt(id);
                    output.writeInt(dataType);

                    if (data instanceof Double) {
                        output.writeDouble((Double) data);
                    } else {
                        output.writeUTF((String) data);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
