package dam.ad.printers;

import dam.ad.converters.RowConverter;
import dam.ad.headers.HeaderProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.stream.Stream;

public interface StreamPrinter<T> {

    HeaderProvider getHeaderProvider();

    RowConverter<T> getRowConverter();

    Writer getWriter();

    default String getHeader(boolean withLine) {
        return getHeaderProvider().getHeader(withLine);
    }

    default void printHeader(boolean withLine) {
        String headerText = getHeader(withLine);
        println(headerText);
        //getWriter().println(headerText);
    }

    default void printHeader() {
        printHeader(true);
    }

    default String getAsRow(T t) {
        return getRowConverter().getAsRow(t);
    }

    default void printRow(T t) {
        String rowText = getAsRow(t);
        println(rowText);
    }

    default void println(Object text) {
        try {
            if (text != null) getWriter().write(text.toString());
            getWriter().write(System.lineSeparator());
            getWriter().flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void print(Stream<T> elements, boolean withLine) {

        printHeader(withLine);

        elements
                .map(getRowConverter()::getAsRow)
                //.peek(System.out::println)
                .forEach(this::println);
        //.forEach(getWriter()::println);

        println(null);
        //getWriter().println();
    }

    default void print(Stream<T> elements) {
        print(elements, true);
    }

}
