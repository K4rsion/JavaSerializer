package framework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JsonWriter writes to JSON file json object.
 */
public class JsonWriter {
    /**
     * Write serialized object to JSON file.
     *
     * @param fileToWritePath File to write in.
     * @param map             Map which contains object.
     */
    public static void writeObject(String fileToWritePath, Map<String, Object> map) {
        try (FileWriter fileToWrite = new FileWriter(fileToWritePath)) {
            JSONObject json = new JSONObject(map);
            fileToWrite.write(json.toString());
        } catch (IOException e) {
            System.err.println("Error while opening file or writing object to file");
        }
    }

    /**
     * Write list of serialized objects to JSON file.
     *
     * @param fileToWritePath File to write in.
     * @param maps            List of map which contains object.
     */
    public static void writeObjects(String fileToWritePath, ArrayList<HashMap<String, Object>> maps) {
        try (FileWriter fileToWrite = new FileWriter(fileToWritePath)) {
            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, Object> map : maps) {
                JSONObject json = new JSONObject(map);
                jsonArray.put(json);
            }
            fileToWrite.write(jsonArray.toString());
        } catch (IOException e) {
            System.err.println("Error while opening file or writing objects to file");
        }
    }
}
