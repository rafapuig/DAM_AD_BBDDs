package dam.ad.futbol.file;

import dam.ad.file.DTOReader;
import dam.ad.futbol.model.Jugador;

import java.time.LocalDate;

public class JugadorDTOReader extends DTOReader<Jugador> {
    @Override
    protected Jugador read(String line) {
        String[] parts = line.split(",");
        return new Jugador(
                0,
                parts[0],
                parts[1],
                LocalDate.parse(parts[2]),
                Double.parseDouble(parts[3]),
                Integer.parseInt(parts[4]),
                Integer.parseInt(parts[5]),
                Integer.parseInt(parts[6]),
                Boolean.parseBoolean(parts[7])
        );
    }
}
