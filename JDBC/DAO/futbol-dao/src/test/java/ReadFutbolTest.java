import dam.ad.file.DTOReader;
import dam.ad.futbol.file.EquipoDTOReader;
import dam.ad.futbol.file.JugadorDTOReader;

import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class ReadFutbolTest {

    @Test
    void testReadEquipos() {
        DTOReader<Equipo> reader = new EquipoDTOReader();

        Stream<Equipo> stream = reader.read();

        stream.forEach(System.out::println);
        stream.close();
    }
    @Test
    void testReadJugadores() {
        DTOReader<Jugador> jugadorDTOReader = new JugadorDTOReader();

        Stream<Jugador> stream = jugadorDTOReader.read();

        stream.forEach(System.out::println);
        stream.close();
    }
}
