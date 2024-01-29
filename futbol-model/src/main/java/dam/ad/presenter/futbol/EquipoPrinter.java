package dam.ad.presenter.futbol;

import dam.ad.converters.RowConverter;
import dam.ad.model.futbol.Equipo;
import dam.ad.headers.HeaderProvider;
import dam.ad.printers.StreamPrinter;

import java.io.PrintWriter;

public class EquipoPrinter implements StreamPrinter<Equipo> {
    EquipoHeaderProvider headerProvider = new EquipoHeaderProvider();
    RowConverter<Equipo> rowConverter = new EquipoRowConverter();

    PrintWriter writer;

    public EquipoPrinter(PrintWriter writer) {
        this.writer = writer;
    }
    @Override
    public HeaderProvider getHeaderProvider() {
        return headerProvider;
    }

    @Override
    public RowConverter<Equipo> getRowConverter() {
        return rowConverter;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public static final EquipoPrinter TO_CONSOLE =
            new EquipoPrinter(new PrintWriter(System.out, true));

}

