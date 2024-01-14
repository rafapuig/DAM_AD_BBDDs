package dam.ad.presenter.futbol;

import dam.ad.converters.RowConverter;
import dam.ad.model.futbol.Equipo;

public class EquipoRowConverter implements RowConverter<Equipo> {
    public static final int COLUMN_ID_LENGTH = 2;
    public static final int COLUMN_NOMBRE_LENGTH = 40;
    public static final int COLUMN_PAIS_LENGTH = 4;
    private static final String ROW_FORMAT =
            "%" + COLUMN_ID_LENGTH + "s " +
            "%-" + COLUMN_NOMBRE_LENGTH + "s " +
            "%-" + COLUMN_PAIS_LENGTH +"s ";

    @Override
    public String getAsRow(Equipo equipo) {
        return String.format(ROW_FORMAT,
                equipo.getEquipoId(),
                equipo.getNombre(),
                equipo.getPais());
    }
}
