package dam.ad.personas.db.hsqldb;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PersonaDTOMapper implements DTOMapper<Persona> {

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
    @Override
    public Persona apply(ResultSet rs) throws SQLException {
        return new Persona(
                rs.getInt(1),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                Sexo.fromInicial(rs.getString("sexo")),
                getNacimiento(rs),
                getIngresos(rs)
        );
    }
}
