import dam.ad.file.DTOReader;
import dam.ad.futbol.file.JugadorDTOReader;
import dam.ad.futbol.model.Jugador;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class ReadJugadoresTest {

    @Test
    void testReadJugadores() {
        DTOReader<Jugador> jugadorDTOReader = new JugadorDTOReader();

        Stream<Jugador> stream = jugadorDTOReader.readFromResource("/futbol/jugadores.csv");

        stream.forEach(System.out::println);
        stream.close();



    }
}
