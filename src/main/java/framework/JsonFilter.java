package framework;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * JsonFilter represents class which can filter JSON objects without serializing them.
 * There are two ways of using filter() method - with Operator and String operator.
 */
public class JsonFilter {

    /**
     * Filter objects from JSON file without serializing them.
     *
     * @param inputFilePath Input file path.
     * @param field         Field to compare with.
     * @param operator      Operator to compare.
     * @param fieldValue    Needed value of field.
     * @return ArrayList of filtered objects.
     */
    public static ArrayList<HashMap<String, Object>> filter(String inputFilePath, String field, Operator operator, String fieldValue) {
        ArrayList<JSONObject> filteredData = new ArrayList<>();
        try {
            FileReader reader = new FileReader(inputFilePath);
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
            hashedFilteredData.add(JsonReader.toMap(e));
        }

        return hashedFilteredData;
    }

    /**
     * Filter objects from JSON file without serializing them.
     *
     * @param inputFilePath Input file path.
     * @param field         Field to compare with.
     * @param operator      Operator to compare.
     * @param fieldValue    Needed value of field.
     * @return ArrayList of filtered objects.
     */
    public static ArrayList<HashMap<String, Object>> filter(String inputFilePath, String field, String operator, String fieldValue) {
        ArrayList<JSONObject> filteredData = new ArrayList<>();
        try {
            FileReader reader = new FileReader(inputFilePath);
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
            hashedFilteredData.add(JsonReader.toMap(e));
        }
        return hashedFilteredData;
    }

    /**
     * Check if objects from JSON file have the "field" parameter.
     *
     * @param inputFilePath File path to read objects from.
     * @param field         Field to check for entry.
     * @return True - if field exists, else - False.
     */
    public static Boolean inside(String inputFilePath, String field) {
        return !filter(inputFilePath, field, "in", null).isEmpty();
    }

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
}