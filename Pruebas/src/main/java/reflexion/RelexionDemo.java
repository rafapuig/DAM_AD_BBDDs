package reflexion;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class RelexionDemo {

    record NombreCompleto(
            String nombre,
            String apellidos
    ) {
    }

    public static void main(String[] args) {

        NombreCompleto nombreCompleto1 = new NombreCompleto("Rafa", "Puig");
        NombreCompleto nombreCompleto2 = new NombreCompleto("Yestris", "Nieto");

        List<NombreCompleto> list = List.of(nombreCompleto1, nombreCompleto2);


        List<String> fieldNames = Arrays.stream(NombreCompleto.class.getDeclaredFields())
                .map(Field::getName)
                .map(String::toUpperCase)
                .toList();

        Integer[] fieldMinLength = Arrays.stream(NombreCompleto.class.getDeclaredFields())
                .map(Field::getName)
                .map(String::length)
                .toArray(Integer[]::new);

        System.out.println(fieldNames);

        Object o = nombreCompleto1;

        print(list, new int[]{15, 30});

    }

    private static void print(List<NombreCompleto> list, int[] lengths) {
        if (list.isEmpty()) return;

        Field[] fields = list.getFirst().getClass().getDeclaredFields();
        //Field[] fields = NombreCompleto.class.getDeclaredFields();


        StringJoiner header = new StringJoiner("");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);

            String columnHeader = String.format("%-" + lengths[i] + "s ", fields[i].getName().toUpperCase());
            header.add(columnHeader);
        }
        System.out.println(header);
        String line = "-".repeat(header.length());
        System.out.println(line);


        list.forEach(nc -> {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    System.out.printf("%-" + lengths[i] + "s ", fields[i].get(nc));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
        });
    }


}
