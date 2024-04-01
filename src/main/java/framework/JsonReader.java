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

public class JsonReader {
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

    private static HashMap<String, Object> toMap(JSONObject jsonObject) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
                map.put(key, value);
            } else if (value instanceof JSONArray) {
                ArrayList<Object> arrayValues = new ArrayList<>();
                for (int i = 0; i < ((JSONArray) value).length(); i++) {
                    if (((JSONArray) value).get(i) instanceof JSONObject) {
                        arrayValues.add(toMap((JSONObject) ((JSONArray) value).get(i)));
                    } else {
                        arrayValues.add(((JSONArray) value).get(i));
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
