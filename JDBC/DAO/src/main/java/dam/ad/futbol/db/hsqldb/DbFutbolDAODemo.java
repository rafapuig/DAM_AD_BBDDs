package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DataSourceFactory;
import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.file.DTOReader;
import dam.ad.futbol.file.EquipoDTOReader;
import dam.ad.futbol.file.JugadorDTOReader;
import dam.ad.futbol.model.Equipo;
import dam.ad.futbol.model.Jugador;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

        DataSource dataSource = DataSourceFactory.getInstance().getDataSource("jdbc/futbol");

        DatabaseSchema schema = new FutbolSchema();

        try {
            createDatabaseSchema(dataSource, schema);

            DAO<Equipo> equipoDAO = FutbolDAOManager.<Equipo>getDAO(Equipo.class, dataSource); // new DbEquipoDAO(dataSource);
            DAO<Jugador> jugadorDAO = FutbolDAOManager.<Jugador>getDAO(Jugador.class, dataSource); // new DbJugadorDAO(dataSource);

            loadEquipos(equipoDAO);
            loadJugadores(jugadorDAO);

            System.out.println("Numero de equipos: " + equipoDAO.getCount());

            equipoDAO.getAll().forEach(System.out::println);

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

        } finally {
            cleanUp(dataSource, schema);
            shutdown(dataSource); //Para HSQLDB
        }
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

    private static Jugador getJugador(DAO<Jugador> jugadorDAO, DAO<Equipo> equipoDAO) {
        Jugador luka = jugadorDAO.getAll()
                .filter(jugador -> jugador.getNombre().startsWith("Luka"))
                .findFirst()
                .orElseThrow();

        luka.setEquipo(equipoDAO.getById(luka.getEquipoId()).orElseThrow());

        return luka;
    }

    private static void loadJugadores(DAO<Jugador> jugadorDAO) {
        DTOReader<Jugador> jugadorDTOReader = new JugadorDTOReader();
        Stream<Jugador> jugadores = jugadorDTOReader
                .readFromResource("/futbol/jugadores.csv");
        jugadores.forEach(jugadorDAO::add);
        jugadores.close();

        //jugadorDAO.getAll().forEach(System.out::println);
    }

    private static void loadEquipos(DAO<Equipo> equipoDAO) {
        DTOReader<Equipo> equipoDTOReader = new EquipoDTOReader();

        Stream<Equipo> equipos = equipoDTOReader
                .readFromResource("/futbol/equipos.csv");

        equipos.forEach(equipoDAO::add);
        equipos.close();

        //equipoDAO.getAll().forEach(System.out::println);
    }




    static void createDatabaseSchema(DataSource dataSource, DatabaseSchema schema) {
        execute(dataSource, schema.getCreateSchema());
    }

    static void cleanUp(DataSource dataSource, DatabaseSchema schema) {
        execute(dataSource, schema.getDropSchema());
    }

    static void execute(DataSource dataSource, String SQLStatement) {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            statement.execute(SQLStatement);
            statement.close();
            connection.commit();

        } catch (SQLException e) {
            try {
                System.out.println("Desahaciendo la transaccion:\n" + e.getMessage());
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void shutdown(DataSource dataSource) throws SQLException {

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    static void bindJugadoresEquipos(DAO<Equipo> equipoDAO, DAO<Jugador> jugadorDAO) {
        List<Equipo> equipos = equipoDAO.getAll().toList();
        List<Jugador> jugadores = jugadorDAO.getAll().toList();


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


        // Establecer la coleccion de jugadores de cada equipo: equipo.setJugadores()

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

}
