package framework;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import ru.nsu.kgurin.Person;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deserializer {
    private static final HashMap<Integer, Object> objectIdMap = new HashMap<>();

    public static Object deserialize(Class<?> clazz, Map<String, Object> map) throws Exception {
        createEmptyObjects(clazz, map);
        return setFields(clazz, map);
    }

    public static ArrayList<Object> deserializeList(Class<?> clazz, ArrayList<HashMap<String, Object>> maps) throws Exception {
        ArrayList<Object> objects = new ArrayList<>();
        for (var map : maps) {
            createEmptyObjects(clazz, map);
            objects.add(setFields(clazz, map));
        }
        return objects;
    }

    private static Object setFields(Class<?> clazz, Map<String, Object> map) throws Exception {
        Object object = objectIdMap.get(map.get("__id__"));

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    Object value = map.get(field.getName());
                    if (value instanceof Map) {
                        Object innerObject = setFields(field.getType(), (Map<String, Object>) map.get(field.getName()));
                        field.set(object, innerObject);
                    } else if (field.getType().isArray() || List.class.isAssignableFrom(field.getType())) {
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
                            field.set(object, array);
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
                            field.set(object, arrayValues);
                        }
                    } else if (value.getClass().getSimpleName().equals("Integer")
                            && !field.getType().getSimpleName().equals("int")
                            && !field.getType().getSimpleName().equals(value.getClass().getSimpleName())) {
                        Object innerObject = objectIdMap.get(value);
                        field.set(object, innerObject);
                    } else {
                        field.set(object, map.get(field.getName()));
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return object;
    }

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
                            createEmptyObjects(field.getType(), (Map<String, Object>) map.get(field.getName()));
                        } else if (field.getType().isArray() || List.class.isAssignableFrom(field.getType())) {
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
                clazz = clazz.getSuperclass();
            }
        }
    }
}
