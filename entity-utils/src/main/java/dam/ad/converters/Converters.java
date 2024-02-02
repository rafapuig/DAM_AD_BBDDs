package dam.ad.converters;

import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Será un conversor de conversores
 * Podrá registrar conversiones por la anotación RowConvertible
 */
public class Converters {
    private static final Map<Class<?>, RowConverter<?>> converterMap =
            new HashMap<>();

    public static <T> void registerConverter(Class<T> type) {
        RowConverter<T> converter = new DefaultDTORowConverter<>(type);
        converterMap.put(type, converter);
    }

    public static <T> void registerConverter(Class<T> type, RowConverter<T> converter) {
        converterMap.putIfAbsent(type, converter);
    }

    public static <T> RowConverter<T> getConverter(T t) {
        return getConverter((Class<T>) t.getClass());
    }

    public static <T> RowConverter<T> getConverter(Class<T> tClass) {
        if (!converterMap.containsKey(tClass)) {
            registerConverter(tClass);
        }
        return (RowConverter<T>) converterMap.get(tClass);
    }

    public static <T> String getAsRow(T t) {
        if(!t.getClass().isAnnotationPresent(RowConvertible.class)) return t.toString();
        RowConverter<T> rowConverter = getConverter(t);
        if (rowConverter != null) {
            return rowConverter.getAsRow(t);
        }
        return t.toString();
    }

    public static <T> int[] getColumnLengths(Class<T> type) {

        Field[] fields = type.getDeclaredFields();

        int[] columnLengths = new int[fields.length];

        //RowConvertible[] rca = type.getAnnotationsByType(RowConvertible.class);
        //if(rca.length == 0) return columnLengths;

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            RowField[] rowFields = fields[i].getAnnotationsByType(RowField.class);
            columnLengths[i] = rowFields.length > 0 ? rowFields[0].columnLength() : 0;
        }
        return columnLengths;
    }


   /* public static String convertToRow(Object object) {
        RowConverter rowConverter = getConverter(object);  // converterMap.get(object.getClass());
        if (rowConverter != null) {
            try {
                return rowConverter.getAsRow(object);
            } catch (Exception e) {

            }
        }
        return object.toString();
    }*/
}
