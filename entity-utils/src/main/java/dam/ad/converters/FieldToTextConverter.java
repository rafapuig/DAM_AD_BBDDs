package dam.ad.converters;

import dam.ad.dto.annotations.RowField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.StringJoiner;

public class FieldToTextConverter {
    private static final String NULL_TEXT = "";

    /**
     * Obtiene el valor textual resultante que tiene el objeto en el campo indicado (o a traves del getter)
     * teniendo en cuenta las anotaciones que pueda tener el campo de la clase
     * @param field El campo de la clase del cual queremos obtener el valor
     * @param object instancia de la cual queremos obtener el valor del campo (o getter)
     * @return el texto resultante de procesar el valor del campo para la instancia proporcionada,
     * teniendo en cuanta las anotaciones del campo
     */
    String convert(Field field, Object object) {
        //Obtenemos el valor a través del propio campo o a través del getter del campo
        Object fieldValue = getFieldOrPropertyValue(field, object);

        //Por defecto el texto del campo es el toString del campo
        String fieldText = fieldValue == null ? NULL_TEXT : fieldValue.toString();

        String annotatedResultText = getTextFromAnnotatedField(field, fieldValue);

        if (annotatedResultText != null) fieldText = annotatedResultText;

        return fieldText;
    }



    Object getterValue(Field field, Object object) {
        try {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String getterName = "get" + capitalizedFieldName;
            Method getter = field.getDeclaringClass().getMethod(getterName, null);
            //Method method = type.getMethod(getterName,null);
            return getter.invoke(object, null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            System.out.println("Excepcion: " + e.getClass().getName() + " " + e.getMessage());
        }
        return null;
    }

    Object getFieldOrPropertyValue(Field field, Object object) {
        if (object == null) return null;
        try {
            field.setAccessible(true);
            //En principio, es el valor del campo
            Object fieldValue = field.get(object);

            //Pero si hay un getter obtener el valor a través del getter
            if (fieldValue == null) {    //Si el valor del campo es nulo
                fieldValue = getterValue(field, object); //Probamos con el getter
            }
            return fieldValue;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTextFromAnnotatedField(Field field, Object fieldValue) {

        RowField[] rowFields = field.getAnnotationsByType(RowField.class);
        if (rowFields.length <= 0) return null; // Si el campo no tiene anotaciones devolver null

        //Sí el campo está anotado el campo
        RowField rowField = rowFields[rowFields.length - 1];

        String format = rowField.numericFormat().isBlank() ? "#,##0.000" : rowField.numericFormat();
        if (format!=null && fieldValue instanceof Float) {
            return new DecimalFormat(format).format(fieldValue);
        }

        //Si el campo está anotado como complejo, convertir en fila el propio campo del objeto
        if (rowField.asComplex()) {
            return Converters.getAsRow(fieldValue); // Converters.convertToRow(fieldValue);
        } else {
            //¿Tiene una anotacion de expresion?
            String expression = rowField.expression(); //.toLowerCase();
            if (!expression.isBlank()) {
                return getObjectExpresionString(field.getType(), fieldValue, expression);
            }
        }
        return null;
    }

    private String getObjectExpresionString(Class<?> type, Object object, String expression) {
        StringJoiner text = new StringJoiner(" ");
        String[] fieldNames = expression.split("\\+");
        //TODO mejorar las expresiones para obtener mas combinaciones de valor
        for(String fieldName : fieldNames) {
            text.add(getTextFromFieldName(type, fieldName.trim(), object));
        }
        /*try {
            String fieldName = expression;
            Field field = type.getDeclaredField(fieldName); // fields[i].getType().getDeclaredField(fieldName);
            field.setAccessible(true);
            //System.out.println("Field value: " + object);

            //text = fieldValue==null ? NULL_TEXT :  field.get(fieldValue) == null ? getterValue(field, fieldValue).toString() : field.get(fieldValue).toString();

            Object fieldValue = getFieldOrPropertyValue(field, object);
            text = object == null ? NULL_TEXT : fieldValue == null ? NULL_TEXT : fieldValue.toString();

        } catch (NoSuchFieldException e) { //Si no se ha encontrado el campo
            text = object == null ? NULL_TEXT : object.toString();  // Converters.convertToRow(fieldValue);
        }*/
        return text.toString();
    }
    private String getTextFromFieldName(Class<?> type, String fieldName, Object object) {
        String text;
        try {
            Field field = type.getDeclaredField(fieldName); // fields[i].getType().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = getFieldOrPropertyValue(field, object);
            text = object == null ? NULL_TEXT : fieldValue == null ? NULL_TEXT : fieldValue.toString();
        } catch (NoSuchFieldException e) { //Si no se ha encontrado el campo
            text = object == null ? NULL_TEXT : object.toString();// Converters.convertToRow(fieldValue);
        }
        return text;
    }

}
