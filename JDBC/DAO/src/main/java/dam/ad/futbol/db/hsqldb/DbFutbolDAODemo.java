package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.DAO;
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
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DbFutbolDAODemo {

    static {
        try {
            registerDataSource();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {

        //registerDataSource();
        //registerDerbyDataSource();
        registerHSQLDBDataSource();

        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("jdbc/futbol");

        DatabaseSchema schema = new FutbolSchema();
        createDatabaseSchema(dataSource, schema);

        DAO<Equipo> equipoDAO = new DbEquipoDAO(dataSource);
        DAO<Jugador> jugadorDAO = new DbJugadorDAO(dataSource);

        DTOReader<Equipo> equipoDTOReader = new EquipoDTOReader();


        Stream<Equipo> equipos = equipoDTOReader.readFromResource("/futbol/equipos.csv");

        equipos.forEach(equipoDAO::add);
        equipos.close();

        equipoDAO.getAll().forEach(System.out::println);

        DTOReader<Jugador> jugadorDTOReader = new JugadorDTOReader();
        Stream<Jugador> jugadores = jugadorDTOReader.readFromResource("/futbol/jugadores.csv");
        jugadores.forEach(jugadorDAO::add);
        jugadores.close();

        performJugadoresEquipos(equipoDAO, jugadorDAO);

        jugadorDAO.getAll().forEach(System.out::println);

        //jugadorDAO.getAll().findFirst().get().getEquipoId();

        Jugador luka = jugadorDAO.getById(1).orElseThrow();

        List<Jugador> jugadoresList = jugadorDAO.getAll()
                .peek(jugador -> jugador.setEquipo(
                        equipoDAO.getById(jugador.getEquipoId()).orElse(null)))
                .toList();

        jugadoresList.forEach(
                j -> System.out.println(j.getNombre() + " " + j.getEquipo().getNombre()));

        luka.setEquipo(equipoDAO.getById(luka.getEquipoId()).orElseThrow());

        List<Equipo> equipoList = equipoDAO.getAll().toList();

        var map = jugadoresList.stream()
                .collect(
                        Collectors.groupingBy(Jugador::getEquipo));

        map.forEach(Equipo::setJugadores);

        equipoList = map.keySet().stream().toList();

       /* equipoList
                .forEach(e -> e.setJugadores(jugadoresList.stream()
                        .filter(jugador -> jugador.getEquipoId() == e.getEquipoId())
                        .toList()));*/

        //map.keySet().forEach(e -> System.out.println(e + ":" + e.getJugadores()));

        equipoList.forEach(e -> System.out.println(e + ":" + e.getJugadores()));

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

        cleanUp(dataSource, schema);

        shutdown(dataSource); //Para HSQLDB
    }

    static DataSource createDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:hsqldb:C:/BBDDs/hsqldb/futbol"); //create=true"); // ;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");

        // No tiene Connection Pool !!!!
        return JDBCDataSourceFactory.createDataSource(properties);
    }

    static void registerDataSource() throws Exception {

        //Properties props = new Properties();
        //props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        //Context initialContext = new InitialContext(props);

        Context initialContext = new InitialContext();
        JDBCDataSource dataSource = new JDBCDataSource();
        //dataSource.setDatabaseName();

        dataSource.setURL("C:/BBDDs/hsqldb/futbol");
        dataSource.setUser("SA");
        dataSource.setPassword("");

        initialContext.bind("jdbc/hsqldb/futbol", dataSource);
    }

    static void registerHSQLDBDataSource() throws Exception {

        //Properties props = new Properties();
        //props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        //Context initialContext = new InitialContext(props);

        Context initialContext = new InitialContext();
        JDBCDataSource dataSource = new JDBCDataSource();
        //dataSource.setDatabaseName();

        dataSource.setURL("C:/BBDDs/hsqldb/JDBCfutbol");
        dataSource.setUser("SA");
        dataSource.setPassword("");

        initialContext.bind("jdbc/futbol", dataSource);
    }

    static void registerDerbyDataSource() throws Exception {

        Context initialContext = new InitialContext();
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("C:/BBDDs/derby/futbol");

        initialContext.bind("jdbc/futbol", dataSource);
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

    static void performJugadoresEquipos(DAO<Equipo> equipoDAO, DAO<Jugador> jugadorDAO) {
        List<Equipo> equipos = equipoDAO.getAll().toList();
        List<Jugador> jugadores = jugadorDAO.getAll().toList();

        var equiposMap = equipoDAO.getAll()
                .collect(Collectors.toMap(Equipo::getEquipoId, Function.identity()));

        jugadores.forEach(jugador -> jugador.setEquipo(equiposMap.get(jugador.getEquipoId())));

        /*jugadores.forEach(jugador -> jugador.setEquipo(equipos.stream()
                .filter(e -> jugador.getEquipoId() == e.getEquipoId())
                .findAny()
                .orElse(null)));*/

        equipos.forEach(equipo -> equipo.setJugadores(jugadores.stream()
                .filter(jugador -> jugador.getEquipo().equals(equipo))
                .toList()));

        equipos.forEach(
                equipo -> {
                    System.out.println(equipo.getNombre());
                    equipo.getJugadores().forEach(System.out::println);
                } );

        jugadores.forEach(
                jugador ->
                        System.out.println(jugador.getNombre() + " -> " + jugador.getEquipo().getNombre()));


        System.out.println(equipos.get(0).getJugadores().get(0).getEquipo().getJugadores().get(0).getEquipo().getNombre());
    }

}
