package dam.ad.jdbc.statements.futbol;

import dam.ad.futbol.model.Equipo;
import dam.ad.futbol.model.Jugador;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutbolDataAccess implements AutoCloseable {
    Connection connection;

    public FutbolDataAccess(String dbURL) {
        openConnection(dbURL);
    }

    void openConnection(String dbURL) {
        try {
            connection = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo la conexión a la BBDD", e);
        }
    }

    Connection getConnection() {
        return this.connection;
    }


    /**
     * Método para cerrar la conexion y apagar la base de datos
     */
    @Override
    public void close() {
        if (getConnection() != null) {
            try {
                Statement stmt = getConnection().createStatement();
                stmt.execute("SHUTDOWN");
                getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(
                        "ERROR cerrando la conexión con la base de datos",
                        e);
            }
        }
    }


    /**
     * Crea una entidad Equipo a partir de leer los campos de un resultSet
     * posicionado en la fila que contiene los datos del equipo que queremos leer
     */
    Equipo createEquipo(ResultSet rs) throws SQLException {
        return new Equipo(
                rs.getInt("equipoID"),
                rs.getString("nombre"),
                rs.getString("pais")
        );
    }

    /**
     * Cierra un ResultSet, se debe llamar cuando ya hemos acabado de leer los datos que contenía
     */
    private void close(ResultSet resultSet) {
        try {
            System.out.println("Cerrando resultSet");
            resultSet.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    Stream<Equipo> getAllEquipos() {
        try (Statement stmt = getConnection().createStatement()) {
            final String SQL = "SELECT * FROM equipo";

            ResultSet rs = stmt.executeQuery(SQL);

            // Vamos a generar un Stream<Equipo> mediante un Stream Builder
            Stream.Builder<Equipo> builder = Stream.builder();
            while (rs.next()) { // mientras leamos un nuevo registro en el ResultSet ...
                builder.add(createEquipo(rs)); // Añadimos un Equipo al builder
            }

            //Una vez hemos añadido todos los elementos al Stream Builder, generamos el Stream
            Stream<Equipo> stream = builder.build();

            // Le añadimos al stream un manejador de cierre para que se cierre también el ResultSet
            return stream.onClose(() -> close(rs));

        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo los equipos", e);
        }
    }

    List<Equipo> getAllEquiposAsList() {
        try (Stream<Equipo> stream = getAllEquipos()) {
            return stream.toList();
        }
    }

    Map<Integer, Equipo> getAllEquiposAsMap() {
        try (Stream<Equipo> stream = getAllEquipos()) {
            return stream.collect(
                    Collectors.toMap(
                            Equipo::getEquipoId,
                            Function.identity()
                    )
            );
        }
    }

    int addEquipo(Equipo equipo) {
        final String SQL = "INSERT INTO equipo VALUES (DEFAULT, ?, ?)";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getPais());

            if (stmt.executeUpdate() > 0) { // Se ha insertado correctamente
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                equipo.setEquipoId(generatedKeys.getInt(1));
                return equipo.getEquipoId();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return -1;
    }

    boolean deleteEquipo(Equipo equipo) {
        return deleteEquipo(equipo.getEquipoId());
        /*final String SQL = "DELETE FROM equipo WHERE  equipoId = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(SQL)) {

            stmt.setInt(1, equipo.getEquipoId());

            return stmt.executeUpdate() > 0;// Se ha eliminado correctamente

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }*/
    }

    boolean deleteEquipo(int equipoId) {
        final String SQL = "DELETE FROM equipo WHERE  equipoId = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(SQL)) {

            stmt.setInt(1, equipoId);

            return stmt.executeUpdate() > 0;// Se ha eliminado correctamente

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    Equipo findEquipoById(int Id) {
        return null;
    }

    Stream<Jugador> getAllJugadores() {
        return null;
    }

    Stream<Jugador> getJugadoresByEquipo(Equipo equipo) {
        return null;
    }

    Map<Equipo,List<Jugador>> getJugadoresByEquipoMap() {
        return null;
    }

    List<Equipo> getEquiposWithJugadores() {
        return null;
    }


}

