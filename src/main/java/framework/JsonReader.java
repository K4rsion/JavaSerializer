package framework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * JsonReader reads from JSON file json object to deserialize it further.
 */
public class JsonReader {

    /**
     * Read object from JSON file.
     *
     * @param fileFromReadPath File to read from.
     * @return Object.
     */
    public static HashMap<String, Object> readObject(String fileFromReadPath) {
        try {
            File fileFromRead = new File(fileFromReadPath);
            FileReader reader = new FileReader(fileFromRead);
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            return toMap(jsonObject);
        } catch (FileNotFoundException e) {
            System.err.println("Error trying to open file from read");
            return null;
        } catch (JSONException e) {
            System.err.println("No JSON object found in the file");
            return null;
        }
    }

    /**
     * Read list of objects from JSON file.
     *
     * @param fileFromReadPath File to read from.
     * @return List of objects.
     */
    public static ArrayList<HashMap<String, Object>> readObjects(String fileFromReadPath) {
        try {
            ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
            File fileFromRead = new File(fileFromReadPath);
            FileReader reader = new FileReader(fileFromRead);
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                maps.add(toMap(jsonObject));
            }
            return maps;
        } catch (FileNotFoundException e) {
            System.err.println("Error trying to open file from read");
            return null;
        } catch (JSONException e) {
            System.err.println("No JSON object found in the file");
            return null;
        }
    }

    /**
     * Convert JSONObject to map.
     *
     * @param jsonObject JSON object to convert.
     * @return HashMap object.
     */
    public static HashMap<String, Object> toMap(JSONObject jsonObject) {
        HashMap<String, Object> map = new HashMap<>();
        Iterator<?> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = (String) keysItr.next();
            Object value = null;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                System.err.println("Error trying to access field of object");
            }
            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
                map.put(key, value);
            } else if (value instanceof JSONArray) {
                ArrayList<Object> arrayValues = new ArrayList<>();
                for (int i = 0; i < ((JSONArray) value).length(); i++) {
                    try {
                        if (((JSONArray) value).get(i) instanceof JSONObject) {
                            arrayValues.add(toMap((JSONObject) ((JSONArray) value).get(i)));
                        } else {
                            arrayValues.add(((JSONArray) value).get(i));
                        }
                    } catch (JSONException e) {
                        System.err.println("Error trying to access field of object");
                    }
                }
                map.put(key, arrayValues);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
