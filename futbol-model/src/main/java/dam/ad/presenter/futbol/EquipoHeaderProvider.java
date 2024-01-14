package dam.ad.presenter.futbol;

import dam.ad.printers.EntityHeaderProvider;
import static dam.ad.presenter.futbol.EquipoRowConverter.*;

public class EquipoHeaderProvider implements EntityHeaderProvider {
    private static final String HEADER_FORMAT =
            "%-" + COLUMN_ID_LENGTH + "s " +
            "%-" + COLUMN_NOMBRE_LENGTH + "s " +
            "%-" + COLUMN_PAIS_LENGTH +"s ";
    static final String[] FIELDS =
            {"ID", "NOMBRE", "PAIS"};

    @Override
    public String getHeader() {
        String format = HEADER_FORMAT;
        String header = String.format(format, (Object[]) FIELDS);

        return header;
    }
}
