package dam.ad.reflection;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class FieldsExtractor {
    Class<?> tClass;

    public FieldsExtractor(Class<?> tClass) {
        this.tClass = tClass;
        generateFieldsList();
    }

    public void generateFieldsList() {
        if(classHierarchy == null) generateClassHierarchyStack();
        fields = generateFieldsList(classHierarchy);
    }

    public static List<Field> generateFieldsList(Class<?> aClass) {
         return generateFieldsList(
                 generateClassHierarchyStack(aClass)
         );
    }

    public static List<Field> generateFieldsList(Deque<Class<?>> classHierarchy) {
        return classHierarchy.stream()
                .map(FieldsExtractor::getFields)
                .flatMap(Collection::stream)
                .toList();
    }

    @Getter
    private List<Field> fields;


    public static List<Field> getFields(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        return List.of(fields);
    }

    @Getter
    private Deque<Class<?>> classHierarchy;

    public void generateClassHierarchyStack() {
        classHierarchy = generateClassHierarchyStack(tClass);
    }

    public static Deque<Class<?>> generateClassHierarchyStack(Class<?> aClass) {
        Deque<Class<?>> classHierarchy = new ArrayDeque<>();
        while (!aClass.equals(Object.class)) {
            classHierarchy.push(aClass);
            aClass = aClass.getSuperclass();
        }
        return classHierarchy;
    }

}
