package dam.ad.jdbc.statements.personas.query.stream;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.statements.personas.query.PersonaDAQuery;
import dam.ad.jdbc.query.ResultSetStream;
import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.personas.model.Persona;
import dam.ad.personas.model.Sexo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PersonaDAStream extends PersonaDAQuery {

    public PersonaDAStream(Connection connection) {
        super(connection);
    }

    Persona createPersona(ResultSet rs) throws SQLException {
        return new Persona(
                rs.getInt(1),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                Sexo.fromInicial(rs.getString("sexo")),
                rs.getDate("nacimiento").toLocalDate(),
                rs.getDouble("ingresos")
        );
    }


    public Stream<Persona> getPersonasAsStream1(
            String sql,
            Consumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();

                DTOMapper<Persona> dtoMapper = resultSet -> new Persona(
                        rs.getInt(1),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        Sexo.fromInicial(rs.getString("sexo")),
                        rs.getDate("nacimiento").toLocalDate(),
                        rs.getDouble("ingresos")
                );

                return new ResultSetStream<>(
                        Generators.getStreamGenerator(rs, dtoMapper, true));

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }

    }



    public Stream<Persona> getPersonasAsStream2(
            String sql,
            Consumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();
                DTOMapper<Persona> dtoMapper = resultSet -> createPersona(resultSet);
                return new ResultSetStream<>(Generators.getStreamGenerator(rs, dtoMapper, true));

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }

    }

    public Stream<Persona> getPersonasAsStream3(
            String sql,
            Consumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                resultSet -> createPersona(resultSet),
                true);
    }


    public Stream<Persona> getPersonasAsStream4(
            String sql,
            Consumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                new PersonaDTOMapper(),
                true);
    }



    public Stream<Persona> getPersonasAsStream(
            String sql,
            Consumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                this::createPersona,
                true);
    }

    public Stream<Persona> getPersonasAsStream(String sql) {
        return getPersonasAsStream(sql, null);
    }

}
