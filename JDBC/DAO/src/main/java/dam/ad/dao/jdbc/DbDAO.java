package dam.ad.dao.jdbc;

import dam.ad.dao.DAO;
import dam.ad.jdbc.stream.generation.Generators;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class DbDAO<T> implements DAO<T> {

    protected final DataSource dataSource;

    Supplier<Connection> connectionSupplier;

    public DbDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        //connectionSupplier = this::getConnection();

    }

    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }


    protected abstract T createDataTransferObject(ResultSet resultSet) throws SQLException;

    protected String SQL_SELECT_BY_ID;

    @Override
    public Optional<T> getById(int id) {

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createDataTransferObject(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    //************** INSERT ***********************************

    protected abstract void setAddStatementParams(PreparedStatement stmt, T t) throws SQLException;

    protected abstract void setDataTransferObjectID(T t, int id);

    protected String SQL_INSERT;

    @Override
    public boolean add(T t) {

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     SQL_INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {

            setAddStatementParams(stmt, t);

            if (stmt.executeUpdate() != 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                generatedKeys.next();
                setDataTransferObjectID(t, generatedKeys.getInt(1));
                generatedKeys.close();
            }
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
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


    //**************** UPDATE *****************************************

    protected abstract void setUpdateParams(PreparedStatement stmt, T t) throws SQLException;

    protected String SQL_UPDATE;

    @Override
    public boolean update(T t) {

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            setUpdateParams(stmt, t);

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    // ****************** DELETE *********************************************

    protected String SQL_DELETE;

    protected abstract void setDeleteParams(PreparedStatement stmt, T t) throws SQLException;

    @Override
    public boolean delete(T t) {

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL_DELETE)) {

            setDeleteParams(statement, t);

            return statement.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    //************* SELECT ALL *******************************************

    protected String SQL_SELECT_ALL;

    @Override
    public Stream<T> getAll() {

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL)) {

            try (ResultSet rs = stmt.executeQuery()) {
                return generateStream(rs);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private Stream<T> generateStream2(ResultSet rs) throws SQLException {
        return Generators
                .<T>getStreamGenerator(Generators.Yield.LAZY)
                .generate(rs, this::createDataTransferObject);
    }

    private Stream<T> generateStream(ResultSet rs) throws SQLException {

        Stream.Builder<T> builder = Stream.builder();

        while (rs.next())
            builder.add(createDataTransferObject(rs));

        Stream<T> stream = builder.build();

        return stream;

        // No es necesario pues el resultSet ya es recorrido
        //return stream.onClose(() -> close(rs));
    }


    //*************** SELECT COUNT *********************************************

    protected String SQL_SELECT_COUNT;

    @Override
    public long getCount() {
        try (Connection conn = getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SQL_SELECT_COUNT)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0;
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}