package dam.ad.jdbc.dataaccess.personas;

import dam.ad.jdbc.dataaccess.AbstractDataAccess;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.stream.Stream;

public class PersonasDataAccess extends AbstractDataAccess {

    public PersonasDataAccess(String dbURL) {
        super(dbURL);
    }


    Persona createPersona(ResultSet rs) throws SQLException {

        return new Persona(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                Sexo.fromInicial(rs.getString(4)),
                getNacimiento(rs),
                getIngresos(rs)
        );
    }

    private static LocalDate getNacimiento(ResultSet rs) throws SQLException {
        rs.getDate("nacimiento");
        if (rs.wasNull()) return null;
        return rs.getDate("nacimiento").toLocalDate();
    }

    private static Float getIngresos(ResultSet rs) throws SQLException {
        Float ingresos = rs.getFloat("ingresos");
        if (rs.wasNull()) ingresos = null;
        return ingresos;
    }

    boolean addPersona(Persona persona) {

        return JDBCQuery.update(
                getConnection(),
                "INSERT INTO persona VALUES(DEFAULT, ?, ?, ?, ?, ?)",
                stmt -> {
                    stmt.setString(1, persona.getNombre());
                    stmt.setString(2, persona.getApellidos());
                    stmt.setString(3, persona.getSexo().getInicial());
                    if (persona.getNacimiento() != null) {
                        stmt.setDate(4, Date.valueOf(persona.getNacimiento()));
                    } else {
                        stmt.setNull(4, Types.DATE);
                    }
                    if (persona.getIngresos() != null) {
                        stmt.setFloat(5, persona.getIngresos());
                    } else {
                        stmt.setNull(5, Types.REAL);
                    }
                });
    }

    boolean updatePersona(Persona persona) {
        return JDBCQuery.update(
                getConnection(),
                """
                        UPDATE persona
                        SET nombre = ?, apellidos = ?, sexo = ?, nacimiento = ?, ingresos = ?
                        WHERE personaId = ?
                        """,
                stmt -> {
                    stmt.setString(1, persona.getNombre());
                    stmt.setString(2, persona.getApellidos());
                    stmt.setString(3, persona.getSexo().getInicial());
                    stmt.setDate(4, Date.valueOf(persona.getNacimiento()));
                    stmt.setFloat(5, persona.getIngresos());
                    stmt.setInt(6, persona.getPersonaId());
                });
    }

    boolean deletePersona(Persona persona) {
        return JDBCQuery.update(
                getConnection(),
                "DELETE FROM persona WHERE personaId = ?",
                stmt -> stmt.setInt(1, persona.getPersonaId())
        );
    }

    Persona findPersona(int personaID) {
        return JDBCQuery.query(
                getConnection(),
                "SELECT * FROM persona WHERE personaId = ?",
                stmt -> stmt.setInt(1, personaID),
                this::createPersona, false).findFirst().orElse(null);
    }

    Stream<Persona> getAllPersonas() {
        return JDBCQuery.query(
                getConnection(),
                "SELECT * FROM persona",
                null,
                this::createPersona, true);
    }

}
