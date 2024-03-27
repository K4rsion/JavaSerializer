package framework;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Deserializer2000 {
    private static final HashMap<Integer, Object> objectIdMap = new HashMap<>();

    public static Object deserialize(Class<?> clazz, Map<String, Object> map) throws Exception {
        createEmptyObjects(clazz, map);
        return setFields(clazz, map);
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

    private static void createEmptyObjects(Class<?> clazz, Map<String, Object> map) throws Exception {
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
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
