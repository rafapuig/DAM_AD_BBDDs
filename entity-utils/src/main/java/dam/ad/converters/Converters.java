package dam.ad.converters;

import java.util.HashMap;
import java.util.Map;

/**
 * Será un conversor de conversores
 * Podrá registrar conversiones por la anotacion RowConvertible
 */
public class Converters {
    private static final Map<Class<?>, RowConverter<?>> converterMap =
            new HashMap<>();

    //public Converters(Map<Class<?>, RowConverter<?>> converterMap) {
    //    this.converterMap = converterMap;
    //}

    public static  <T> void registerConverter(Class<T> type, RowConverter<T> converter) {
        converterMap.put(type, converter);
    }

    public static <T> RowConverter<T> getConverter(T t) {
        RowConverter<T> rowConverter = (RowConverter<T>) converterMap.get(t.getClass());
        return rowConverter;
    }

    public static <T> String getAsRow(T t) {
        RowConverter<T> rowConverter = getConverter(t);
        if(rowConverter != null) {
            return rowConverter.getAsRow(t);
        }
        return t.toString();
    }



    public static String convert(Object object) {
        RowConverter rowConverter = getConverter(object);  // converterMap.get(object.getClass());
        if(rowConverter != null) {
            return rowConverter.getAsRow(object);
        }
        return object.toString();
    }
}
