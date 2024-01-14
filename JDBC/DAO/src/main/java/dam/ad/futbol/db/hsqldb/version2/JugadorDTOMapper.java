package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.futbol.Jugador;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JugadorDTOMapper implements DTOMapper<Jugador> {
    @Override
    public Jugador apply(ResultSet resultSet) throws SQLException {
        return new Jugador(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                //resultSet.getObject(4, LocalDate.class),
                resultSet.getDate(4).toLocalDate(),
                resultSet.getDouble(5),
                resultSet.getInt(6),
                resultSet.getInt(7),
                resultSet.getInt(8),
                resultSet.getBoolean(9)
        );
    }
}
