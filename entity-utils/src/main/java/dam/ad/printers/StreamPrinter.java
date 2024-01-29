package dam.ad.printers;

import dam.ad.converters.RowConverter;
import dam.ad.headers.HeaderProvider;

import java.io.PrintWriter;
import java.util.stream.Stream;

public interface StreamPrinter<T> {

    HeaderProvider getHeaderProvider();

    RowConverter<T> getRowConverter();

    PrintWriter getWriter();


    default void printHeader(boolean withLine) {
        String headerText = getHeaderProvider().getHeader(withLine);
        getWriter().println(headerText);
    }
    default void printHeader() {
        printHeader(true);
    }


    default void printRow(T t) {
        String rowText = getRowConverter().getAsRow(t);
        getWriter().println(rowText);
    }

    default void print(Stream<T> elements, boolean withLine) {

        printHeader(withLine);

        elements
                .map(getRowConverter()::getAsRow)
                .forEach(getWriter()::println);

        getWriter().println();
    }

    default void print(Stream<T> elements) {
        print(elements, true);
    }

}
