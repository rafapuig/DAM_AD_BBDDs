package dam.ad.model.personas;

import dam.ad.converters.Converters;
import dam.ad.converters.DefaultDTORowConverter;
import dam.ad.dto.annotations.RowField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class TestAnnotations {
    public static void main(String[] args) {
        Class<?> cls = PersonaDTO.class;

        testAnnotations(cls);

        DefaultDTORowConverter<PersonaDTO> converter = new DefaultDTORowConverter<>(PersonaDTO.class);
        Converters.registerConverter(PersonaDTO.class, converter);

        Stream.of(
                new PersonaDTO(1,"Rafa", false),
                new PersonaDTO(2,"Emilio", true))
                .map(Converters::getAsRow)
                .forEach(System.out::println);
    }

    private static void testAnnotations(Class<?> cls) {
        Annotation[] all = cls.getAnnotations();

        System.out.println(all.length);
        for (int i = 0; i < all.length; i++) {
            System.out.println(all[i].toString());
        }

        Field[] fields = cls.getDeclaredFields();
        for(Field field : fields) {
            Annotation[] fieldAnns = field.getAnnotations();

            for (Annotation a : fieldAnns) {
                System.out.println(a.toString());
                RowField rowField = (RowField) a;
                System.out.println(rowField.label() + " " + rowField.columnLength());
            }
        }
    }
}
