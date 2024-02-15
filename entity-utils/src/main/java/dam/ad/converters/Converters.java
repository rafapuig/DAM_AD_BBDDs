package dam.ad.converters;

import dam.ad.dto.annotations.RowConvertible;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Será un conversor de conversores
 * Podrá registrar conversiones por la anotación RowConvertible
 */
public class Converters {

    static {
        converterMap = new HashMap<>();
        registerConverter(Float.class, new FloatRowConverter());
    }


    private static final Map<Class<?>, RowConverter<?>> converterMap;


    public static <T> void registerConverter(Class<T> type) {
        RowConverter<T> converter = new DefaultDTORowConverter<>(type);
        converterMap.put(type, converter);
    }

    public static <T> void registerConverter(Class<T> type, RowConverter<T> converter) {
        //System.out.println("Registering RowConverter for class " + type.getName());
        converterMap.putIfAbsent(type, converter);
    }

    public static <T> RowConverter<T> getConverter(T t) {
        return getConverter((Class<T>) t.getClass());
    }

    public static <T> RowConverter<T> getConverter(Class<T> tClass) {


        //System.out.println("Obteniendo el conversor de " + tClass.getName());


        if (!converterMap.containsKey(tClass)) {
            registerConverter(tClass);
        }
        return (RowConverter<T>) converterMap.get(tClass);
    }

    public static <T> String getAsRow(T t) {

        Class<?> type = t.getClass();

        while (!type.isAnnotationPresent(RowConvertible.class) && type.getSuperclass() != Object.class) {

            type = type.getSuperclass();
            // System.out.println("Tipo... " + type.getName());
        }

       if (!type.isAnnotationPresent(RowConvertible.class)) {
           if(t instanceof Double) return new DecimalFormat("#,##0.000").format(t);
           if(t instanceof Long) return new DecimalFormat("#,##0").format(t);
            return t.toString();
        }

        RowConverter<T> rowConverter = (RowConverter<T>) getConverter(type); // antes getConverter(t);
        if (rowConverter != null) {
            return rowConverter.getAsRow(t);
        }
        return t.toString();
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
