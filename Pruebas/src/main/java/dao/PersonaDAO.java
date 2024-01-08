package dao;

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
                (LocalDate) rs.getObject("nacimiento", LocalDate.class),
                rs.getFloat("ingresos")
        );
    }

    @Override
    public Optional<Persona> getById(int id) {
        try (Connection connection = getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement("SELECT * FROM persona WHERE personaId = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(createPersona(rs));
            }
            return Optional.empty();

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
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /*@Override
    public boolean add(Collection<Persona> collection) {
        return false;
    }*/

    @Override
    public boolean update(Persona persona) {
        return false;
    }

    @Override
    public boolean delete(Persona persona) {
        return false;
    }

    private void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<Persona> getAll() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM persona")) {

            ResultSet rs = stmt.executeQuery();

            Stream.Builder<Persona> builder = Stream.builder();
            while (rs.next())
                builder.add(createPersona(rs));

            Stream<Persona> stream = builder.build();

            return stream.onClose(() -> close(rs));

            /*return StreamSupport.stream(new Spliterators.AbstractSpliterator<Persona>(
                    Long.MAX_VALUE,
                    Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(Consumer<? super Persona> action) {
                    try {
                        if (!rs.next()) return false;
                        action.accept(createPersona(rs));
                        return true;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }, false).onClose(() -> {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });*/

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
