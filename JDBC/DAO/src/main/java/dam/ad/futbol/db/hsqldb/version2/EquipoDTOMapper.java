package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.jdbc.query.DTOMapper;
import dam.ad.model.futbol.Equipo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EquipoDTOMapper implements DTOMapper<Equipo> {
    @Override
    public Equipo apply(ResultSet resultSet) throws SQLException {
        return new Equipo(
                resultSet.getInt("equipoID"),
                resultSet.getString("nombre"),
                resultSet.getString("pais")
        );
    }
}
