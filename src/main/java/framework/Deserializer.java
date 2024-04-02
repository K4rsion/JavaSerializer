package framework;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deserializer deserializes object or list of objects.
 */
public class Deserializer {
    private static final HashMap<Integer, Object> objectIdMap = new HashMap<>();

    /**
     * Deserialize Java object from JSON file.
     *
     * @param clazz Class of object that should be deserialized.
     * @param map   Result of JsonReader, JsonFilter.
     * @return Object that has been deserialized from JSON file.
     */
    public static Object deserialize(Class<?> clazz, Map<String, Object> map) {
        createEmptyObjects(clazz, map);
        return setFields(clazz, map);
    }

    /**
     * Deserialize list of Java objects from JSON file.
     *
     * @param clazz Class of objects that should be deserialized.
     * @param maps  Result of JsonReader, JsonFilter.
     * @return List of objects that have been deserialized from JSON file.
     */
    public static ArrayList<Object> deserializeList(Class<?> clazz, ArrayList<HashMap<String, Object>> maps) {
        ArrayList<Object> objects = new ArrayList<>();
        for (var map : maps) {
            createEmptyObjects(clazz, map);
            objects.add(setFields(clazz, map));
        }
        return objects;
    }

    /**
     * Set all fields to current object.
     *
     * @param clazz Class of object to set fields.
     * @param map   Map which represents current object.
     * @return Object with set fields.
     */
    private static Object setFields(Class<?> clazz, Map<String, Object> map) {
        Object object = objectIdMap.get(map.get("__id__"));

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    Object value = map.get(field.getName());
                    if (value instanceof Map) {
                        Object innerObject = setFields(field.getType(), (Map<String, Object>) value);
                        try {
                            field.set(object, innerObject);
                        } catch (IllegalAccessException e) {
                            System.err.println("Error trying to access field of " + object);
                        }
                    } else if (value != null && !value.toString().equals("null") && field.getType().getComponentType() != null && !field.getType().getComponentType().isPrimitive()) {
                        if (field.getType().isArray() || List.class.isAssignableFrom(field.getType())) {
                            ArrayList<Object> arrayValues = new ArrayList<>();
                            if (field.getType().isArray()) {
                                List<?> list = (List<?>) value;
                                for (Object element : list) {
                                    if (element instanceof Map) {
                                        arrayValues.add(objectIdMap.get(((Map<?, ?>) element).get("__id__")));
                                        setFields(field.getType().getComponentType(), (Map<String, Object>) element);
                                    } else {
                                        arrayValues.add(objectIdMap.get(element));
                                    }
                                }
                                Class<?> componentType = field.getType().getComponentType();
                                Object[] array = arrayValues.toArray((Object[]) Array.newInstance(componentType, arrayValues.size()));
                                try {
                                    field.set(object, array);
                                } catch (IllegalAccessException e) {
                                    System.err.println("Error trying to access field of " + object);
                                }
                            } else if (List.class.isAssignableFrom(field.getType())) {
                                List<?> list = (List<?>) value;
                                Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                                for (Object element : list) {
                                    if (element instanceof Map) {
                                        arrayValues.add(objectIdMap.get(((Map<?, ?>) element).get("__id__")));
                                        setFields((Class<?>) typeArguments[0], (Map<String, Object>) element);
                                    } else {
                                        arrayValues.add(objectIdMap.get(element));
                                    }
                                }
                                try {
                                    field.set(object, arrayValues);
                                } catch (IllegalAccessException e) {
                                    System.err.println("Error trying to access field of " + object);
                                }
                            }
                        }
                    } else if (value != null && value.getClass().getSimpleName().equals("Integer") && !field.getType().getSimpleName().equals("int") && !field.getType().getSimpleName().equals(value.getClass().getSimpleName())) {
                        Object innerObject = objectIdMap.get(value);
                        try {
                            field.set(object, innerObject);
                        } catch (IllegalAccessException e) {
                            System.err.println("Error trying to access field of " + object);
                        }
                    } else {
                        try {
                            if (value != null && map.get(field.getName()).toString().equals("null")) {
                                field.set(object, null);
                            } else {
                                field.set(object, map.get(field.getName()));
                            }
                        } catch (IllegalAccessException e) {
                            System.err.println("Error trying to access field of " + object);
                        }
                    }
                }

            }
            clazz = clazz.getSuperclass();
        }
        return object;
    }

    /**
     * Create empty object to set all fields in the future.
     *
     * @param clazz Class of object to create fields.
     * @param map   Map which represents object with empty fields.
     */
    private static void createEmptyObjects(Class<?> clazz, Map<String, Object> map) {
        if (!objectIdMap.containsKey((Integer) map.get("__id__"))) {
            Objenesis objenesis = new ObjenesisStd();
            Object object = objenesis.newInstance(clazz);
            objectIdMap.put((Integer) map.get("__id__"), object);


            while (clazz != null) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (map.containsKey(field.getName())) {
                        Object value = map.get(field.getName());
                        if (value instanceof Map) {
                            createEmptyObjects(field.getType(), (Map<String, Object>) value);
                        } else if (value != null && !value.toString().equals("null") && field.getType().getComponentType() != null && !field.getType().getComponentType().isPrimitive()) {
                            if (field.getType().isArray() || List.class.isAssignableFrom(field.getType())) {
                                if (field.getType().isArray()) {
                                    List<?> list = (List<?>) value;
                                    for (Object element : list) {
                                        if (element instanceof Map) {
                                            createEmptyObjects(field.getType().getComponentType(), (Map<String, Object>) element);
                                        }
                                    }
                                } else if (List.class.isAssignableFrom(field.getType())) {
                                    List<?> list = (List<?>) value;
                                    Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                                    for (Object element : list) {
                                        if (element instanceof Map) {
                                            createEmptyObjects((Class<?>) typeArguments[0], (Map<String, Object>) element);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }
}
