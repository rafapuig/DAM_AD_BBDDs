package dam.ad.printers;

import dam.ad.converters.DefaultDTORowConverter;
import dam.ad.converters.RowConverter;

import java.io.PrintWriter;

public class DefaultDTOPrinter<T> implements EntityPrinter<T> {

    DefaultDTOHeaderProvider<T> headerProvider;
    DefaultDTORowConverter<T> rowConverter;

    public DefaultDTOPrinter(Class<T> type, int... columnLengths) {
        headerProvider = new DefaultDTOHeaderProvider<>(type, columnLengths);
        rowConverter = new DefaultDTORowConverter<>(type, columnLengths);
    }

    @Override
    public EntityHeaderProvider getHeaderProvider() {
        return headerProvider;
    }

    @Override
    public RowConverter<T> getRowConverter() {
        return rowConverter;
    }

    PrintWriter writer = new PrintWriter(System.out, true);
    @Override
    public PrintWriter getWriter() {
        return writer;
    }
}
