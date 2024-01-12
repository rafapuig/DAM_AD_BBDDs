package dam.ad.jdbc.query;

import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.jdbc.stream.generation.Generators;

import java.sql.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * JDBCQuery permite realizar consultas mediante JDBC a través de sus métodos query
 * Estos métodos query bien consumen el ResultSet mediante un Consumer<ResultSet>
 * o bien devuelven un Stream<T> donde T es un DTO (Data Transfer Object), en la práctica
 * una clase POJO que cuanta con los mismos campos que los campos de la entidad en la tabla
 * de la base de datos
 * <p>
 * Además, JDBCQuery permite realizar operaciones DML insert, update o delete mediante otros
 * dos métodos: insert (para recuperar valores auto-numéricos) y update en el resto de escenarios
 */
public class JDBCQuery {

    /**
     * Método para enviar un comando SQL que realice una operación de actualización sobre
     * la base de datos cuando esta operación genere claves auto-numéricas
     * Por tanto, su uso será típicamente para una inserción de una nueva fila
     * cuando se haya definido una columna con valor generado auto-numérico
     * Dicho valor es el entero devuelto al llamador
     *
     * @param connection
     * @param sql         comando SQL que será un INSERT
     * @param paramSetter
     * @return El valor auto-numérico generado para la fila insertada
     */
    public static int insert(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                if (stmt.executeUpdate() != 0) {
                    try(ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        generatedKeys.next();
                        return generatedKeys.getInt(1);
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
        return -1;
    }

    /**
     * Método para efectuar una operación DML sobre la base de datos
     * Para operaciones de inserción, modificación y borrado de filas
     * En el caso de que la inserción vaya a generar un valor auto-numérico se debe
     * usar en metodo insert en lugar de este update
     *
     * @param connection
     * @param sql         comando SQL parametrizado con la operación DML a ejecutar
     * @param paramSetter
     * @return devuelve true si la operación ha modificado, borrado, o insertado con éxito
     */

    public static boolean update(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try {
                return stmt.executeUpdate() > 0;

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    /**
     * El método query efectúa una consulta SQL a la base de datos que devuelve un ResultSet
     * La consulta puede ser parametrizada, de forma que puede ser necesario proporcionar
     * un paramSetter de tipo SQLThrowingConsumer<PreparedStatement> para establecer los valores
     * de los parámetros de la consulta SQL parametrizada
     * El ResultSet obtenido es consumido por el Consumer<ResultSet>
     *
     * @param connection        conexión establecida con la base de datos
     * @param sql               consulta parametrizada o no que se quiere ejecutar contra la base de datos
     * @param paramSetter       establece los valores de los parámetros de la consulta SQL
     * @param resultSetConsumer consume los resultados de la consulta devueltos en el ResultSet
     */
    public static void query(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter,
            Consumer<ResultSet> resultSetConsumer) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            paramSetter.accept(stmt);

            try (ResultSet rs = stmt.executeQuery()) {

                resultSetConsumer.accept(rs);

                JDBCUtil.close(rs); // No hace falta ya que rs se define en un try-with-resources

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }


    /**
     * El método query que devuelve un Stream<T> donde T es el tipo del DTO
     * Necesita un DTOMapper para conocer como crear una instancia de DTO a partir
     * de los campos de un ResultSet posicionada sobre la fila de la cual queremos crear el DTO
     * El Stream generado puede recorrer por completo el ResultSet y hacer en mapeo a DTO
     * de cada fila o registro para ya tener preparados los datos al comenzar a iterar el Stream
     * En este caso, el ResultSet ya habrá sido cerrado, puesto que ya han sido leídos
     * los registros y convertidos en instancias DTO.
     * Este sistema se conoce como modo EAGER o impaciente
     * Pero si el recorrido y mapeo se deja para el momento en que se aplique una operación
     * terminal sobre el Stream entonces, el ResultSet permanecerá abierto hasta que termine
     * la operación terminal (u otra que requiera recorrer por completo los datos y cachearlos
     * como por ejemplo la operación sort() que es intermedia, pero necesita conocer todos
     * los elementos). Este modo se denomina LAZY o perezoso.
     * El parámetro yieldType permite elegir un modo u otro.
     *
     * @param connection  conexión JDBC a la base de datos
     * @param sql         la consulta SQL
     * @param paramSetter el establecedor de los valores de los parámetros de la consulta
     * @param dtoMapper   función que mapea los campos del ResultSet a una instancia de DTO
     * @param yieldType   permite elegir si queremos convertir los registros del ResultSet en DTOs de forma
     *                    impaciente o de manera perezosa (cuando se realiza operación terminal sobre el Stream)
     */
    public static <T> Stream<T> query(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter,
            DTOMapper<T> dtoMapper,
            Generators.Yield yieldType) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            //MUY IPORTANTE: NO hacemos un try-with-resources porque no podemos cerrar
            //el ResultSet automáticamente (solo lo cerramos en modo impaciente)
            try {
                ResultSet rs = stmt.executeQuery();

                Stream<T> stream = new ResultSetStream<>(rs, dtoMapper, yieldType);

                //Muy importante, solamente podemos cerrar el ResultSet si
                // el generador de Stream ha generado un Stream que ya ha recorrido el
                // resultSet y creado con el dtoMapper los elementos del Stream
                if (yieldType == Generators.Yield.EAGER) rs.close();
                // En el caso de que el Stream se cree de manera que recorra en ResultSet
                // de manera perezosa, es decir, en el momento justo en el que se va a
                // invocar una operación terminal sobre el Stream, no podemos cerrar el ResultSet
                // o se provocará una excepción.

                return stream;

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    public static <T> Stream<T> query(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter,
            DTOMapper<T> dtoMapper,
            boolean lazy) {

        return query(connection, sql, paramSetter, dtoMapper,
                lazy ? Generators.Yield.LAZY : Generators.Yield.EAGER);
    }

    /**
     * Por defecto, el Stream generado recorrerá el ResultSet de manera perezosa
     */
    public static <T> Stream<T> query(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter,
            DTOMapper<T> dtoMapper) {

        return query(connection, sql, paramSetter, dtoMapper, true);
    }


    public static SQLThrowingConsumer<PreparedStatement> createParamSetterFrom(Object... params) {
        return statement -> {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        };
    }

    /**
     * Version del método query que establece los parámetros del comandoSQL
     * a partir de una lista de valores (array de Object)
     * @param params listado de valores con los datos para sustituir por los ? en el comando SQL
     */
    public static <T> Stream<T> query(
            Connection connection,
            String sql,
            DTOMapper<T> dtoMapper,
            Object... params) {

        SQLThrowingConsumer<PreparedStatement> paramSetter =
                createParamSetterFrom(params);

        return query(connection, sql, paramSetter, dtoMapper, true);
    }

    /**
     * El método queryScalar es útil cuando la consulta SQL está diseñada para devolver
     * un único campo en la proyección y se espera que cuando se ejecute esta devuelva
     * a su vez una única fila (como máximo)
     * Por tanto, la consulta realmente devuelve un dato simple o escalar
     * @param type indica el tipo al que convertir el valor escalar que devuelve el comando SQL
     */

    public static <T> T queryScalar(
            Connection connection,
            String sql,
            SQLThrowingConsumer<PreparedStatement> paramSetter,
            Class<T> type) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getObject(1, type);
                }
                return null;

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

}
