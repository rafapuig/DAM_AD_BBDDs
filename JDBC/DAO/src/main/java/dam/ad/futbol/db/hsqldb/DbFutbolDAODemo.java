package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.DAOFactory;
import dam.ad.dao.jdbc.DataSourceFactory;
import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.file.DTOReader;
import dam.ad.futbol.file.EquipoDTOReader;
import dam.ad.futbol.file.FileEquipoDAO;
import dam.ad.futbol.file.JugadorDTOReader;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DbFutbolDAODemo {

    public static void main(String[] args) throws Exception {

        DataSource dataSource = DataSourceFactory.getInstance()
                .getDataSource("jdbc/futbol");

        DatabaseSchema schema = new FutbolSchema();

        DAOFactory daoFactory = new DbFutbolDAOFactory(dataSource);

        FutbolDAOManager futbolDAOManager = new FutbolDAOManager(daoFactory);

        try {
            cleanUp(dataSource, schema);

            createDatabaseSchema(dataSource, schema);

            DAO<Equipo> equipoDAO = futbolDAOManager.getEquipoDAO();
            DAO<Jugador> jugadorDAO = futbolDAOManager.getJugadorDAO();

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

            //printBoundEquiposJugadores(entityManager.getEquipos(), entityManager.getJugadores());
            printBoundEquiposJugadores(equipos, jugadores);


            testModificarEquipo(entityManager, equipoDAO);


            //equipoDAO.getAll().forEach(System.out::println);

            //performJugadoresEquipos(equipoDAO, jugadorDAO);

            //jugadorDAO.getAll().forEach(System.out::println);

            //jugadorDAO.getAll().findFirst().get().getEquipoId();

            /*
            Jugador luka = getJugador(jugadorDAO, equipoDAO);
            luka.setEquipo(equipoDAO.getById(luka.getEquipoId()).orElseThrow());
            */

            /* List<Jugador> jugadores = jugadorDAO.getAll()
                    .peek(jugador -> jugador.setEquipo(
                            equipoDAO.getById(jugador.getEquipoId())
                                    .orElse(null)))
                    .toList();

            jugadores.forEach(
                    j -> System.out.println(j.getNombre() + " " + j.getEquipo().getNombre()));



            List<Equipo> equipos = equipoDAO.getAll().toList();

            Map<Equipo, List<Jugador>> map = jugadores.stream()
                    .collect(
                            Collectors.groupingBy(Jugador::getEquipo));

            map.forEach(Equipo::setJugadores);

            // equipoList = map.keySet().stream().toList();

            equipos
                .forEach(e -> e.setJugadores(jugadores.stream()
                        .filter(jugador -> jugador.getEquipoId() == e.getEquipoId())
                        .toList()));

            //map.keySet().forEach(e -> System.out.println(e + ":" + e.getJugadores()));

            equipos.forEach(e -> System.out.println(e + ":" + e.getJugadores()));

            //performOperationsDemo(equipoDAO);

             */

            //testBindEquiposJugadores(futbolDAOManager);

            testEquipoToJugadoresBinders(futbolDAOManager);

        } finally {
            //cleanUp(dataSource, schema);
            try {
                shutdown(dataSource); //Para HSQLDB
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    static void createDatabaseSchema(DataSource dataSource, DatabaseSchema schema) {
        System.out.println("Creando el esquema de la base de datos...");
        execute(dataSource, schema.getCreateSchema());
    }

    static void cleanUp(DataSource dataSource, DatabaseSchema schema) {
        System.out.println("Eliminando la base de datos...");
        execute(dataSource, schema.getDropSchema());
    }

    static void shutdown(DataSource dataSource) {
        System.out.println("Apagando la base de datos...");

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    static void execute(DataSource dataSource, String SQLStatement) {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            for (String sqlStatement : SQLStatement.split(";")) {

                try (Statement statement = connection.createStatement()) {
                    statement.execute(sqlStatement);
                }
            }
            connection.commit();

        } catch (SQLException e) {
            try {
                System.out.println("Desahaciendo la transacción:\n" + e.getMessage());
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static void loadEquipos_(DAO<Equipo> equipoDAO) {
        DTOReader<Equipo> equipoDTOReader = new EquipoDTOReader();

        Stream<Equipo> equipos = equipoDTOReader.read();
        equipos.forEach(equipoDAO::add);
        equipos.close();

        new FileEquipoDAO().getAll()
                .forEach(equipoDAO::add);

        //equipoDAO.getAll().forEach(System.out::println);
    }

    private static void loadEquipos(DAO<Equipo> equipoDAO) {
        DAO<Equipo> fileEquipoDAO = new FileEquipoDAO();

        Stream<Equipo> equipos = fileEquipoDAO.getAll();
        equipos.forEach(equipoDAO::add);
        equipos.close();

        //equipoDAO.getAll().forEach(System.out::println);
    }

    private static void loadJugadores(DAO<Jugador> jugadorDAO) {
        DTOReader<Jugador> jugadorDTOReader = new JugadorDTOReader();
        Stream<Jugador> jugadores = jugadorDTOReader.read();
        jugadores.forEach(jugadorDAO::add);
        jugadores.close();

        //jugadorDAO.getAll().forEach(System.out::println);
    }


    private static void testEquipoToJugadoresBinders(FutbolDAOManager futbolDAOManager) {
        EquiposToJugadoresBinder[] binders = new EquiposToJugadoresBinder[]{
                EquiposToJugadoresBinder::bindEquiposToJugadores1,
                EquiposToJugadoresBinder::bindEquiposToJugadores2,
                EquiposToJugadoresBinder::bindEquiposToJugadores3,
                EquiposToJugadoresBinder::bindEquiposToJugadores4,
                EquiposToJugadoresBinder::bindEquiposToJugadores5,
                EquiposToJugadoresBinder::bindEquiposToJugadores6
        };

        Arrays.asList(binders).forEach(
                binder -> testBindEquiposJugadores(futbolDAOManager, binder));

        //testBindEquiposJugadores(futbolDAOManager, DbFutbolDAODemo::bindEquiposToJugadores1);
        //testBindEquiposJugadores(futbolDAOManager, DbFutbolDAODemo::bindEquiposToJugadores2);
    }


    static void testBindEquiposJugadores(FutbolDAOManager manager) {
        List<Equipo> equipos = manager.getEquipoDAO().getAll().toList();
        List<Jugador> jugadores = manager.getJugadorDAO().getAll().toList();

        FutbolEntityManager.bindEquiposJugadores(equipos, jugadores);
        printBoundEquiposJugadores(equipos, jugadores);
    }

    static void testBindEquiposJugadores(FutbolDAOManager manager, EquiposToJugadoresBinder binder) {

        System.out.println("Probando el binder " + binder + "...");

        List<Equipo> equipos = manager.getEquipoDAO().getAll().toList();
        List<Jugador> jugadores = manager.getJugadorDAO().getAll().toList();

        FutbolEntityManager.bindEquiposJugadores(equipos, jugadores, binder);
        printBoundEquiposJugadores(equipos, jugadores);
    }


    private static void performOperationsDemo(DAO<Equipo> equipoDAO) {

        Optional<Equipo> equipo = equipoDAO.getById(1);

        equipo.ifPresent(System.out::println);

        Equipo nuevo = new Equipo(0, "Valencia C.F", "ESP");
        equipoDAO.add(nuevo);

        equipoDAO.getAll().forEach(System.out::println);

        nuevo.setNombre("Amunt Valencia");
        nuevo.setPais("KOR");

        equipoDAO.update(nuevo);

        equipoDAO.getAll().forEach(System.out::println);

        //equipoDAO.delete(nuevo);

        //equipoDAO.getById(6).ifPresent(equipoDAO::delete);

        IntStream.range(6, 100)
                .mapToObj(id -> new Equipo(id, "", ""))
                .forEach(equipoDAO::delete);

        equipoDAO.getAll().forEach(System.out::println);
    }

    private static Jugador testGetJugador(FutbolDAOManager futbolDAOManager) {

        Jugador luka = futbolDAOManager.getJugadorDAO().getAll()
                .filter(jugador -> jugador.getNombre().startsWith("Luka"))
                .findFirst()
                .orElseThrow();

        luka.setEquipo(futbolDAOManager.getEquipoDAO()
                .getById(luka.getEquipoId()).orElseThrow());

        return luka;
    }


    static void performJugadoresEquipos(DAO<Equipo> equipoDAO, DAO<Jugador> jugadorDAO) {

        List<Equipo> equipos = equipoDAO.getAll().toList();
        List<Jugador> jugadores = jugadorDAO.getAll().toList();

        //También se puede cargar la lista de jugadores con el equipo
        /*jugadores = jugadorDAO.getAll()
                .peek(jugador -> jugador.setEquipo(
                        equipoDAO.getById(jugador.getEquipoId())
                                .orElse(null)))
                .toList();*/

        //Crear mapa de equipos a partir de la lista
        Map<Integer, Equipo> equiposMap = equipos.stream()
                .collect(Collectors
                        .toMap(
                                Equipo::getEquipoId, // key Integer ID del equipo
                                Function.identity())); // value referencia al objeto Equipo

        // Asignar a cada jugador la referencia al equipo en el que esta
        jugadores
                .forEach(jugador -> jugador.setEquipo(
                        equiposMap.get(jugador.getEquipoId()))); //O(1) con el mapa de equipos


        // Otra forma sin usar el equiposMap
        /*jugadores.forEach(jugador -> jugador.setEquipo(equipos.stream()
                .filter(e -> jugador.getEquipoId() == e.getEquipoId())
                .findAny()
                .orElse(null)));*/


        // Establecer la colección de jugadores de cada equipo: equipo.setJugadores()

        //Forma 1
        /* equipos.forEach(equipo -> equipo.setJugadores(jugadores.stream()
                .filter(jugador -> jugador.getEquipo().equals(equipo))
                .toList())); */

        // Forma 2 de establecer los jugadores de un equipo
        /*Map<Equipo, List<Jugador>> jugadoresByEquipoMap = jugadores.stream()
                .collect(Collectors.groupingBy(Jugador::getEquipo));

        //jugadoresByEquipoMap.
        //        forEach((equipo, jugadoresList) -> equipo.setJugadores(jugadoresList));

        jugadoresByEquipoMap.
                forEach(Equipo::setJugadores);

        equipos.forEach(equipo ->
        {
            /*jugadoresByEquipoMap.computeIfPresent(
                    equipo,
                    (key, jugadoresList) -> {
                        key.setJugadores(jugadoresList);
                        return jugadoresList;
                    });

            jugadoresByEquipoMap.computeIfAbsent(
                    equipo,
                    key -> {
                        List<Jugador> empty = List.of();
                        key.setJugadores(empty);
                        return empty;
                    });
        });*/


        //Forma 3
        Map<Equipo, List<Jugador>> jugadoresByEquipoMap = jugadores.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Jugador::getEquipo),
                        map -> {
                            map.forEach(Equipo::setJugadores);
                            return map;
                        }));

        equipos.stream()
                .filter(equipo -> equipo.getJugadores() == null)
                .forEach(equipo -> equipo.setJugadores(List.of()));


        //Forma 4: menos eficiente
        /*equipos
                .forEach(equipo -> equipo.setJugadores(jugadores.stream()
                        .filter(jugador -> jugador.getEquipoId() == equipo.getEquipoId())
                        .toList()));*/

        //Forma 5: derivada de la anterior
        /*equipos
                .forEach(equipo -> equipo.setJugadores(jugadores.stream()
                        .filter(jugador -> jugador.getEquipo().equals(equipo))
                        .toList()));*/

        // Imprimir resultados
        equipos.forEach(
                equipo -> {
                    System.out.println(equipo.getNombre());
                    equipo.getJugadores().forEach(System.out::println);
                });

        //SALEN NULOS por no usar streams y crear una lista con cero elementos en lugar de null
        jugadores.forEach(
                jugador ->
                        System.out.println(jugador.getNombre()
                                           + " -> "
                                           + jugador.getEquipo().getNombre()));
    }


    public static void printBoundEquiposJugadores(List<Equipo> equipos, List<Jugador> jugadores) {
        System.out.println("Equipos enlazados con jugadores:");
        // Imprimir resultados
        equipos.forEach(
                equipo -> {
                    System.out.println(equipo.getNombre());
                    equipo.getJugadores().forEach(System.out::println);
                });
        System.out.println();

        System.out.println("Jugadores y equipo al que pertenecen:");
        //SALEN NULOS por no usar streams y crear una lista con cero elementos en lugar de null
        jugadores.forEach(
                jugador ->
                        System.out.println(jugador.getNombre()
                                           + " -> "
                                           + jugador.getEquipo().getNombre()));
        System.out.println();
    }

    private static void testModificarEquipo(FutbolEntityManager entityManager, DAO<Equipo> equipoDAO) {
        Equipo equipo = entityManager.getEquipos().stream()
                .filter(e -> e.getEquipoId() == 1)
                .findAny().orElse(null);

        assert equipo != null;
        equipo.setPais("FRA");

        equipoDAO.update(equipo);

        equipoDAO.getAll().filter(e -> e.equals(equipo)).forEach(System.out::println);
    }

}
