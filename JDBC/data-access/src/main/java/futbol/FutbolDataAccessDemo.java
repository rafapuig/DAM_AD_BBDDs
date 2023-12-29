package futbol;

import dam.ad.jdbc.dataaccess.DTOSupplier;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;

import java.util.Optional;
import java.util.stream.Stream;

public class FutbolDataAccessDemo {

    // URL de conexión a la base de datos futbol HSQLDB
    static final String HSQLDB_FUTBOL_URL =
            "jdbc:hsqldb:C:/BBDDs/hsqldb/JDBCfutbol;ifexists=true;user=SA;password=";

    public static void main(String[] args) {

        //Usamos un try-with-resources para que se autocierre la conexion con la BBDD
        try (FutbolDataAccess fda = new FutbolDataAccess(HSQLDB_FUTBOL_URL)) {

            //testGetAllEquipos(fda);

            //testFindEquipoByName(fda);

            //testDeleteEquipo(fda);

            //testGetJugadoresByEquipo(fda);

            //testGetAllJugadores(fda);

            testGetAllJugadoresListMap(fda);
        }
    }

    private static void testGetAllEquipos(FutbolDataAccess fda) {
        // Obtener un Stream<Equipo> con el que procesar los datos con origen de la BD
        try (Stream<Equipo> equipos = fda.getAllEquipos()) {
            equipos.forEach(System.out::println);
        }

        // Alternativamente usando el método que devuelve una lista
        fda.getAllEquiposAsList()
                .forEach(System.out::println);

        // O en un Map
        fda.getAllEquiposAsMap().forEach(
                (key, value) -> {
                    System.out.print(key + "=");
                    System.out.println(value);
                }
        );
    }

    static void testFindEquipoByName(FutbolDataAccess fda) {
        System.out.println("Busca equipo con nombre Real...");
        Stream<Equipo> equipoStream = fda.findEquipoByName("Real");
        equipoStream.forEach(System.out::println);
        System.out.println("fin test buscar equipo");
    }

    static void testGetJugadoresByEquipo(FutbolDataAccess fda) {
        Optional<Equipo> equipo = fda.findEquipoById(1);

        equipo.ifPresentOrElse(
                (e) -> {
                    Stream<Jugador> jugadores = fda.getJugadoresByEquipo(equipo.get());
                    jugadores.forEach(System.out::println);
                },
                () -> System.out.println("No se encontró el equipo")
        );
    }

    static void testAddEquipo(FutbolDataAccess fda) {
        Equipo equipo = new Equipo(0, "Valencia C.F.", "ESP");
        fda.addEquipo(equipo);
        System.out.println(equipo);
        fda.getAllEquiposAsList().forEach(System.out::println);
    }

    static void testDeleteEquipo(FutbolDataAccess fda) {
        try (Stream<Equipo> allEquipos = fda.getAllEquipos()) {

            Equipo equipo = allEquipos
                    .filter(e -> e.getNombre().startsWith("Valencia"))
                    .findAny().orElse(null);

            allEquipos.close();

            if (equipo != null) {
                fda.deleteEquipo(equipo);
                fda.getAllEquiposAsList().forEach(System.out::println);
            }
        }
    }

    static void testGetAllJugadores(FutbolDataAccess fda) {
        try (Stream<Jugador> jugadores = fda.getAllJugadores(true)) {
            jugadores.forEach(System.out::println);
        } // Auto llamada a jugadores.close();
    }


    static void testGetAllJugadoresListMap(FutbolDataAccess fda) {

        DTOSupplier<Jugador> jugadorDTOSupplier;

        jugadorDTOSupplier = () -> fda.getAllJugadores(true);

        var map = jugadorDTOSupplier.asMap(Jugador::getJugadorId);

        map.forEach((key, value) -> {
            System.out.println(key + "=" + value);
        });

        try (Stream<Jugador> stream = jugadorDTOSupplier.getStream()) {

            stream.forEach(System.out::println);
        }
    }


}
