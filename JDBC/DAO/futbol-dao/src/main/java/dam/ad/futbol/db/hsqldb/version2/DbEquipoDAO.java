package dam.ad.futbol.db.hsqldb.version2;

import dam.ad.dao.jdbc.DbDAO5;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;
import dam.ad.model.futbol.Equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Supplier;

public class DbEquipoDAO extends DbDAO5<Equipo> {
    DTOMapper<Equipo> dtoMapper;

    public DbEquipoDAO(Supplier<Connection> connectionSupplier) {
        super(connectionSupplier);
        dtoMapper = new EquipoDTOMapper();
    }

    @Override
    protected DTOMapper<Equipo> getDTOMapper() {
        return dtoMapper;
    }

    @Override
    protected String getSQLSelectByID() {
        return EquipoSQL.SELECT_BY_ID;
    }

    @Override
    protected Object[] getInsertParamValues(Equipo equipo) {
        return new Object[]{
                equipo.getNombre(),
                equipo.getPais()};
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(Equipo equipo) {
        return JDBCQuery.createParamSetterFrom(
                equipo.getNombre(),
                equipo.getPais()
        );
    }

    @Override
    protected String getSQLInsert() {
        return EquipoSQL.INSERT;
    }

    @Override
    protected void setDataTransferObjectID(Equipo equipo, int id) {
        equipo.setEquipoId(id);
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(Equipo equipo) {
        return getInsertParamSetter(equipo)
                .andThen(stmt -> stmt.setInt(3, equipo.getEquipoId()));
    }

    @Override
    protected String getSQLUpdate() {
        return EquipoSQL.UPDATE;
    }

    @Override
    protected SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(Equipo equipo) {
        return stmt -> stmt.setInt(1, equipo.getEquipoId());
    }

    @Override
    protected String getSQLDelete() {
        return EquipoSQL.DELETE;
    }

    @Override
    protected String getSQLSelectAll() {
        return EquipoSQL.SELECT_ALL;
    }

    @Override
    protected String getSQLCount() {
        return EquipoSQL.SELECT_COUNT_ALL;
    }
}
