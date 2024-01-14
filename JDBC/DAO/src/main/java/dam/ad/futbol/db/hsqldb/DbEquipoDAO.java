package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.jdbc.TemplateDbDAO;
import dam.ad.model.futbol.Equipo;


import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbEquipoDAO extends TemplateDbDAO<Equipo> {
    public DbEquipoDAO(DataSource dataSource) {
        super(dataSource);
        SQL_INSERT = "INSERT INTO equipo VALUES (DEFAULT, ?, ?)";
        SQL_SELECT_ALL = "SELECT * FROM equipo";
        SQL_SELECT_BY_ID = "SELECT * FROM equipo WHERE equipoId = ?";
        SQL_UPDATE = "UPDATE equipo SET NOMBRE = ?, PAIS = ? WHERE equipoId = ?";
        SQL_DELETE = "DELETE FROM equipo WHERE equipoId = ?";
        SQL_SELECT_COUNT = "SELECT COUNT(*) FROM equipo";
    }

    @Override
    protected Equipo createDataTransferObject(ResultSet resultSet) throws SQLException {
        return new Equipo(
                resultSet.getInt("equipoID"),
                resultSet.getString("nombre"),
                resultSet.getString("pais")
        );
    }

    @Override
    protected void setAddStatementParams(PreparedStatement stmt, Equipo equipo) throws SQLException {
        stmt.setString(1, equipo.getNombre());
        stmt.setString(2, equipo.getPais());
    }

    @Override
    protected void setDataTransferObjectID(Equipo equipo, int id) {
        equipo.setEquipoId(id);
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Equipo equipo) throws SQLException {
        setAddStatementParams(stmt, equipo);
        stmt.setInt(3, equipo.getEquipoId());
    }

    @Override
    protected void setDeleteParams(PreparedStatement stmt, Equipo equipo) throws SQLException {
        stmt.setInt(1, equipo.getEquipoId());
    }
}
