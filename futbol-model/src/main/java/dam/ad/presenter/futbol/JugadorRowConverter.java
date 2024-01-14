package dam.ad.presenter.futbol;

import dam.ad.converters.RowConverter;
import dam.ad.model.futbol.Jugador;

public class JugadorRowConverter implements RowConverter<Jugador> {
    public static final int COLUMN_ID_LENGTH = 2;
    public static final int COLUMN_NOMBRE_LENGTH = 35;
    public static final int COLUMN_PAIS_LENGTH = 4;
    public static final int COLUMN_NACIMIENTO_LENGTH = 10;
    public static final int COLUMN_ESTATURA_LENGTH = 8;
    public static final int COLUMN_PESO_LENGTH = 4;
    public static final int COLUMN_DORSAL_LENGTH = 6;
    public static final int COLUMN_EQUIPO_LENGTH = 20;
    public static final int COLUMN_CAPITAN_LENGTH = 7;

    private static final String ROW_FORMAT =
            "%" + COLUMN_ID_LENGTH + "s " +
            "%-" + COLUMN_NOMBRE_LENGTH + "s " +
            "%-" + COLUMN_PAIS_LENGTH +"s " +
            "%-" + COLUMN_NACIMIENTO_LENGTH +"s " +
            "%" + COLUMN_ESTATURA_LENGTH +"s " +
            "%" + COLUMN_PESO_LENGTH +"s " +
            "%" + COLUMN_DORSAL_LENGTH +"s " +
            "%-" + COLUMN_EQUIPO_LENGTH +"s " +
            "%" + COLUMN_CAPITAN_LENGTH +"s ";

    @Override
    public String getAsRow(Jugador jugador) {
        return String.format(ROW_FORMAT,
                jugador.getJugadorId(),
                jugador.getNombre(),
                jugador.getPais(),
                jugador.getNacimiento(),
                jugador.getEstatura(),
                jugador.getPeso(),
                jugador.getDorsal(),
                jugador.getEquipo() != null ? jugador.getEquipo().getNombre() : jugador.getEquipoId(),
                jugador.isCapitan() ? "SI" : "NO"
        );

    }
}
