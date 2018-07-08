/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickynotes;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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

    static Map<String, Integer> stageMap = new HashMap<>();

    // Set the parent directory
    static File savaData = new File(System.getProperty("user.dir") + "/StickynotesData");

    Map<Integer, Map<Integer, Object>> stageDataMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws Exception {
        loadStageData();

        if (stageDataMap.isEmpty()) {
            new Stickynote();
        } else {
            for (Map.Entry<Integer, Map<Integer, Object>> entry : stageDataMap.entrySet()) {
                double x = 0, y = 0, width = 0, height = 0;
                String text = "";

                Integer id = entry.getKey();
                Map<Integer, Object> dataMap = entry.getValue();

                for (Map.Entry<Integer, Object> entry1 : dataMap.entrySet()) {
                    Integer dataType = entry1.getKey();
                    Object data = entry1.getValue();

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

                new Stickynote(id, x, y, width, height, text);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void loadStageData() throws IOException {
        // Get the stage's data
        if (savaData.exists()) {
            try (DataInputStream input = new DataInputStream(new FileInputStream(savaData))) {
                while (input.available() != 0) {
                    int id = input.readInt();
                    int dataType = input.readInt();
                    Object data;

                    if (dataType != 4) {
                        data = input.readDouble();
                    } else {
                        data = input.readUTF();
                    }

                    Map<Integer, Object> map = stageDataMap.get(id);

                    if (map == null) {
                        map = new HashMap<>();
                        stageDataMap.put(id, map);
                    }

                    map.put(dataType, data);
                }
            }
        }
    }
}
