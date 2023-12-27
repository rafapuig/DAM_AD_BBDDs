package dam.ad.jdbc.statements.personas.query.stream;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.personas.model.Persona;
import dam.ad.personas.model.Sexo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonaDTOMapper extends DTOMapper<Persona> {
    @Override
    public Persona apply(ResultSet rs) throws SQLException {
        return new Persona(
                rs.getInt(1),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                Sexo.fromInicial(rs.getString("sexo")),
                rs.getDate("nacimiento").toLocalDate(),
                rs.getDouble("ingresos")
        );
    }
}
