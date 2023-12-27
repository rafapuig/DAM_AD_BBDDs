package dam.ad.jdbc.statements.personas.consumers;

import dam.ad.jdbc.statements.personas.SQLs;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class PersonaDAConsumers extends dam.ad.jdbc.statements.personas.PersonaDA {

    public PersonaDAConsumers(Connection connection) {
        super(connection);
    }

    public static Consumer<PreparedStatement> sexoHombreParamSetter = stmt -> {
        try {
            stmt.setString(1, "H");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public static Consumer<PreparedStatement> sexoMujerParamSetter = stmt -> {
        try {
            stmt.setString(1, "M");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };


    public ResultSet getPersonas(String sql, Consumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            paramSetter.accept(stmt);

            try {
                return stmt.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    /**
     * Reemplazamos la implementación del método para sacar partido de la reutilización de
     * código que podemos lograr delegando en el método getPersonas con el paramSetter
     */
    @Override
    public ResultSet getPersonasBySexo(String sexo) {

        Consumer<PreparedStatement> sexoParamSetter = stmt -> {
            try {
                stmt.setString(1, sexo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        return getPersonas(SQLs.SELECT_PERSONAS_BY_SEXO, sexoParamSetter);
    }


    /**
     * En este caso vamos a crear un método de orden superior
     * para generar el Consumer<PreparedStatement>
     * que establece el parámetro de la fecha de nacimiento
     * La expresión lambda utilizará el valor del parámetro de entrada del método
     * Se trata, por tanto, de una closure o cierre
     * Un mecanismo muy util para poder parametrizar una expresión lambda
     */
    protected Consumer<PreparedStatement> getNacimientoParamSetter(LocalDate date) {
        return stmt -> {
            try {
                stmt.setDate(1, Date.valueOf(date));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Ahora ya podemos implementar la version de reemplazo del método que obtiene un ResultSet
     * con las personas nacidas después de una fecha dada como argumento de llamada
     * delegando de nuevo en el método getPersonas, proporcionándole el comando SQL
     * y el Consumer<PreparedStatement> que devuelve el método getNacimientoParamSetter
     */
    @Override
    public ResultSet getPersonasNacidasAfter(LocalDate date) {

        return getPersonas(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                getNacimientoParamSetter(date));
    }

    /**
     * Puede ser interesante que el método no devuelva el ResultSet al llamador,
     * sino que lo procese el mismo internamente (que lo consuma)
     * De esta manera nos evitamos tener que devolver un ResultSet JDBC al llamador
     * y que este tenga que acoplarse con las interfaces de JDBC
     * Para ello, añadimos otro parámetro al método que sea un Consumer<ResultSet>
     * El método ya no devuelve nada al llamador, simplemente es invocado
     * por los efectos colaterales que produce su invocación
     * Por este motivo es mejor renombrarlo como queryPersonas,
     * ya que el verbo get sugiere que se devuelve algo al llamador
     */

    public void queryPersonas(String sql,
                       Consumer<PreparedStatement> paramSetter,
                       Consumer<ResultSet> resultConsumer) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try (ResultSet rs = stmt.executeQuery()) {
                resultConsumer.accept(rs);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }


}
