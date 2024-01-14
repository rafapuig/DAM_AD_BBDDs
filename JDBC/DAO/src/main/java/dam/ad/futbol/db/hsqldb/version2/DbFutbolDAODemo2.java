package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.converters.SimpleRowConverter;
import dam.ad.converters.WildcardRowConverter;
import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.GenericDTOMapper;
import dam.ad.futbol.db.hsqldb.DbFutbolDAODemo;
import dam.ad.futbol.db.hsqldb.FutbolEntityManager;
import dam.ad.futbol.file.FileEquipoDAO;
import dam.ad.futbol.file.FileJugadorDAO;
import dam.ad.model.futbol.Equipo;
import dam.ad.presenter.futbol.EquipoPrinter;
import dam.ad.model.futbol.Jugador;
import dam.ad.presenter.futbol.JugadorPrinter;
import dam.ad.printers.GenericEntityPrinter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DbFutbolDAODemo2 {

    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/futbol20;shutdown=true";


    public static void main(String[] args) throws Exception {

        FutbolDAOManager manager = new FutbolDAOManager(URL);

        try {

            manager.dropSchema();
            manager.generateSchema();

            DAO<Equipo> equipoDAO = manager.createDAO(Equipo.class);
            DAO<Jugador> jugadorDAO = manager.createDAO(Jugador.class);

            loadEquipos(equipoDAO);
            loadJugadores(jugadorDAO);

            System.out.println("Numero de equipos: " + equipoDAO.getCount());
            System.out.println("Numero de jugadores: " + jugadorDAO.getCount());

            List<Equipo> equipos = equipoDAO.getAll().toList();
            List<Jugador> jugadores = jugadorDAO.getAll().toList();

            FutbolEntityManager entityManager = new FutbolEntityManager();
            entityManager.setEquipos(equipos);
            entityManager.setJugadores(jugadores);

            entityManager.bindEquiposJugadores();

            testAddEquipo(equipoDAO);

            EquipoPrinter.TO_CONSOLE.print(equipos.stream());

            JugadorPrinter.TO_CONSOLE.print(jugadores.stream());

            DbFutbolDAODemo.printBoundEquiposJugadores(equipos, jugadores);

            //testJugadoresFranceses(manager, entityManager);

            //testJugadoresEquipo(manager, entityManager);

            testGenericDTOMapper1(manager);

            testGenericDTOMapper2(manager);

            //testJugadoresNombreEdad(manager);

            //testSimpleRowConverter(manager);

            testWildcardRowConverter(manager);

        } finally {
            System.out.println("Cerrando la base de datos...");
            manager.shutdown();
            System.out.println("Fin de la demo");
        }

    }

    private static void testSimpleRowConverter(FutbolDAOManager manager) throws Exception {
        Stream<List<Object>> query = manager.query(
                "SELECT jugadorID, j.nombre, j.pais, nacimiento, estatura, peso, dorsal, e.nombre, capitan FROM Jugador j INNER JOIN equipo e ON e.equipoID = j.equipoID");
        SimpleRowConverter src = new SimpleRowConverter(2, -20, 4, 11, 8, 7, 4, -20, 8);
        query
                .map(src::getAsRow)
                .forEach(System.out::println);

        manager.close();
    }

    private static void testWildcardRowConverter(FutbolDAOManager manager) throws Exception {
        Stream<?> query = manager.query(
                "SELECT jugadorID, j.nombre, j.pais, nacimiento, estatura, peso, dorsal, e.nombre, capitan FROM Jugador j INNER JOIN equipo e ON e.equipoID = j.equipoID");
        WildcardRowConverter src = new WildcardRowConverter(2, -20, 4, 11, 8, 7, 4, -20, 8);
        query
                .map(src::getAsRow)
                .forEach(System.out::println);

        manager.close();
    }

    private static void testGenericDTOMapper1(FutbolDAOManager manager) {
        Stream<?> query = manager.query(
                """
                        SELECT nombre, (CURRENT_DATE - nacimiento) YEAR AS edad
                        FROM jugador""");

        query.forEach(System.out::println);
    }

    private static void testJugadoresNombreEdad(FutbolDAOManager manager) {
        Stream<?> query = manager.query(
                """
                        SELECT nombre, CONVERT( (CURRENT_DATE - nacimiento) YEAR, SQL_INTEGER) AS edad 
                        FROM jugador ORDER BY edad DESC""",
                JugadorNombreEdad.class);

        GenericEntityPrinter.print(query.toList(), 20, 4);
    }

    private static void testGenericDTOMapper2(FutbolDAOManager manager) {

        Stream<EquipoNumJugadores> query = manager.query("""
                        SELECT e.equipoId, e.nombre, e.pais, COUNT(*) as jugadores
                        FROM equipo e INNER JOIN jugador j ON e.equipoID = j.equipoID
                        GROUP BY e.equipoId
                        """,
                new GenericDTOMapper<>(EquipoNumJugadores.class));

        List<EquipoNumJugadores> equipoNumJugadoresList = query.toList();

        System.out.println(equipoNumJugadoresList);

        GenericEntityPrinter.print(equipoNumJugadoresList, 2, 30, 4, 9);
    }

    public record JugadorNombreEdad(String nombre, int edad) {
    }




    public record EquipoNumJugadores(int id, String nombre, String pais, long jugadores) {
    }



    private static void testJugadoresEquipo(FutbolDAOManager manager, FutbolEntityManager entityManager) {
        String nombreEquipo = "Real Madrid";

        Optional<Equipo> equipo = manager.querySingleResult(
                "SELECT * FROM equipo WHERE nombre LIKE ?",
                Equipo.class, "%" + nombreEquipo + "%");

        entityManager.setEquipos(equipo.stream().toList());

        List<Jugador> jugadoresRM = manager.query(
                "SELECT * FROM jugador WHERE equipoID = ? ORDER BY nacimiento",
                Jugador.class,
                equipo.orElseThrow().getEquipoId()).toList();

        entityManager.setJugadores(jugadoresRM);

        entityManager.bindEquiposJugadores();

        equipo.ifPresent(e -> System.out.println(e.getNombre()));

        JugadorPrinter.TO_CONSOLE.print(jugadoresRM.stream());
    }

    private static void testJugadoresFranceses(FutbolDAOManager manager, FutbolEntityManager entityManager) {
        Stream<Jugador> franceses =
                manager.query("SELECT * FROM jugador WHERE pais = ?",
                        Jugador.class, "FRA");

        List<Jugador> francesesList = franceses.toList();

        List<Equipo> equiposConJugadorFrances =
                manager.query("""
                                SELECT * FROM equipo
                                WHERE equipoID IN (
                                    SELECT equipoID FROM jugador WHERE pais=?)""",
                        Equipo.class, "FRA").toList();

        entityManager.setEquipos(equiposConJugadorFrances);
        entityManager.setJugadores(francesesList);

        entityManager.bindEquiposJugadores();

        System.out.println("Equipos con jugadores franceses:");
        EquipoPrinter.TO_CONSOLE.print(equiposConJugadorFrances.stream());

        System.out.println("Jugadores franceses:");
        JugadorPrinter.TO_CONSOLE.print(francesesList.stream());
    }

    private static void testAddEquipo(DAO<Equipo> equipoDAO) {
        Equipo newEquipo = new Equipo(0, "Osasuna", "ESP");
        equipoDAO.add(newEquipo);

        Optional<Equipo> equipo = equipoDAO.getById(newEquipo.getEquipoId());

        equipo.ifPresentOrElse(System.out::println,
                () -> System.out.println("El equipo no se encuentra."));
    }

    static void loadEquipos(DAO<Equipo> equipoDAO) {
        try (DAO<Equipo> fileEquipoDAO = new FileEquipoDAO()) {
            Stream<Equipo> equipos = fileEquipoDAO.getAll();
            equipos.forEach(equipoDAO::add);
        }
    }

    static void loadJugadores(DAO<Jugador> jugadorDAO) {
        try (DAO<Jugador> fileJugadorDAO = new FileJugadorDAO()) {
            Stream<Jugador> jugadores = fileJugadorDAO.getAll();
            jugadores.forEach(jugadorDAO::add);
        }
    }

}
