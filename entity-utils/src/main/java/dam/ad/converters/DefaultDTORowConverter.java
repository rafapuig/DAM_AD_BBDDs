package dam.ad.converters;

import java.lang.reflect.Field;
import java.util.StringJoiner;

public class DefaultDTORowConverter<T> implements RowConverter<T> {
    Class<T> type;
    Field[] fields;
    int[] columnLengths;
    String[] formats;

    int maxLength;

    public DefaultDTORowConverter(Class<T> type, int... columnLengths) {
        this.columnLengths = columnLengths;
        this.type = type;
        fields = this.type.getDeclaredFields();
        formats = new String[columnLengths.length];

        for (Field field : fields) {
            field.setAccessible(true);
        }
        maxLength = Integer.min(columnLengths.length, fields.length);

        for (int i = 0; i < maxLength; i++) {
            //String align = fields[i].getType().isAssignableFrom() .equals(String.class) ? "-" : "";
            String align = Number.class.isAssignableFrom(fields[i].getType()) ? "" : "-";
            formats[i] = "%" + align + columnLengths[i] + "s";
        }
    }

    @Override
    public String getAsRow(T t) {
        try {
            StringJoiner sj = new StringJoiner(" ");

            for (int i = 0; i < maxLength; i++) {
                if(columnLengths[i] > 0) {
                    Object fieldValue = fields[i].get(t);
                    String text = Converters.convert(fieldValue);
                    sj.add(String.format(formats[i], text));
                }
            }
            return sj.toString();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
