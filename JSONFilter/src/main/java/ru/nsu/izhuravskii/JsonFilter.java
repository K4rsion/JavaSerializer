package ru.nsu.izhuravskii;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JsonFilter {
    public static void main(String[] args) {
        try {
            File inputFile = new File("src/main/resources/input.json");
            File outputFile = new File("src/main/resources/output.json");

            FileReader reader = new FileReader(inputFile);
            JSONTokener token = new JSONTokener(reader);
            //проверить на ленивые вычисления
            JSONArray jsonArray = new JSONArray(token);


            ArrayList<JSONObject> filteredData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object jsonObject = jsonArray.get(i);

                //переписать в enum, перегрузка функции
                //можно генерировать строку селектором
                if(isCorrectJsonObject((JSONObject) jsonObject, "age", Operator.IN,"age")) {
                    filteredData.add((JSONObject) jsonObject);
                }
            }

            // Записываем строку JSON в файл output.json
            FileWriter writer = new FileWriter(outputFile);
            writer.write(filteredData.toString());
            writer.close();

            reader.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    enum Operator {
        EQUALS("="),
        NOT_EQUALS("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN_OR_EQUALS("<="),
        IN("in");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    // Метод для фильтрации JSON объекта
    //in - оператор проверяет есть ли поле в объекте
    //обращение по уровням
    //exceptions в самом конце
    //два метода с одинаковым названием, но разными типами аргументов


    private static Boolean isCorrectJsonObject(JSONObject jsonObject, String field, Operator operator, Object fieldValue) throws JSONException {
        String[] keys = JSONObject.getNames(jsonObject);
        if (keys != null) {
            for (String key : keys) {
                Object value = jsonObject.get(key);
                if (operator.equals(Operator.IN)) {
                    return jsonObject.has((String) fieldValue);
                } else if (value instanceof JSONObject innerObject) {
                    isCorrectJsonObject(innerObject, field, operator, fieldValue);
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

    // Метод для сравнения значений в соответствии с оператором
    private static boolean compareValues(Object compareValue, Operator operator, Object factValue) {
        return switch (operator) {
            case EQUALS -> compareValue.equals(factValue);
            case NOT_EQUALS -> !compareValue.equals(factValue);
            case GREATER_THAN -> (Integer) compareValue > (Integer) factValue;
            case LESS_THAN -> (Integer) compareValue < (Integer) factValue;
            case GREATER_THAN_OR_EQUALS -> (Integer) compareValue >= (Integer) factValue;
            case LESS_THAN_OR_EQUALS -> (Integer) compareValue <= (Integer) factValue;
            default -> false;
        };
    }
}
