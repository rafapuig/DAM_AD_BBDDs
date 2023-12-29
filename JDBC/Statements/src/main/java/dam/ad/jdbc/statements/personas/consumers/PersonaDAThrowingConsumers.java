package dam.ad.jdbc.statements.personas.consumers;

import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.stream.ThrowingConsumer;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * En esta clase PersonaDA hacemos que para establecer los parámetros del PreparedStatement
 * se utilice, en lugar de un Consumer<PreparedStatement>, el cual obliga a tratar la excepción
 * en un bloque try, un ThrowingConsumer<PreparedStatement, SQLException>.
 * El interface ThrowingConsumer<T,E extends Exception> lo habremos definido de manera que
 * el Single Abstract Method "void accept(T t) throws E" declare que lanza una excepción de tipo E
 * De esta manera podemos definir la expresión lambda que asigna valores a los parámetros del
 * PreparedStatement sin tener que capturar las excepciones
 */
public class PersonaDAThrowingConsumers extends PersonaDAConsumer {
    public PersonaDAThrowingConsumers(Connection connection) {
        super(connection);
    }

    /**
     * Esta sería la versión del ParamSetter que establece el parámetro sexo como H cuando
     * se va a consultar con el comando SQL parametrizado "SELECT * FROM persona WHERE sexo = ?"
     * Ya no es necesario envolver la instrucción setXXX (que puede lanzar SQLException)
     * dentro de un bloque try gracias a que usamos la interfaz ThrowingConsumer
     */
    public static ThrowingConsumer<PreparedStatement, SQLException> sexoHombreParamSetterNoEx =
            stmt -> stmt.setString(1, "H");

    /**
     * Lo mismo ocurre con la version que establece el parámetro sexo como M
     */
    public static ThrowingConsumer<PreparedStatement, SQLException> sexoMujerParamSetterNoEx =
            stmt -> stmt.setString(1, "M");

    /**
     * La interface ThrowingConsumer es menos estricta que Consumer, en la primera no se
     * exige sé que controlen las excepciones, se pueden elevar al llamador
     * La expresión que se asigne a un ThrowingConsumer puede o no controlar la excepción
     * mientras que a un Consumer la expresión debe controlar la excepción
     * Por tanto, podemos asignar una instancia de Consumer<PreparedStatement> a una variable
     * de tipo ThrowingConsumer mediante la referencia a método, a su SAM
     * Al contrario, no funcionará porque una expresión asignada a un ThrowingConsumer
     * no está garantizado que haya controlado la excepción en lugar de propagarla al llamador
     */
    public static ThrowingConsumer<PreparedStatement, SQLException> sexoMujerParamSetterNoExLegacy =
            sexoMujerParamSetter::accept;

    public static ThrowingConsumer<PreparedStatement, SQLException> sexoHombreParamSetterNoExLegacy =
            sexoHombreParamSetter::accept;

    /**
     * No compila por los motivos explicados, descomentar para ver el error que indica el compilador
     */
    //Consumer<PreparedStatement> sexoHombreParamSetterFromThrowingConsumer = sexoHombreParamSetterNoEx::accept;


    /**
     * La versión del método de orden superior para definir la expresión lambda parametrizada
     * o clousure a partir del parámetro de entrada del método, pero al ser asociada con
     * la interfaz ThrowingConsumer no es necesario controlar la excepción SQLException
     * que puede provocar la llamada al método setString del PreparedStatement
     */
    public ThrowingConsumer<PreparedStatement, SQLException> getSexoParamSetterNoEx(String sexo) {
        return stmt -> stmt.setString(1, sexo);
    }

    public ResultSet getPersonas(String sql, ThrowingConsumer<PreparedStatement, SQLException> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

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
     * Podemos reemplazar la version de la clase base para que delegue ahora
     * en la version que acabamos de implementar de getPersonas pero con un
     * ThrowingConsumer
     * Como Consumer no es compatible con ThrowingConsumer (recuérdese tema de genéricos)
     * usamos la referencia a método para asignar:
     * paramSetter::accept
     */
    @Override
    public ResultSet getPersonas(String sql, Consumer<PreparedStatement> paramSetter) {
        ThrowingConsumer<PreparedStatement, SQLException> paramSetterNoEx = paramSetter::accept;
        return this.getPersonas(sql, paramSetterNoEx);
    }

    @Override
    public void queryPersonas(String sql, Consumer<PreparedStatement> paramSetter, Consumer<ResultSet> resultConsumer) {
        ThrowingConsumer<PreparedStatement, SQLException> paramSetterNoEx = paramSetter::accept;
        this.queryPersonas(sql, paramSetterNoEx, resultConsumer);
    }

    public void queryPersonas(String sql, ThrowingConsumer<PreparedStatement, SQLException> paramSetter, Consumer<ResultSet> resultConsumer) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            try(ResultSet rs = stmt.executeQuery()) {
                resultConsumer.accept(rs);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    @Override
    public void queryPersonasNacidasAfter(LocalDate date) {

        queryPersonas(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                (ThrowingConsumer<PreparedStatement, SQLException>)
                        stmt -> stmt.setDate(1, Date.valueOf(date)),
                new PersonasPrinter());
    }
}
