package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class DbDAOConnected<T> implements DAO<T>, AutoCloseable {

    protected final Connection connection;
    protected DataSource dataSource;

    protected DbDAOConnected(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.connection = this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() throws SQLException{
        return this.connection;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
    protected abstract T createDataTransferObject(ResultSet resultSet) throws SQLException;

    protected String SQL_SELECT_BY_ID;

    @Override
    public Optional<T> getById(int id) {
        try (
             PreparedStatement stmt =
                     getConnection().prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(createDataTransferObject(rs));
            }
            close(rs);
            return Optional.empty();

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    protected abstract void setAddStatementParams(PreparedStatement stmt, T t) throws SQLException;

    protected abstract void setTransferObjectID(T t, int id);

    protected String SQL_INSERT;

    @Override
    public boolean add(T t) {

        try (
             PreparedStatement stmt =
                     getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setAddStatementParams(stmt, t);
            if (stmt.executeUpdate() != 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                setTransferObjectID(t, generatedKeys.getInt(1));
            }

            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /*@Override
    public boolean add(Collection<T> collection) {

        try (
             PreparedStatement stmt =
                     getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            for (T item: collection) {
                setAddStatementParams(stmt, item);
                if (stmt.executeUpdate() != 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    generatedKeys.next();
                    setTransferObjectID(item, generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }*/


    protected abstract void setUpdateParams(PreparedStatement stmt, T t) throws SQLException;

    protected String SQL_UPDATE;

    @Override
    public boolean update(T t) {
        try (
             PreparedStatement stmt =
                     getConnection().prepareStatement(SQL_UPDATE)) {
            setUpdateParams(stmt, t);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    protected String SQL_DELETE;

    protected abstract void setDeleteParams(PreparedStatement stmt, T t) throws SQLException;

    @Override
    public boolean delete(T t) {
        try (
             PreparedStatement statement =
                     getConnection().prepareStatement(SQL_DELETE)) {
            setDeleteParams(statement, t);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    private void close(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    protected String SQL_SELECT_ALL;

    @Override
    public Stream<T> getAll() {

        try (
             PreparedStatement stmt =
                     getConnection().prepareStatement(SQL_SELECT_ALL)) {

            ResultSet rs = stmt.executeQuery();

            Stream.Builder<T> builder = Stream.builder();
            while (rs.next())
                builder.add(createDataTransferObject(rs));

            Stream<T> stream = builder.build();

            return stream.onClose(() -> close(rs));


        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
