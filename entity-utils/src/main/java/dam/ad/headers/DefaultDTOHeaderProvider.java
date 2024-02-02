package dam.ad.headers;

import dam.ad.dto.annotations.RowField;
import dam.ad.printers.Printers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DefaultDTOHeaderProvider<T> extends GenericHeaderProvider {
    public DefaultDTOHeaderProvider(Class<T> type) {
        super(getHeaders(type));
    }

    private static <T> Stream<HeaderColumn> getHeaders(Class<T> type) {
        //System.out.println("Obteniendo headers del tipo " + type.getName() + "...");

        Field[] fields = type.getDeclaredFields();  //Campos del tipo T

        return Arrays.stream(fields)
                .flatMap(field -> generateHeaderColumn(field).stream());
    }


    public DefaultDTOHeaderProvider(Class<T> type, int... columnLengths) {
        super(getHeaders(type, columnLengths));
    }

    private static <T> Stream<HeaderColumn> getHeaders(Class<T> type, int[] columnLengths) {
        //System.out.println("Obteniendo headers del tipo " + type.getName() + " con longitud personalizada ...");
        Field[] fields = type.getDeclaredFields();

        int maxLength = Integer.min(columnLengths.length, fields.length);

        return IntStream.range(0, maxLength)
                //.filter(i -> columnLengths[i] > 0)
                //.peek(i -> fields[i].setAccessible(true))
                .mapToObj(i -> generateHeaderColumn(fields[i], columnLengths[i]))
                .flatMap(Optional::stream);
    }

    private static String getHeaderColumnLabel(Field field) {
        return getHeaderColumnLabel(field, getRowFieldAnnotation(field));
    }

    private static String getHeaderColumnLabel(Field field, RowField rowField) {

        if (rowField == null) return field.getName();

        //La etiqueta de la columna será (en principio) el nombre original en el código del campo
        // si no se ha dado valor al atributo label de la anotación RowField
        String columnLabel = rowField.label().isBlank() ? field.getName() : rowField.label();

        //Si se ha marcado el campo como complejo (es un tipo con sus propios campos)
        if (rowField.asComplex()) {
            System.out.println("El campo '" + field.getName() + "' es complejo, hay que registrar un " + field.getType().getName() + "...");
            columnLabel = Printers.getPrinter(field.getType()).getHeaderProvider().getHeader();
        }
        return columnLabel;
    }

    private static int getHeaderColumnLength(RowField rowField) {
        return getHeaderColumnLength(rowField, 0);
    }

    private static int getHeaderColumnLength(RowField rowField, int length) {
        if (length > 0 || rowField == null) return length;
        return rowField.columnLength();
    }

    private static int getHeaderColumnLength(Field field, int length) {
        return getHeaderColumnLength(getRowFieldAnnotation(field), length);
    }


    private static RowField getRowFieldAnnotation(Field field) {
        if (!field.isAnnotationPresent(RowField.class)) return null;
        RowField[] rowFieldAnnotations = field.getAnnotationsByType(RowField.class);
        return rowFieldAnnotations[rowFieldAnnotations.length - 1]; //Devolvemos la ultima de todas
    }

    private static Optional<HeaderColumn> generateHeaderColumn(Field field, int columnLength) {
        if(columnLength <= 0) return Optional.empty();

        String label = getHeaderColumnLabel(field);
        return Optional.of(new HeaderColumn(label, columnLength));
    }

    private static Optional<HeaderColumn> generateHeaderColumn(Field field) {
        RowField rowField = getRowFieldAnnotation(field);

        int length = getHeaderColumnLength(rowField);
        if (length <= 0) return Optional.empty();

        String label = getHeaderColumnLabel(field, rowField);
        return Optional.of(new HeaderColumn(label, length));
    }


}
