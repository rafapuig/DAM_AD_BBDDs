package dam.ad.converters;

import dam.ad.dto.annotations.RowField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringJoiner;

public class DefaultDTORowConverter<T> implements RowConverter<T> {
    Class<T> type;
    Field[] fields; //TODO obtener todos los campos, en la jerarquia de herencia
    int[] columnLengths; //La idea es obtenerlas por anotaciones en runtime
    String[] formats;

    int maxLength;

    public DefaultDTORowConverter(Class<T> type) {
        this(type, getColumnLengths(type));
    }

    public static <T> int[] getColumnLengths(Class<T> type) {

        Field[] fields = type.getDeclaredFields();

        int[] columnLengths = new int[fields.length];

        //RowConvertible[] rca = type.getAnnotationsByType(RowConvertible.class);
        //if(rca.length == 0) return columnLengths;

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            RowField[] rowFields = fields[i].getAnnotationsByType(RowField.class);
            columnLengths[i] = rowFields.length > 0 ? rowFields[rowFields.length - 1].columnLength() : 0;
        }
        return columnLengths;
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
        this.type = type;
        this.columnLengths = columnLengths;
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



    @Override
    public String getAsRow(T t) {
        //System.out.println("Conversor para tipo " + this.type.getName());
        //System.out.println("Getting row for " + t + " of type " +  t.getClass().getName());

        StringJoiner sj = new StringJoiner(" ");

        for (int i = 0; i < maxLength; i++) {

            if (columnLengths[i] <= 0) continue;

            Field field = fields[i];

            //fields[i].setAccessible(true);

            String fieldText = new FieldToTextConverter().convert(field, t);

            sj.add(String.format(formats[i], fieldText));
        }
        return sj.toString();
    }




}
