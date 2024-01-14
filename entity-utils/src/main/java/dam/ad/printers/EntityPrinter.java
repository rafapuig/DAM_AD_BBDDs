package dam.ad.printers;

import dam.ad.converters.RowConverter;

import java.io.PrintWriter;
import java.util.stream.Stream;

public interface EntityPrinter<T> {

    EntityHeaderProvider getHeaderProvider();

    default void printHeader() {
        getWriter()
                .println(getHeaderProvider()
                        .getHeader(true));
    }

    RowConverter<T> getRowConverter();

    PrintWriter getWriter();

    default void printRow(T t) {
        getWriter().println(getRowConverter().getAsRow(t));
    }

    default void print(Stream<T> entities) {
        printHeader();

        entities
                .map(getRowConverter()::getAsRow)
                .forEach(getWriter()::println);

        getWriter().println();
    }

}
