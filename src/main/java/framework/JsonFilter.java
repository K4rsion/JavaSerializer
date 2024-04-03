package framework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class has been written for filtering JSON files.
 */
public class JsonFilter {
    /**
     * This is the main method that we can call to filter an input JSON file.
     * @param inputFile - input JSON file
     * @param field - field in JSON for comparing value of it and filtering
     * @param operator - comparison operator(here we use its value from enum)
     * @param fieldValue - value of the above-mentioned field
     * @return - returns HashMap of filtered JSON data
     */
    public static ArrayList<HashMap<String, Object>> filter(File inputFile, String field, Operator operator, String fieldValue) {
        ArrayList<JSONObject> filteredData = new ArrayList<>();
        try {
            FileReader reader = new FileReader(inputFile);
            JSONTokener token = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(token);
            String[] fieldParts = field.split("\\.");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject innerJsonObject = new JSONObject();
                if (fieldParts.length > 1) {
                    innerJsonObject = (JSONObject) jsonObject.get(fieldParts[0]);
                } else {
                    if (isCorrectJsonObject(jsonObject, fieldParts[0], operator, fieldValue)) {
                        filteredData.add((jsonObject));
                    }
                }
                for (int j = 1; j < fieldParts.length; j++) {
                    if (j == fieldParts.length - 1) {
                        if (isCorrectJsonObject(innerJsonObject, fieldParts[j], operator, fieldValue)) {
                            filteredData.add((jsonObject));
                        }
                    } else {
                        innerJsonObject = (JSONObject) innerJsonObject.get(fieldParts[j]);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error while reading JSON file");
        } catch (JSONException e) {
            System.err.println("Error trying to get JSON element");
        }

        ArrayList<HashMap<String, Object>> hashedFilteredData = new ArrayList<>();
        for (var e : filteredData) {
            try {
                hashedFilteredData.add(toMap(e));
            } catch (JSONException exception) {
                System.err.println("Error trying to convert JSON element");
            }
        }

        return hashedFilteredData;
    }

    /**
     * Same method as previous, but for String value of operator
     * @param inputFile - input JSON file
     * @param field - field in JSON for comparing value of it and filtering
     * @param operator - comparison operator(here we use its String value)
     * @param fieldValue - value of the above-mentioned field
     * @return - returns HashMap of filtered JSON data
     */
    public static ArrayList<HashMap<String, Object>> filter(File inputFile, String field, String operator, String fieldValue) {
        ArrayList<JSONObject> filteredData = new ArrayList<>();
        try {
            FileReader reader = new FileReader(inputFile);
            JSONTokener token = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(token);
            String[] fieldParts = field.split("\\.");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONObject innerJsonObject = new JSONObject();
                if (fieldParts.length > 1) {
                    innerJsonObject = (JSONObject) jsonObject.get(fieldParts[0]);
                } else {
                    if (isCorrectJsonObject(jsonObject, fieldParts[0], operator, fieldValue)) {
                        filteredData.add((jsonObject));
                    }
                }
                for (int j = 1; j < fieldParts.length; j++) {
                    if (j == fieldParts.length - 1) {
                        if (isCorrectJsonObject(innerJsonObject, fieldParts[j], operator, fieldValue)) {
                            filteredData.add((jsonObject));
                        }
                    } else {
                        innerJsonObject = (JSONObject) innerJsonObject.get(fieldParts[j]);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error while reading JSON file");
        } catch (JSONException e) {
            System.err.println("Error trying to get JSON element");
        }
        ArrayList<HashMap<String, Object>> hashedFilteredData = new ArrayList<>();
        for (var e : filteredData) {
            try {
                hashedFilteredData.add(toMap(e));
            } catch (JSONException exception) {
                System.err.println("Error trying to convert JSON element");
            }
        }
        return hashedFilteredData;
    }

    /**
     * This method checks for availability of field in the objects of JSON file
     * @param inputFile - input JSON file
     * @param field - field to check
     * @return - true if there is a field in the file, else false
     */
    public static Boolean inside(File inputFile, String field) {
        return !filter(inputFile, field, "in", null).isEmpty();
    }

    /**
     * This method checks for correctness of JSON object for adding to filtered data
     * @param jsonObject - JSON object to check
     * @param field - field in JSON for comparing value of it and filtering
     * @param operator - comparison operator(here we use its value from enum)
     * @param fieldValue - value of the above-mentioned field
     * @return - true if the object fits for conditions, else false
     * @throws JSONException if JSON object is incorrect
     */
    private static Boolean isCorrectJsonObject(JSONObject jsonObject, String field, Operator operator, Object fieldValue) throws JSONException {
        String[] keys = JSONObject.getNames(jsonObject);
        if (keys != null) {
            for (String key : keys) {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject innerObject) {
                    if (isCorrectJsonObject(innerObject, field, operator, fieldValue)) {
                        return true;
                    }
                } else if (key.equals(field) && compareValues(value, operator, fieldValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method do the same action as previous, but for String value of operator
     * @param jsonObject - JSON object to check
     * @param field - field in JSON for comparing value of it and filtering
     * @param operator - comparison operator(here we use its String value)
     * @param fieldValue - value of the above-mentioned field
     * @return - true if the object fits for conditions, else false
     * @throws JSONException if JSON object is incorrect
     */
    private static Boolean isCorrectJsonObject(JSONObject jsonObject, String field, String operator, Object fieldValue) throws JSONException {
        String[] keys = JSONObject.getNames(jsonObject);
        if (keys != null) {
            for (String key : keys) {
                Object value = jsonObject.get(key);
                if (operator.equals("in")) {
                    return jsonObject.has(field);
                } else if (value instanceof JSONObject innerObject) {
                    isCorrectJsonObject(innerObject, field, operator, fieldValue);
                } else if (key.equals(field) && compareValues(value, operator, fieldValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method exactly compares actual value of the field
     * with value that we entered to filter
     * @param compareValue - value that we entered
     * @param operator - comparison operator(here we use its String value)
     * @param factValue - actual value of the field
     * @return - true if comparing has been done successfully, else false
     */
    private static boolean compareValues(Object compareValue, String operator, Object factValue) {
        return switch (operator) {
            case "=" -> compareValue.equals(factValue);
            case "!=" -> !compareValue.equals(factValue);
            case ">" -> (Integer) compareValue > (Integer) factValue;
            case "<" -> (Integer) compareValue < (Integer) factValue;
            case ">=" -> (Integer) compareValue >= (Integer) factValue;
            case "<=" -> (Integer) compareValue <= (Integer) factValue;
            default -> false;
        };
    }

    /**
     * This method is same as previous? but fot enum value of operator
     * @param compareValue - value that we entered
     * @param operator - comparison operator(here we use its value from enum)
     * @param factValue - actual value of the field
     * @return - true if comparing has been done successfully, else false
     */
    private static boolean compareValues(Object compareValue, Operator operator, Object factValue) {
        return switch (operator) {
            case EQUALS -> compareValue.equals(factValue);
            case NOT_EQUALS -> !compareValue.equals(factValue);
            case GREATER_THAN -> (Integer) compareValue > (Integer) factValue;
            case LESS_THAN -> (Integer) compareValue < (Integer) factValue;
            case GREATER_THAN_OR_EQUALS -> (Integer) compareValue >= (Integer) factValue;
            case LESS_THAN_OR_EQUALS -> (Integer) compareValue <= (Integer) factValue;
        };
    }

    /**
     * This method is for converting filtered JSON objects to HashMap
     * @param jsonObject - filtered JSON object
     * @return - HashMap of filtered JSON objects
     * @throws JSONException - if some of JSON objects are incorrect(or something else)
     */
    private static HashMap<String, Object> toMap(JSONObject jsonObject) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        Iterator keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = (String) keysItr.next();
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
