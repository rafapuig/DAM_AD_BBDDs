package dam.ad.presenter.futbol;

import dam.ad.converters.RowConverter;
import dam.ad.model.futbol.Jugador;
import dam.ad.printers.EntityHeaderProvider;
import dam.ad.printers.EntityPrinter;

import java.io.PrintWriter;

public class JugadorPrinter implements EntityPrinter<Jugador> {

    PrintWriter writer;
    EntityHeaderProvider headerProvider;

    RowConverter<Jugador> rowConverter;

    public JugadorPrinter(PrintWriter writer) {
        this.writer = writer;
        this.headerProvider = new JugadorHeaderProvider();
        this.rowConverter = new JugadorRowConverter();
    }

    @Override
    public EntityHeaderProvider getHeaderProvider() {
        return headerProvider;
    }

    @Override
    public RowConverter<Jugador> getRowConverter() {
        return rowConverter;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public static final JugadorPrinter TO_CONSOLE =
            new JugadorPrinter(new PrintWriter(System.out, true));

}
