package dam.ad.jdbc.dataaccess;

import dam.ad.jdbc.JDBCUtil;

import java.sql.Connection;

public class AbstractDataAccess implements AutoCloseable {
    Connection connection;

    public AbstractDataAccess(String dbURL) {
        openConnection(dbURL);  //Inicia la conexión a la base de datos
    }

    void openConnection(String dbURL) {
        connection = JDBCUtil.getConnection(dbURL);
    }

    protected Connection getConnection() {
        return this.connection;
    }

    public void setAutoCommit(boolean autoCommit) {
        JDBCUtil.setAutoCommit(getConnection(), autoCommit);
    }

    public void commit() {
        JDBCUtil.commit(getConnection());
    }

    public void rollback() {
        JDBCUtil.rollback(getConnection());
    }

    @Override
    public void close() {
        closeConnection();
    }

    /**
     * Método para cerrar la conexión y apagar la base de datos
     */
    void closeConnection() {
        JDBCUtil.shutdown(getConnection());
        JDBCUtil.close(getConnection());
    }
}
