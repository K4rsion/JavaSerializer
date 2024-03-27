package framework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.json.JSONObject;
public class JsonWriter {
    public static void writeToJson(Map<String, Object> map) {
        try (FileWriter file = new FileWriter("output.json")) {
            // Convert map to JSON
            JSONObject json = new JSONObject(map);
            file.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
