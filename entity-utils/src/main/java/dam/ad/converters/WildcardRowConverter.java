package dam.ad.converters;

import java.util.List;
import java.util.StringJoiner;

public class WildcardRowConverter implements RowConverter<Object> {

    int[] columnLengths;
    public WildcardRowConverter(int... columnLengths) {
        this.columnLengths = columnLengths;
    }


    @Override
    public String getAsRow(Object o) {
        List<Object> objects = (List<Object>) o;
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
