package dam.ad.printers;

import dam.ad.converters.Converters;

import java.lang.reflect.Field;
import java.util.StringJoiner;

public class DefaultDTOHeaderProvider<T> implements EntityHeaderProvider {
    Class<T> type;
    Field[] fields;
    int[] columnLengths;
    String[] formats;

    int maxLength;

    String header = "";

    public DefaultDTOHeaderProvider(Class<T> type, int... columnLengths) {

        this.columnLengths = columnLengths;

        this.type = type;
        fields = type.getDeclaredFields();

        maxLength = Integer.min(columnLengths.length, fields.length);

        StringJoiner header = new StringJoiner(" ");

        for (int i = 0; i < maxLength; i++) {

            fields[i].setAccessible(true);

            String format = "%-" + columnLengths[i] + "s";            

            String fieldName = fields[i].getName().toUpperCase();

            String columnHeader = String.format(format, fieldName);
            header.add(columnHeader);
        }

        this.header = header.toString();
    }

    @Override
    public String getHeader() {
        return this.header;
    }
}
