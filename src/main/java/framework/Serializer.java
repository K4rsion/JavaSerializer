package framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Serializer {
    private static Set<Integer> serializedObjects = new HashSet<>();

    public static void serialize(Object object, HashMap<String, Object> innerMap) {
        serializedObjects.add(object.hashCode());
        innerMap.put("__id__", object.hashCode());
        Class<?> clazz = object.getClass();

        // сериализую родителей
        parentSerialize(object.getClass(), object, innerMap);

        parse(clazz, object, innerMap);
    }

    private static void parentSerialize(Class<?> clazz, Object object, HashMap<String, Object> innerMap) {
        Class<?> superClass = clazz;
        if (superClass.getSuperclass() != null) {
            superClass = clazz.getSuperclass();
            parentSerialize(superClass, object, innerMap);
        }

        parse(superClass, object, innerMap);
    }

    private static void parse(Class<?> clazz, Object object, HashMap<String, Object> outterMap) {
        Field[] fieldsd = clazz.getFields();
        Field[] fields = clazz.getDeclaredFields();
        for (var field : fields) {
            field.setAccessible(true);
            try {
                if (!(field.getType().isPrimitive() || field.getType().getName().startsWith("java."))) {
                    if (!serializedObjects.contains(field.get(object).hashCode())) {
                        HashMap<String, Object> map = new HashMap<>();
                        outterMap.put(field.getName(), map);
                        serialize(field.get(object), map);
                    } else {
                        // ссылка на объект
                        outterMap.put(field.getName(), field.get(object).hashCode());
                    }
                } else {
                    int modifiers = field.getModifiers();

                    // Проверка на transient и static модификаторы
                    if (!java.lang.reflect.Modifier.isTransient(modifiers)
                            && !java.lang.reflect.Modifier.isStatic(modifiers)) {
                        outterMap.put(field.getName(), field.get(object));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
