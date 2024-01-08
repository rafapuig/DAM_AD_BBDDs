package dam.ad.jdbc.statements.personas.consumers;

import dam.ad.jdbc.statements.personas.PersonaDA;
import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Hereda de PersonaDA modificándola para que mediante programación funcional
 * se le pase comportamiento parametrizado a algunos métodos de la clase
 * Se utilizará la interfaz funcional Consumer<T>
 */

public class PersonaDAConsumer extends PersonaDA {

    public PersonaDAConsumer(Connection connection) {
        super(connection);
    }

    /**
     * Mediante un Consumer<PrepareStatement> podemos definir el código que queremos
     * ejecutar dado un PreparedStatement, es decir, qué haremos para "consumirlo"
     * En este caso, dado el PreparedStatement, lo que haremos será establecer
     * el parámetro de índice 1 al valor "H" y capturar las excepciones
     * Este Consumer será util cuando se quiera realizar la consulta parametrizada
     * que filtra las personas por sexo
     */
    public static Consumer<PreparedStatement> sexoHombreParamSetter = stmt -> {
        try {
            stmt.setString(1, "H");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Y en este otro caso lo establecemos a "M" para filtrar las mujeres
     */
    public static Consumer<PreparedStatement> sexoMujerParamSetter = stmt -> {
        try {
            stmt.setString(1, "M");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Las dos expresiones lambda anteriores son prácticamente idénticas, salvo por el valor
     * "H" o "M" pasado como segundo argumento en la llamada a setString
     * Para parametrizar una expresión lambda podemos escribir un método que reciba, como
     * argumento en la llamada, el valor que queremos parametrizar en la expresión lambda
     * y capturarlo dentro de la expresión lambda,
     * Las expresiones lambda que utilizan variables dentro del mismo ámbito (scope) en
     * el que está definida la propia expresión lambda se denominan cierres o clousures
     * @param sexo String que debería tomar como valores "H" para hombres o "M" para mujeres
     * @return la expresión lambda de tipo Consumer<PreparedStatement>
     */
    public static Consumer<PreparedStatement> getSexoParamSetter(String sexo) {
        return stmt -> {
            try {
                stmt.setString(1, sexo);

            } catch (SQLException e) {
                throw new RuntimeException("ERROR estableciendo el valor del parámetro SEXO", e);
            }
        };
    }

    /**
     * Ahora tenemos un método de consultas parametrizadas genérico, en el cual
     * se ha codificado toda la parte común a toda consulta que vaya a necesitar
     * establecer parámetros y devolver un ResultSet
     * Pero el código que establece los valores de los parámetros está delegado
     * al Consumer<PreparedStatement> paramSetter
     * @param paramSetter Consumer que contiene el código que establece el valor de los parámetros de la consulta
     */

    public ResultSet getPersonas(String sql, Consumer<PreparedStatement> paramSetter) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Proporcionamos la instancia PreparedStatement que el consumer necesita
            // al invocar al SAM accept de Consumer
            // El Consumer<PreparedStatement> ha sido proporcionado como argumento de llamada
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

        //No repetimos el código para preparar el comando, ejecutarlo y devolver
        // el resultset dado que contamos un método al cual le pasamos el código
        // que establece el valor de los parámetros del comando SQL
        // el segundo argumento de la llamada es una instancia de Consumer<PreparedStatement>
        return getPersonas(SQLs.SELECT_PERSONAS_BY_SEXO, sexoParamSetter);
    }

    /**
     * En esta segunda version vemos que tan conciso puede quedar el método
     * Si ya tenemos un método que nos devuelve el Consumer<PreparedStatement>
     */
    public ResultSet getPersonasBySexo2(String sexo) {
        return getPersonas(SQLs.SELECT_PERSONAS_BY_SEXO, getSexoParamSetter(sexo));
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
     * ya que el verbo get sugiere que se devuelve algo al llamador y puede dar lugar
     * a equívocos
     */
    public void queryPersonas(String sql,
                              Consumer<PreparedStatement> paramSetter,
                              Consumer<ResultSet> resultConsumer) {

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (paramSetter != null) paramSetter.accept(stmt);

            //El resultSet lo obtenemos en un try-with-resources para que
            //se cierre automáticamente sin explicitarlo en un bloque finally
            try (ResultSet rs = stmt.executeQuery()) {
                resultConsumer.accept(rs);  //Consumimos el resultSet

            } catch (SQLException e) {
                throw new RuntimeException("ERROR ejecutando el comando SQL:" + sql, e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR preparando el comando SQL: " + sql, e);
        }
    }

    /**
     * Aprovechamos el método genérico de consultas con consumo de los resultados
     * para este método específico que consulta personas filtradas según el sexo
     * proporcionado como argumento de llamada
     */
    public void queryPersonasBySexo(String sexo, Consumer<ResultSet> resultSetConsumer) {
        queryPersonas(
                SQLs.SELECT_PERSONAS_BY_SEXO,
                getSexoParamSetter(sexo),
                resultSetConsumer);
    }

    /**
     * Podríamos haber ido más lejos y "mojarnos" con el Consumer<ResultSet>
     * Con este método establecemos directamente el consumidor del ResultSet
     * La clase PrintPersonas tiene un método estático printPersonas que recibe
     * como argumento un ResultSet, por tanto, se puede usar su referencia a método
     * como valor del argumento resultSetConsumer, dicho método imprime por consola
     * las filas resultantes.
     */
    public void queryPersonasBySexo(String sexo) {
        queryPersonas(
                SQLs.SELECT_PERSONAS_BY_SEXO,
                getSexoParamSetter(sexo),
                PersonasPrinter::printPersonas
        );
    }

    /**
     * En este método vemos que también podemos hacer lo mismo para filtrar por
     * la fecha de nacimiento y obtener las personas nacidas después de la fecha
     * proporcionada como parámetro e imprimirlas por la consola
     * Dado que la clase PersonasPrinter implementa la interfaz Consumer<ResultSet>
     * también podemos pasarle una instancia de PersonasPrinter como tercer argumento
     */
    public void queryPersonasNacidasAfter(LocalDate date) {
        queryPersonas(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                getNacimientoParamSetter(date),
                new PersonasPrinter());
    }

}
