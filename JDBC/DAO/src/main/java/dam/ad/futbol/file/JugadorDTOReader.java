package dam.ad.futbol.file;

import dam.ad.file.DTOReader;
import dam.ad.futbol.model.Jugador;

import java.net.URL;
import java.time.LocalDate;
import static dam.ad.futbol.file.JugadorDTOReader.Column.*;

public class JugadorDTOReader extends DTOReader<Jugador> {
    static String DEFAULT_RESOURCE_NAME = "/futbol/jugadores.csv";

    enum Column {
        NOMBRE,
        PAIS,
        NACIMIENTO,
        ESTATURA,
        PESO,
        DORSAL,
        EQUIPO_ID,
        CAPITAN
    }

    String resourceName;

    public JugadorDTOReader() {
        this(DEFAULT_RESOURCE_NAME);
    }

    public JugadorDTOReader(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    protected URL getURL() {
        return this.getClass().getResource(resourceName);
    }

    @Override
    protected Jugador read(String line) {

        String[] parts = line.split(",");

        return new Jugador(
                0,
                parts[NOMBRE.ordinal()],
                parts[PAIS.ordinal()],
                LocalDate.parse(parts[NACIMIENTO.ordinal()]),
                Double.parseDouble(parts[ESTATURA.ordinal()]),
                Integer.parseInt(parts[PESO.ordinal()]),
                Integer.parseInt(parts[DORSAL.ordinal()]),
                Integer.parseInt(parts[EQUIPO_ID.ordinal()]),
                Boolean.parseBoolean(parts[CAPITAN.ordinal()])
        );
    }
}
