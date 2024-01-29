package dam.ad.printers;


import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

public class GenericDTOPrinter {

    public static <T> void print(List<T> list, int... lengths) {
        if (list.isEmpty()) return;

        Field[] fields = list.getFirst().getClass().getDeclaredFields();

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
                    String align = fields[i].getType().equals(String.class) ? "-" : "";
                    System.out.printf("%" + align + lengths[i] + "s ", fields[i].get(nc));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println();
        });
    }
}
