package dam.ad.jdbc.statements.personas.query;

import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAConsumers;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class PersonaDAQuery extends PersonaDAConsumers {

    public PersonaDAQuery(Connection connection) {
        super(connection);
    }

    public void queryPersonasBySexo(String sexo) {
        JDBCQuery.query(
                connection,
                SQLs.SELECT_PERSONAS_BY_SEXO,
                stmt -> {
                    try {
                        stmt.setString(1, sexo);
                    } catch (SQLException e) {
                        throw new RuntimeException("ERROR estableciendo el parámetro Sexo", e);
                    }
                },
                PersonasPrinter::printPersonas);
    }

    public void queryPersonasNacidasAfter(LocalDate date) {
        JDBCQuery.query(
                connection,
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                getNacimientoParamSetter(date),
                new PersonasPrinter());
    }

    @Override
    public void queryPersonas(String sql, Consumer<PreparedStatement> paramSetter, Consumer<ResultSet> resultConsumer) {
        JDBCQuery.query(connection, sql, paramSetter, resultConsumer);
    }
}
