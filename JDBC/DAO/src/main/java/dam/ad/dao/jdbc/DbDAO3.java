package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * DbDao3 está pensada para funcionar en cooperación con un DAOManager
 * El DAOManager se encarga de gestionar las conexiones y transacciones
 * Por tanto, el DbDAO3 no administra ningún recurso y no necesita ser Autocloseable
 */
public abstract class DbDAO3<T> implements DAO<T> {
    DAOManager daoManager;

    public DbDAO3(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    /**
     * Devuelve la conexión que es a su vez obtenida a través del DAOManager
     */
    protected Connection getConnection() {
        return daoManager.getConnection();
    }

    protected abstract DTOMapper<T> getDTOMapper();

    protected abstract String getSQLSelectByID();

    @Override
    public Optional<T> getById(int id) {

        return JDBCQuery.query(
                getConnection(),
                getSQLSelectByID(),
                preparedStatement -> preparedStatement.setInt(1, id),
                getDTOMapper()).findFirst();
    }


    // *********** ADD *****************************************************************
    protected abstract SQLThrowingConsumer<PreparedStatement> getInsertParamSetter(T t);

    protected abstract String getSQLInsert();

    protected abstract void setDataTransferObjectID(T t, int id);

    @Override
    public boolean add(T t) {
        int id = JDBCQuery.insert(
                getConnection(),
                getSQLInsert(),
                getInsertParamSetter(t));
        if (id >= 0) {
            setDataTransferObjectID(t, id);
            return true;
        }
        return false;
    }

    //*********** UPDATE ******************************************************

    protected abstract SQLThrowingConsumer<PreparedStatement> getUpdateParamSetter(T t);

    protected abstract String getSQLUpdate();

    @Override
    public boolean update(T t) {
        return JDBCQuery.update(
                getConnection(),
                getSQLUpdate(),
                getUpdateParamSetter(t));
    }


    // ************ DELETE ************************************
    protected abstract SQLThrowingConsumer<PreparedStatement> getDeleteParamSetter(T t);

    protected abstract String getSQLDelete();

    @Override
    public boolean delete(T t) {
        return JDBCQuery.update(
                getConnection(),
                getSQLDelete(),
                getDeleteParamSetter(t)
        );
    }

    protected abstract String getSQLSelectAll();

    @Override
    public Stream<T> getAll() {
        return JDBCQuery.query(
                getConnection(),
                getSQLSelectAll(),
                null,
                getDTOMapper());
    }

    protected abstract String getSQLCount();

    @Override
    public long getCount() {
        return JDBCQuery.queryScalar(
                getConnection(),
                getSQLCount(),
                null,
                Long.class).longValue();
    }

    Long toLong(ResultSet resultSet) throws SQLException {
        resultSet.next();
        return resultSet.getLong(1);
    }

    public long getCount2() {
        return JDBCQuery.query(
                getConnection(),
                getSQLCount(),
                null,
                this::toLong).findFirst().orElse(0L);
    }

    protected SQLThrowingConsumer<PreparedStatement> getParamSetterFromParamList(List<Object> params) {
        return statement -> {
            for (int i = 1; i <= params.size(); i++) {
                statement.setObject(i, params.get(i - 1));
            }
        };
    }
}
