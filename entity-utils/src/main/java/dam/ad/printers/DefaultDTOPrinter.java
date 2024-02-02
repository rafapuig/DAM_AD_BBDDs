package dam.ad.printers;

import dam.ad.converters.DefaultDTORowConverter;
import dam.ad.converters.RowConverter;
import dam.ad.headers.DefaultDTOHeaderProvider;
import dam.ad.headers.HeaderProvider;

import java.io.PrintWriter;
import java.io.Writer;

public class DefaultDTOPrinter<T> implements StreamPrinter<T> {

    HeaderProvider headerProvider;
    RowConverter<T> rowConverter;

    //Writer writer = new PrintWriter(System.out, true);
    /*public void setWriter(Writer writer) {
        this.writer = writer;
    }*/

    public DefaultDTOPrinter(Class<T> type) {
        this(
                new DefaultDTOHeaderProvider<>(type),
                new DefaultDTORowConverter<>(type)
        );
    }

    public DefaultDTOPrinter(Class<T> type, int... columnLengths) {
        this(
                new DefaultDTOHeaderProvider<>(type, columnLengths),
                new DefaultDTORowConverter<>(type, columnLengths)
        );
    }

    protected DefaultDTOPrinter(HeaderProvider headerProvider, RowConverter<T> rowConverter) {
        this.headerProvider = headerProvider;
        this.rowConverter = rowConverter;
    }

    @Override
    public HeaderProvider getHeaderProvider() {
        return headerProvider;
    }

    @Override
    public RowConverter<T> getRowConverter() {
        return rowConverter;
    }



    @Override
    public Writer getWriter() {
        //return writer;
        //Estamos obteniendo el writer desde fuera (vigilar este acoplamiento)
        return Printers.writer;
    }

}
