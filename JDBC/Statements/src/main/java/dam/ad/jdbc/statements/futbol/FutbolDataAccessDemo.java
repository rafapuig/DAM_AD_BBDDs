package dam.ad.jdbc.statements.futbol;

import dam.ad.futbol.model.Equipo;

import java.util.stream.Stream;

public class FutbolDataAccessDemo {

    // URL de conexión a la base de datos futbol HSQLDB
    static final String HSQLDB_FUTBOL_URL =
            "jdbc:hsqldb:C:/BBDDs/hsqldb/futbol;ifexists=true;user=SA;password=";

    public static void main(String[] args) {

        //Usamos un try-with-resources para que se autocierre la conexion con la BBDD
        try (FutbolDataAccess fda = new FutbolDataAccess(HSQLDB_FUTBOL_URL)) {

            // Obtener un Stream<Equipo> con el que procesar los datos con origen de la BD
            try(Stream<Equipo> equipos = fda.getAllEquipos()) {
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

            Equipo newEquipo = new Equipo(0, "Valencia C.F.", "ESP");
            addEquipo(fda, newEquipo);

            deleteEquipo(fda, newEquipo);
        }
    }

    private static void deleteEquipo(FutbolDataAccess fda, Equipo newEquipo) {
        fda.deleteEquipo(newEquipo);
        fda.getAllEquiposAsList().forEach(System.out::println);
    }

    private static void addEquipo(FutbolDataAccess fda, Equipo equipo) {
        fda.addEquipo(equipo);
        System.out.println(equipo);
        fda.getAllEquiposAsList().forEach(System.out::println);
    }


}
