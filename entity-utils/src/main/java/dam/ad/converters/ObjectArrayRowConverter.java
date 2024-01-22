package dam.ad.converters;

import java.util.StringJoiner;

public class ObjectArrayRowConverter implements RowConverter<Object[]> {

    int[] columnLengths;

    public ObjectArrayRowConverter(int... columnLengths) {
        this.columnLengths = columnLengths;
    }

    @Override
    public String getAsRow(Object[] objects) {

        StringJoiner sj = new StringJoiner(" ");

        for (int i = 0; i< objects.length; i++) {
            int align =  Number.class.isAssignableFrom(objects[i].getClass()) ? 1 : -1;
            String format = "%" + (columnLengths[i] * align) + "s";
            String column = String.format(format, objects[i]);
            sj.add(column);
        }
        return sj.toString();
    }
}
