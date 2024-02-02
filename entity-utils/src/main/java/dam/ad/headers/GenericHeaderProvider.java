package dam.ad.headers;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GenericHeaderProvider implements HeaderProvider {
    String headerText;

    @Override
    public String getHeader() {
        return headerText;
    }

    HeaderColumn[] headerColumns;

    public HeaderColumn[] getHeaderColumns() {
        return headerColumns;
    }


    public GenericHeaderProvider(String[] labels, int[] columnLengths) {
        this(IntStream.range(0, Math.min(labels.length, columnLengths.length))
                .mapToObj(i -> new HeaderColumn(labels[i], columnLengths[i])));
    }

    public GenericHeaderProvider(HeaderColumn... headerColumns) {
        this(Arrays.stream(headerColumns));
    }

    protected GenericHeaderProvider(Stream<HeaderColumn> headers) {
        headerText = generateHeader(headers);
    }

    protected static String generateHeader(Stream<HeaderColumn> headers) {
        return headers
                .map(HeaderColumn::format)
                .map(String::toUpperCase)
                .collect(Collectors.joining(" "));
    }


    public static void main(String[] args) {
        GenericHeaderProvider provider = new GenericHeaderProvider(
                new HeaderColumn("id", 3),
                new HeaderColumn("nombre", 30),
                new HeaderColumn("equipo", 10)
        );

        System.out.println(provider.getHeader(true));
    }
}
