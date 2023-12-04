package dam.ad.futbol.file;

import dam.ad.futbol.model.Equipo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Stream;

public class ReadEquipos {
    public static void main(String[] args) {
        Stream<Equipo> equipoStream = readEquiposFromResource("/futbol/equipos.csv");
        equipoStream.forEach(System.out::println);
        equipoStream.close();
    }

    public static Stream<Equipo> readEquiposFromResource(String name) {
        try {
            InputStream is = Objects.requireNonNull(ReadEquipos.class.
                            getResource("/futbol/equipos.csv"))
                    .openStream();

            return readEquipos(is);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Stream<Equipo> readEquipos(InputStream input) {
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(isr);

        return br.lines()
                .map(ReadEquipos::readEquipo)
                .onClose(() -> {
                    try {
                        input.close();
                        System.out.println("Closed input stream");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    static Equipo readEquipo(String line) {
        String[] parts = line.split(",");
        return new Equipo(0, parts[0], parts[1]);
    }

}
