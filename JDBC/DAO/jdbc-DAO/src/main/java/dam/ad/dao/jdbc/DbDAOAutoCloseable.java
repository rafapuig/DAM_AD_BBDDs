package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class DbDAOAutoCloseable<T> implements DAO<T>, AutoCloseable {

    protected final DataSource dataSource;
    protected boolean keepConnected = true;

    public DbDAOAutoCloseable(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DbDAOAutoCloseable(DataSource dataSource, boolean keepConnected) {
        this.dataSource = dataSource;
        this.keepConnected = keepConnected;
    }

    Connection connection;
    private Connection getConnection() {
        try {
            if (keepConnected) {
                if(connection == null) {
                    connection = this.dataSource.getConnection();
                }
            } else {
                connection = this.dataSource.getConnection();
            }
            return connection;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract T createDataTransferObject(ResultSet resultSet) throws SQLException;

    protected String SQL_SELECT_BY_ID;

    @Override
    public Optional<T> getById(int id) {
        Connection conn = getConnection();
        try (
                PreparedStatement stmt =
                        conn.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(createDataTransferObject(rs));
            }
            close(rs);
            return Optional.empty();

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (!keepConnected) closeConnection(conn);
        }
    }

    protected abstract void setAddStatementParams(PreparedStatement stmt, T t) throws SQLException;

    protected abstract void setTransferObjectID(T t, int id);

    protected String SQL_INSERT;

    @Override
    public boolean add(T t) {
        Connection conn = getConnection();
        try (
                PreparedStatement stmt =
                        conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setAddStatementParams(stmt, t);
            if (stmt.executeUpdate() != 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                setTransferObjectID(t, generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if(!keepConnected) closeConnection(conn);
        }
    }

    /*@Override
    public boolean add(Collection<T> collection) {
        Connection conn = getConnection();
        try (
             PreparedStatement stmt =
                     conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            for (T item : collection) {
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
        } finally {
            if(!keepConnected) closeConnection(conn);
        }
    }*/


    protected abstract void setUpdateParams(PreparedStatement stmt, T t) throws SQLException;

    protected String SQL_UPDATE;

    @Override
    public boolean update(T t) {
        Connection conn = getConnection();
        try (
             PreparedStatement stmt =
                     connection.prepareStatement(SQL_UPDATE)) {
            setUpdateParams(stmt, t);
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if(!keepConnected) closeConnection(conn);
        }
    }


    protected String SQL_DELETE;

    protected abstract void setDeleteParams(PreparedStatement stmt, T t) throws SQLException;

    @Override
    public boolean delete(T t) {
        Connection conn = getConnection();
        try (
             PreparedStatement statement =
                     connection.prepareStatement(SQL_DELETE)) {
            setDeleteParams(statement, t);
            return statement.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if(!keepConnected) closeConnection(conn);
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
        Connection conn = getConnection();
        try (
                PreparedStatement stmt =
                        conn.prepareStatement(SQL_SELECT_ALL)) {

            ResultSet rs = stmt.executeQuery();

            Stream.Builder<T> builder = Stream.builder();
            while (rs.next())
                builder.add(createDataTransferObject(rs));

            Stream<T> stream = builder.build();

            return stream.onClose(() -> close(rs));

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            if (!keepConnected) closeConnection(conn);
        }
    }

    @Override
    public void close()  {
        System.out.println("Autoclosing DAO");
        closeConnection(connection);
    }
}
