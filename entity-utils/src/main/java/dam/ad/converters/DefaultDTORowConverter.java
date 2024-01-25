package dam.ad.converters;

import dam.ad.dto.annotations.RowField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.StringJoiner;

public class DefaultDTORowConverter<T> implements RowConverter<T> {
    Class<T> type;
    Field[] fields;
    int[] columnLengths; //La idea es obtenerlas por anotaciones en runtime
    String[] formats;

    int maxLength;

    public DefaultDTORowConverter(Class<T> type) {
        this(type, getColumnLengths(type));
    }

    private static <T> int[] getColumnLengths(Class<T> type) {
        Field[] fields = type.getDeclaredFields();

        int[] columnLengths = new int[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            RowField[] rowFields = fields[i].getAnnotationsByType(RowField.class);
            columnLengths[i] = rowFields.length > 0 ? rowFields[0].columnLength() : 0;
        }
        return columnLengths;
    }

    public DefaultDTORowConverter(Class<T> type, int... columnLengths) {
        this.columnLengths = columnLengths;
        this.type = type;
        fields = this.type.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
        }
        maxLength = Integer.min(columnLengths.length, fields.length);
        formats = new String[maxLength];

        for (int i = 0; i < maxLength; i++) {
            Class<?> fieldType = fields[i].getType();
            int align = getAlignment(fieldType);
            int columnLength = columnLengths[i];
            formats[i] = "%" + (align * columnLength) + "s";
            System.out.println(formats[i]);
        }
    }

    private static int getAlignment(Class<?> type) {
        if(type.isPrimitive() && !type.equals(boolean.class)) return 1;
        if(Number.class.isAssignableFrom(type)) return 1;
        return -1;
    }

    @Override
    public String getAsRow(T t) {
        try {
            StringJoiner sj = new StringJoiner(" ");

            for (int i = 0; i < maxLength; i++) {
                if (columnLengths[i] > 0) {
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
