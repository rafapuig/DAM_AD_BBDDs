package reflexion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

public class ReflexionGenericsDemo {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("Hola","Adios");

        System.out.println(
                ((ParameterizedType) stream.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

         Type persistentClass;

            persistentClass = ((ParameterizedType) stream.getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0];
        System.out.println(persistentClass.getClass().getSimpleName());

    }
}
