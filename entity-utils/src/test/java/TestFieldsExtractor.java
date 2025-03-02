import dam.ad.reflection.FieldsExtractor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

@Disabled
public class TestFieldsExtractor {

    @Disabled
    @Test
    void testGenerateClassHierarchy() {

        FieldsExtractor fieldsExtractor = new FieldsExtractor(Empleado.class);

        fieldsExtractor.getClassHierarchy()
                .stream()
                .map(Class::getName)
                .forEach(System.out::println);

        System.out.println(fieldsExtractor.getClassHierarchy().pop().getName());
        System.out.println(fieldsExtractor.getClassHierarchy().pop().getName());
    }

    @Test
    void testGetFields() {
        FieldsExtractor.getFields(Person.class).stream()
                .map(Field::getName)
                .forEach(System.out::println);
    }

    @Test
    void testGetAllFields() {
        FieldsExtractor fieldsExtractor = new FieldsExtractor(Empleado.class);
        fieldsExtractor.getFields().stream()
                .map(Field::getName)
                .forEach(System.out::println);
    }

    @Test
    void testGetGenerateFieldsList() {
        FieldsExtractor.generateFieldsList(Empleado.class).stream()
                .map(Field::getName)
                .forEach(System.out::println);
    }


}
