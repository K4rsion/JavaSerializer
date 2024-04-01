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
import java.util.Objects;

public class JsonFilter {
    public int someField = 0;
    public static void main(String[] args) {
        try {
            File inputFile = new File("src/main/resources/input.json");
            File outputFile = new File("src/main/resources/output.json");

            FileReader reader = new FileReader(inputFile);
            JSONTokener token = new JSONTokener(reader);
            //проверить на ленивые вычисления
            JSONArray jsonArray = new JSONArray(token);

            // Создаем пустую HashMap для хранения отфильтрованных данных
            ArrayList<JSONObject> filteredData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object jsonObject = jsonArray.get(i);

                //переписать в enum, перегрузка функции
                //про selectorы reflection
                //можно генерировать строку селектором
                if(isCorrectJsonObject((JSONObject) jsonObject, "age", "in", "person1")) {
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
    private static Boolean isCorrectJsonObject(JSONObject jsonObject, String field, String operator, Object fieldValue) throws JSONException {
        String[] keys = JSONObject.getNames(jsonObject);
        if (keys != null) {
            for (String key : keys) {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject innerObject) {
                    // Рекурсивно фильтруем вложенные объекты и добавляем результаты в отфильтрованный объект
                    isCorrectJsonObject(innerObject, field, operator, fieldValue);
                } else if (operator.equals("in")) {
                    return jsonObject.has((String) fieldValue);
                    // Если ключ соответствует заданному полю, сравниваем его значение
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
            default -> false; // Invalid operator
        };
    }
}
