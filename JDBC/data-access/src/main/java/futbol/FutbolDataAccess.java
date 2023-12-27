package futbol;

import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.dataaccess.AbstractDataAccess;
import dam.ad.model.futbol.Equipo;
import dam.ad.model.futbol.Jugador;
import dam.ad.stream.StreamUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutbolDataAccess extends AbstractDataAccess {

    public FutbolDataAccess(String dbURL) {
        super(dbURL);
    }

    /**
     * Crea una entidad Equipo a partir de leer los campos de un resultSet
     * posicionado en la fila que contiene los datos del equipo que queremos leer
     */
    static Equipo createEquipo(ResultSet rs) throws SQLException {
        return new Equipo(
                rs.getInt("equipoID"),
                rs.getString("nombre"),
                rs.getString("pais")
        );
    }


    public Stream<Equipo> getAllEquipos() {

        try (Statement stmt = getConnection().createStatement()) {

            final String SQL = "SELECT * FROM equipo";

            ResultSet rs = stmt.executeQuery(SQL);

            // Vamos a generar un Stream<Equipo> mediante un Stream Builder
            Stream.Builder<Equipo> builder = Stream.builder();
            while (rs.next()) { // mientras leamos un nuevo registro en el ResultSet ...
                builder.add(createEquipo(rs)); // Añadimos un Equipo al builder
            }
            JDBCUtil.close(rs);

            //Una vez hemos añadido todos los elementos al Stream Builder, generamos el Stream
            return builder.build();


        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo los equipos", e);
        }
    }

    public List<Equipo> getAllEquiposAsList() {
        try (Stream<Equipo> stream = getAllEquipos()) {
            return stream.toList();
        }
    }

    public Map<Integer, Equipo> getAllEquiposAsMap() {
        try (Stream<Equipo> stream = getAllEquipos()) {
            return stream.collect(
                    Collectors.toMap(
                            Equipo::getEquipoId,
                            Function.identity()
                    )
            );
        }
    }

    public int addEquipo(Equipo equipo) {
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

    public boolean deleteEquipo(Equipo equipo) {
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

    public Optional<Equipo> findEquipoById(int id) {
        final String SQL = "SELECT * FROM equipo WHERE equipoId = ?";

        try (PreparedStatement stmt = getConnection()
                .prepareStatement(SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createEquipo(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Stream<Equipo> findEquipoByName(String name) {
        final String SQL = "SELECT * FROM equipo WHERE nombre LIKE ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(SQL)) {

            stmt.setString(1, "%" + name + "%");

            ResultSet rs = stmt.executeQuery();

            return StreamUtil.
                    generateStreamFromResultSet(rs, FutbolDataAccess::createEquipo);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    static Jugador createJugador(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt(1),
                rs.getString("nombre"),
                rs.getString("pais"),
                rs.getDate("nacimiento").toLocalDate(),
                rs.getDouble("estatura"),
                rs.getInt("peso"),
                rs.getInt("dorsal"),
                rs.getInt("equipoId"),
                rs.getBoolean("capitan")
        );
    }

    Stream<Jugador> getAllJugadores() {
        return getAllJugadores(false);
    }

    public Stream<Jugador> getAllJugadores(boolean lazy) {
        try (Statement stmt = getConnection().createStatement()) {
            final String SQL = "SELECT * FROM jugador";

            ResultSet rs = stmt.executeQuery(SQL);

            return StreamUtil.generateStreamFromResultSet(
                    rs, FutbolDataAccess::createJugador, lazy);

        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo los equipos", e);
        }
    }

    public Stream<Jugador> getJugadoresByEquipo(Equipo equipo) {

        final String SQL = "SELECT * FROM jugador WHERE equipoId = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(SQL)) {

            stmt.setInt(1, equipo.getEquipoId());

            ResultSet rs = stmt.executeQuery();

            return StreamUtil.generateStreamFromResultSet(
                    rs, FutbolDataAccess::createJugador);

        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo los equipos", e);
        }
    }

    Map<Equipo, List<Jugador>> getJugadoresByEquipoMap() {
        return null;
    }

    List<Equipo> getEquiposWithJugadores() {
        return null;
    }


}

