package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.jdbc.JDBCUtil;
import dam.ad.jdbc.query.DTOMapper;
import dam.ad.jdbc.query.JDBCQuery;
import dam.ad.jdbc.stream.SQLThrowingConsumer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class DbDAO2<T> implements DAO<T> {

    protected final DataSource dataSource;

    public DbDAO2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getNewConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        //return currentConnection;
        return getNewConnection();
    }

    Connection currentConnection;

    public void beginTransaction() {
        currentConnection = getNewConnection();
        JDBCUtil.setAutoCommit(currentConnection, false);
    }

    public void commit() {
        JDBCUtil.commit(currentConnection);
        JDBCUtil.close(currentConnection);
        currentConnection = null;
    }

    public void rollback() {
        JDBCUtil.rollback(currentConnection);
        currentConnection = null;
    }

    protected abstract DTOMapper<T> getDTOMapper();

    protected abstract String getSQLSelectByID();

    @Override
    public Optional<T> getById(int id) {

        try (Connection connection = getConnection()) {

            return JDBCQuery.query(
                    connection,
                    getSQLSelectByID(),
                    preparedStatement -> preparedStatement.setInt(1, id),
                    getDTOMapper()).findFirst();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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
}
