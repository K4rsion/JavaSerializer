package framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serializer serializes object or list of objects.
 */
public class Serializer {
    private static final Set<Integer> serializedObjects = new HashSet<>();

    /**
     * Serialize object.
     *
     * @param object Object to serialize.
     * @return HashMap of serialized object.
     */
    public static HashMap<String, Object> serialize(Object object) {
        HashMap<String, Object> map = new HashMap<>();
        parse(object, map);
        return map;
    }

    /**
     * Serialize list of objects.
     *
     * @param objects List of objects to serialize.
     * @return List of serialized objects.
     */
    public static ArrayList<HashMap<String, Object>> serializeList(ArrayList<?> objects) {
        ArrayList<HashMap<String, Object>> maps = new ArrayList<>();
        for (Object object : objects) {
            HashMap<String, Object> map = serialize(object);
            maps.add(map);
            serializedObjects.clear();
        }
        return maps;
    }


    /**
     * Parse all fields of object and create a HashMap from them.
     *
     * @param object Object to parse.
     * @param map    Map to parse in.
     */
    private static void parse(Object object, HashMap<String, Object> map) {
        serializedObjects.add(object.hashCode());
        map.put("__id__", object.hashCode());
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (var field : fields) {
                field.setAccessible(true);
                try {
                    if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")
                            && !field.getType().isArray() && !List.class.isAssignableFrom(field.getType())) {
                        if (field.get(object) == null) {
                            map.put(field.getName(), field.get(object));
                        } else if (!serializedObjects.contains(field.get(object).hashCode())) {
                            HashMap<String, Object> thisMap = new HashMap<>();
                            map.put(field.getName(), thisMap);
                            parse(field.get(object), thisMap);
                        } else {
                            map.put(field.getName(), field.get(object).hashCode());
                        }
                    } else if (field.getType().getComponentType() != null && !field.getType().getComponentType().isPrimitive()) {
                        if (field.getType().isArray() || List.class.isAssignableFrom(field.getType())) {
                            Object fieldValue = field.get(object);
                            if (fieldValue == null) {
                                map.put(field.getName(), null);
                            } else {
                                ArrayList<Object> arrayValues = new ArrayList<>();
                                map.put(field.getName(), arrayValues);
                                if (field.getType().isArray()) {
                                    Object[] array = (Object[]) fieldValue;
                                    for (Object element : array) {
                                        if (!element.getClass().isPrimitive() && !element.getClass().getName().startsWith("java.")) {
                                            if (!serializedObjects.contains(element.hashCode())) {
                                                HashMap<String, Object> thisMap = new HashMap<>();
                                                parse(element, thisMap);
                                                arrayValues.add(thisMap);
                                            } else {
                                                arrayValues.add(element.hashCode());
                                            }
                                        }
                                    }
                                } else if (List.class.isAssignableFrom(field.getType())) {
                                    List<?> list = (List<?>) fieldValue;
                                    for (Object element : list) {
                                        if (!element.getClass().isPrimitive() && !element.getClass().getName().startsWith("java.")) {
                                            if (!serializedObjects.contains(element.hashCode())) {
                                                HashMap<String, Object> thisMap = new HashMap<>();
                                                parse(element, thisMap);
                                                arrayValues.add(thisMap);
                                            } else {
                                                arrayValues.add(element.hashCode());
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        int modifiers = field.getModifiers();
                        if (!java.lang.reflect.Modifier.isTransient(modifiers)
                                && !java.lang.reflect.Modifier.isStatic(modifiers)) {
                            map.put(field.getName(), field.get(object));
                        }
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Error trying to access field of " + object);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

}
