package dam.ad.presenter.futbol;

import dam.ad.headers.HeaderProvider;

import static dam.ad.presenter.futbol.JugadorRowConverter.*;

public class JugadorHeaderProvider implements HeaderProvider {
    private static final String HEADER_FORMAT =
            "%" + COLUMN_ID_LENGTH + "s " +
            "%-" + COLUMN_NOMBRE_LENGTH + "s " +
            "%-" + COLUMN_PAIS_LENGTH +"s " +
            "%-" + COLUMN_NACIMIENTO_LENGTH +"s " +
            "%-" + COLUMN_ESTATURA_LENGTH +"s " +
            "%-" + COLUMN_PESO_LENGTH +"s " +
            "%-" + COLUMN_DORSAL_LENGTH +"s " +
            "%-" + COLUMN_EQUIPO_LENGTH +"s " +
            "%" + COLUMN_CAPITAN_LENGTH +"s ";

    static final String[] FIELDS =
            {"ID", "NOMBRE", "PAIS","NACIMIENTO","ESTATURA","PESO","DORSAL","EQUIPO","CAPITAN"};
    @Override
    public String getHeader() {
        String format = HEADER_FORMAT;
        String header = String.format(format, (Object[]) FIELDS);

        return header;
    }
}
