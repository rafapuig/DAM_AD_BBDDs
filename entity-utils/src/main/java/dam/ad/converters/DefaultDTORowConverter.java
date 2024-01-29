package dam.ad.converters;

import dam.ad.dto.annotations.RowField;
import dam.ad.printers.Printers;

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
        this(type, Converters.getColumnLengths(type));
    }

   /* private static <T> int[] getColumnLengths(Class<T> type) {
        Field[] fields = type.getDeclaredFields();

        int[] columnLengths = new int[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            RowField[] rowFields = fields[i].getAnnotationsByType(RowField.class);
            columnLengths[i] = rowFields.length > 0 ? rowFields[0].columnLength() : 0;
        }
        return columnLengths;
    }*/

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
            //System.out.println(formats[i]);
        }
    }

    private static int getAlignment(Class<?> type) {
        if (type.isPrimitive() && !type.equals(boolean.class)) return 1;
        if (Number.class.isAssignableFrom(type)) return 1;
        return -1;
    }

    public <T> T castear(Class<T> clazz, Object value) {
        return clazz.cast(value);
    }

    @Override
    public String getAsRow(T t) {
        try {
            StringJoiner sj = new StringJoiner(" ");

            for (int i = 0; i < maxLength; i++) {
                if (columnLengths[i] > 0) {
                    Object fieldValue = fields[i].get(t);
                    RowField[] rowFields = fields[i].getAnnotationsByType(RowField.class);
                    fields[i].setAccessible(true);

                    String text = fieldValue.toString(); //Por defecto es el toString del campo
                    if (rowFields.length > 0) { //Sí está anotado el campo
                        RowField rowField = rowFields[rowFields.length - 1];
                        if(rowField.asComplex()) {
                            text = Converters.getAsRow(fieldValue); // Converters.convertToRow(fieldValue);
                        } else {
                            try {
                                String fieldName = rowField.expression();
                                Field field = fields[i].getType().getDeclaredField(fieldName);
                                field.setAccessible(true);
                                text = field.get(fieldValue).toString();
                            } catch (NoSuchFieldException e) { //Si no se ha encontrado el campo
                                text = fieldValue.toString();  // Converters.convertToRow(fieldValue);
                            }
                        }
                    }
                    sj.add(String.format(formats[i], text));
                }
            }
            return sj.toString();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
