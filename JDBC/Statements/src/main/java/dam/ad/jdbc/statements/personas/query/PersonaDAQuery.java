package dam.ad.jdbc.statements.personas.query;

import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAConsumer;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAThrowingConsumers;
import dam.ad.jdbc.stream.ThrowingConsumer;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class PersonaDAQuery extends PersonaDAThrowingConsumers {

    public PersonaDAQuery(Connection connection) {
        super(connection);
    }

    @Override
    public void queryPersonasBySexo(String sexo) {
        JDBCQuery.query(
                connection,
                SQLs.SELECT_PERSONAS_BY_SEXO,
                stmt -> stmt.setString(1, sexo),
                PersonasPrinter::printPersonas);
    }

    @Override
    public void queryPersonasNacidasAfter(LocalDate date) {
        JDBCQuery.query(
                connection,
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                stmt -> stmt.setDate(1, Date.valueOf(date)),
                new PersonasPrinter());
    }

    @Override
    public void queryPersonas(String sql,
                              ThrowingConsumer<PreparedStatement,SQLException> paramSetter,
                              Consumer<ResultSet> resultConsumer) {

        JDBCQuery.query(connection, sql, paramSetter::accept, resultConsumer);
    }

    public void printPersonas(String sql,
                              ThrowingConsumer<PreparedStatement,SQLException> paramSetter) {

        JDBCQuery.query(connection, sql, paramSetter::accept, new PersonasPrinter());
    }
}
