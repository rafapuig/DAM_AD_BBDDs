package dam.ad.converters;

import java.util.List;
import java.util.StringJoiner;

public class ObjectListRowConverter implements RowConverter<List<Object>> {

    int[] columnLengths;
    public ObjectListRowConverter(int... columnLengths) {
        this.columnLengths = columnLengths;
    }

    @Override
    public String getAsRow(List<Object> objects) {
        StringJoiner sj =
                new StringJoiner(" ");

        for (int i = 0; i< objects.size(); i++) {
            String column =
                    String.format("%" + columnLengths[i] + "s", objects.get(i));
            sj.add(column);
        }
        return sj.toString();
    }
}
