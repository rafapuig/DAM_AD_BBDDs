package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.dao.jdbc.DbDAO4;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.model.futbol.Jugador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.function.Supplier;

public class DbJugadorDAO extends DbDAO4<Jugador> {

    DTOMapper<Jugador> dtoMapper;
    public DbJugadorDAO(Supplier<Connection> connectionSupplier) {
        super(connectionSupplier);
        dtoMapper = new JugadorDTOMapper();
    }

    @Override
    protected DTOMapper<Jugador> getDTOMapper() {
        return dtoMapper;
    }

    @Override
    protected String getSQLSelectByID() {
        return JugadorSQL.SELECT_BY_ID;
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(Jugador jugador) {
        return stmt -> {
            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getPais());
            stmt.setDate(3, Date.valueOf(jugador.getNacimiento()));
            stmt.setDouble(4, jugador.getEstatura());
            stmt.setInt(5, jugador.getPeso());
            stmt.setInt(6, jugador.getDorsal());
            stmt.setInt(7, jugador.getEquipoId());
            stmt.setBoolean(8, jugador.isCapitan());
        };
    }

    @Override
    protected String getSQLInsert() {
        return JugadorSQL.INSERT;
    }

    @Override
    protected void setDataTransferObjectID(Jugador jugador, int id) {
        jugador.setJugadorId(id);
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(Jugador jugador) {
        return getInsertParamSetter(jugador).andThen(
                stmt -> stmt.setInt(1, jugador.getJugadorId())
        );
    }

    @Override
    protected String getSQLUpdate() {
        return JugadorSQL.UPDATE;
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(Jugador jugador) {
        return stmt -> stmt.setInt(1, jugador.getJugadorId());
    }

    @Override
    protected String getSQLDelete() {
        return JugadorSQL.DELETE;
    }

    @Override
    protected String getSQLSelectAll() {
        return JugadorSQL.SELECT_ALL;
    }

    @Override
    protected String getSQLCount() {
        return JugadorSQL.SELECT_COUNT_ALL;
    }
}
