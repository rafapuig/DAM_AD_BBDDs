package dam.ad.personas.db.hsqldb.version1;

import dam.ad.dao.DAO;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;


import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class PersonaDAO implements DAO<Persona> {

    private final DataSource dataSource;

    public PersonaDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private Persona createPersona(ResultSet rs) throws SQLException {
        return new Persona(
                rs.getInt("personaId"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                Sexo.fromInicial(rs.getString("sexo")),
                rs.getObject("nacimiento", LocalDate.class),
                rs.getFloat("ingresos")
        );
    }

    @Override
    public Optional<Persona> getById(int id) {

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM persona WHERE personaId = ?")) {

            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createPersona(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean add(Persona persona) {

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO persona VALUES (DEFAULT,?,?,?,?,?)")) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getSexo().getInicial());
            stmt.setObject(4, persona.getNacimiento(), Types.DATE);
            stmt.setDouble(5, persona.getIngresos());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    @Override
    public boolean update(Persona persona) {

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     """
                             UPDATE persona 
                             SET nombre = ?, apellidos = ?, sexo = ?, nacimiento = ?, ingresos = ?
                             WHERE personaId = ?""")) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getSexo().getInicial());
            stmt.setObject(4, persona.getNacimiento(), Types.DATE);
            stmt.setDouble(5, persona.getIngresos());
            stmt.setInt(6, persona.getPersonaId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean delete(Persona persona) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM persona WHERE personaId = ?")) {

            stmt.setInt(1, persona.getPersonaId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<Persona> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM persona")) {

            try (ResultSet rs = stmt.executeQuery()) {

                Stream.Builder<Persona> builder = Stream.builder();

                while (rs.next())
                    builder.add(createPersona(rs));

                return builder.build();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
