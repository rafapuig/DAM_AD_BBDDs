package dam.ad.dao.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class DbDAOUnconnected<T> extends DbDAOConnected<T> {

    DataSource dataSource;

    protected DbDAOUnconnected(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public Optional<T> getById(int id) {
        Optional<T> result = super.getById(id);
        closeConnection();
        return result;
    }

    @Override
    public boolean add(T t) {
        boolean result = super.add(t);
        closeConnection();
        return result;
    }

    @Override
    public boolean update(T t) {
        boolean result = super.update(t);
        closeConnection();
        return result;
    }

    @Override
    public boolean delete(T t) {
        boolean result = super.delete(t);
        closeConnection();
        return result;
    }

    @Override
    public Stream<T> getAll() {
        Stream<T> result = super.getAll();
        closeConnection();
        return result;
    }

    private void closeConnection() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        closeConnection();
    }
}
