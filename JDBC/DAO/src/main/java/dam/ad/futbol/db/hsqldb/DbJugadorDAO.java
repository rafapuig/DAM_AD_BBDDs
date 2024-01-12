package dam.ad.futbol.db.hsqldb;

import dam.ad.dao.jdbc.DbDAO;
import dam.ad.model.futbol.Jugador;


import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbJugadorDAO extends DbDAO<Jugador> {
    public DbJugadorDAO(DataSource dataSource) {
        super(dataSource);
        SQL_INSERT = "INSERT INTO jugador VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQL_SELECT_ALL = "SELECT * FROM jugador";
        SQL_SELECT_BY_ID = "SELECT * FROM jugador WHERE jugadorId = ?";
        SQL_UPDATE = "UPDATE jugador SET NOMBRE = ?, PAIS = ?, NACIMIENTO=?, ESTATURA=?, PESO=?,DORSAL=?, EQUIPOID=?, CAPITAN=? WHERE jugadorId = ?";
        SQL_DELETE = "DELETE FROM jugador WHERE jugadorId = ?";
        SQL_SELECT_COUNT = "SELECT COUNT(*) FROM jugador";
    }

    @Override
    protected Jugador createDataTransferObject(ResultSet resultSet) throws SQLException {
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

    @Override
    protected void setAddStatementParams(PreparedStatement stmt, Jugador jugador) throws SQLException {
        stmt.setString(1, jugador.getNombre());
        stmt.setString(2, jugador.getPais());
        //stmt.setObject(3, jugador.getNacimiento());
        stmt.setDate(3, Date.valueOf(jugador.getNacimiento()));
        stmt.setDouble(4, jugador.getEstatura());
        stmt.setInt(5, jugador.getPeso());
        stmt.setInt(6, jugador.getDorsal());
        stmt.setInt(7, jugador.getEquipoId());
        stmt.setBoolean(8, jugador.isCapitan());
    }

    @Override
    protected void setDataTransferObjectID(Jugador jugador, int id) {
            jugador.setJugadorId(id);
    }

    @Override
    protected void setUpdateParams(PreparedStatement stmt, Jugador jugador) throws SQLException {
        setAddStatementParams(stmt, jugador);
        stmt.setInt(9, jugador.getJugadorId());
    }

    @Override
    protected void setDeleteParams(PreparedStatement stmt, Jugador jugador) throws SQLException {
        stmt.setInt(1, jugador.getJugadorId());
    }
}
