package dam.ad.converters;

import java.util.HashMap;
import java.util.Map;

/**
 * Será un conversor de conversores
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

    public static  String convert(Object object) {
        RowConverter rowConverter = converterMap.get(object.getClass());
        if(rowConverter != null) {
            return rowConverter.getAsRow(object);
        }
        return object.toString();
    }
}
