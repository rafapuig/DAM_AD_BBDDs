package dam.ad.converters;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class ObjectArrayRowConverter implements RowConverter<Object[]> {

    int[] columnLengths;

    DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");

    public ObjectArrayRowConverter(int... columnLengths) {
        this.columnLengths = columnLengths;
    }

    @Override
    public String getAsRow(Object[] objects) {

        StringJoiner sj = new StringJoiner(" ");

        for (int i = 0; i < objects.length; i++) {

            int align = Number.class.isAssignableFrom(objects[i].getClass()) ? 1 : -1;

            String format = "%" + (columnLengths[i] * align) + "s";

            //System.out.println("Convirtiendo " + objects[i] + " en ..." + Converters.getAsRow(objects[i]) + "<--");

            String column = String.format(format, Converters.getAsRow(objects[i])); //String.format(format, objects[i]);
            sj.add(column);
        }
        return sj.toString();
    }
}
