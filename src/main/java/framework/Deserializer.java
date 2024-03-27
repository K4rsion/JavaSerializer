package framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.stream.Stream;

public class Deserializer {
    public static Object buildObject(Class<?> clazz, Map<String, Object> map) throws Exception {
        Constructor<?> constructor = getAnnotatedConstructor(clazz);
        Object[] parameters = getParametersForConstructor(constructor, map);
        constructor.setAccessible(true);
        return constructor.newInstance(parameters);
    }

    private static Constructor<?> getAnnotatedConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Stream.of(constructor.getParameterAnnotations())
                    .allMatch(annotations -> Stream.of(annotations)
                            .anyMatch(annotation -> annotation.annotationType() == JsonValue.class))) {
                return constructor;
            }
        }
        throw new IllegalArgumentException("No constructor annotated with @JsonValue found");
    }


    private static Object[] getParametersForConstructor(Constructor<?> constructor, Map<String, Object> map) throws Exception {
        Parameter[] parametersFromConstructor = constructor.getParameters();
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parametersFromConstructor.length];
        for (int i = 0; i < parametersFromConstructor.length; i++) {
            JsonValue jsonValueAnnotation = parametersFromConstructor[i].getAnnotation(JsonValue.class);
            String key = jsonValueAnnotation.value();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                // проверка на parametersFromConstructor[i].getType().getSimpleName() == value.getClass().getSimpleName()
                if (value instanceof Map) {
                    parameters[i] = buildObject(parameterTypes[i], (Map<String, Object>) value);
                } else {
                    parameters[i] = map.get(key);
                }
            }
        }
        return parameters;
    }
}