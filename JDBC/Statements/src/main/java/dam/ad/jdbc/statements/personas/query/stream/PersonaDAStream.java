package dam.ad.jdbc.statements.personas.query.stream;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.statements.personas.query.PersonaDAQuery;
import dam.ad.jdbc.query.ResultSetStream;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.jdbc.stream.generation.Generators;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * Devolver un ResultSet al llamador como resultado de una consulta no es lo más conveniente
 * Consumir el ResultSet en el componente no siempre es la solución
 * Podemos hacer que el resultado de las consultas genere un Stream
 * El Stream suminitrará un elemento por cada registro recuperado de la base de datos
 * El tipo de elementos del Stream será lo que se conoce como DTO (Data Transfer Object)
 * El DTO puede ser un POJO con atributos igual a los campos de la tabla en la BBDD
 * Si tabla persona de la BBDD se corresponde con la entidad persona de un modelo E-R
 * entonces en el modelo de datos de aplicación Java le corresponderá:
 * una clase Persona
 * Por tanto, necesitamos un procedimiento para obtener una nueva instancia de tipo Persona
 * a partir de los datos del ResultSet con su cursor posicionado en una fila
 * En programación funcional esto sería un mapeo y contamos con la interface:
 * Function<T,R> donde T sería un ResultSet y R sería el tipo del DTO (Persona en este caso)
 * Pero, como los métodos getXXX() del resultSet provocan excepciones que es obligatorio controlar
 * Es más cómodo crear una version de Function con un SAM apply que declare excepciones
 * ThrowingFunction<T,R,E> con un R apply(T t) throws E
 * Como ya sabemos que T va a ser siempre un ResultSet y E una SQLException
 * podemos cerrar un poco y en lugar de usar una interfaz tan generica (3 parametros)
 * definir la interfaz DTOMapper<T> como derivada de ThrowingFunction<ResultSet, T, SQLException>
 * Solo dejamos un tipo paramétrico T que será el tipo del DTO (Persona en este caso)
 */
public class PersonaDAStream extends PersonaDAQuery {

    public PersonaDAStream(Connection connection) {
        super(connection);
    }

    /**
     * Este método devuelve un POJO Persona a partir de los valores de los campos
     * de un ResultSet posicionado en una determinada fila
     * Como recibe como parámetro un ResultSet y devuelve una Persona
     * se puede usar su referencia para asignarla a una variable de tipo DTOMapper<Persona>
     */
    Persona createPersona(ResultSet rs) throws SQLException {
        return new Persona(
                rs.getInt(1),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                Sexo.fromInicial(rs.getString("sexo")),
                rs.getDate("nacimiento").toLocalDate(),
                rs.getFloat("ingresos")
        );
    }


    private Stream<Persona> getPersonasAsStream1(
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
                        rs.getFloat("ingresos")
                );

                return new ResultSetStream<>(rs, dtoMapper, Generators.Yield.LAZY);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }


    private Stream<Persona> getPersonasAsStream2(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();
                DTOMapper<Persona> dtoMapper = resultSet -> createPersona(resultSet);
                return new ResultSetStream<>(rs, dtoMapper, Generators.Yield.LAZY);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    private Stream<Persona> getPersonasAsStream3(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                ResultSet rs = stmt.executeQuery();
                DTOMapper<Persona> dtoMapper = this::createPersona;

                return new ResultSetStream<>(rs, dtoMapper, Generators.Yield.LAZY);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    private Stream<Persona> getPersonasAsStream4(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                resultSet -> createPersona(resultSet),
                true);
    }

    private Stream<Persona> getPersonasAsStream5(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                this::createPersona,
                true);
    }


    private Stream<Persona> getPersonasAsStream6(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                new PersonaDTOMapper(),
                true);
    }


    /**
     * La version definitiva del método getPersonasAsStream
     * @param sql comando SQL que vamos a ejecutar
     * @param paramSetter establecedor de los parámetros del comando
     * @return un Stream<Persona>
     */
    public Stream<Persona> getPersonasAsStream(
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        return JDBCQuery.query(
                connection,
                sql,
                paramSetter,
                new PersonaDTOMapper(),
                Generators.Yield.LAZY);
    }

    public Stream<Persona> getPersonasAsStream(String sql) {
        return getPersonasAsStream(sql, null);
    }

}
