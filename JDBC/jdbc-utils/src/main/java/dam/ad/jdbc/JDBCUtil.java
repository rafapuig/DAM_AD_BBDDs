package dam.ad.jdbc;

import java.sql.*;

public class JDBCUtil {

    public static Connection getConnection(String dbURL) {
        try {
            return DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR obteniendo la conexión a la Base de Datos", e);
        }
    }

    /**
     * Cierra un Statement
     */
    public static void close(Statement statement) {
        if(statement == null) return;
        try {
            if(!statement.isClosed()) {
                System.out.println("Cerrando statement...");
                statement.close();
            } else {
                System.out.println("El statement ya esta cerrado.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Cierra un ResultSet, se debe llamar cuando ya hemos acabado de leer los datos que contenía
     */
    public static void close(ResultSet resultSet) {
        try {
            if(!resultSet.isClosed()) {
                System.out.println("Cerrando resultSet...");
                resultSet.close();
            } else {
                System.out.println("El resultSet ya esta cerrado.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Envía un commando para que apague el motor del gestor de base de datos
     */
    public static void shutdown(Connection connection) {
        if (connection == null) return;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN");
        } catch (SQLException e) {
            throw new RuntimeException("ERROR apagando la base de datos", e);
        }

    }

    /**
     * Cierra una conexion JDBC a una base de datos
     */
    public static void close(Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "ERROR cerrando la conexión con la base de datos", e);
        }
    }

    public static void setAutoCommit(Connection connection, boolean autoCommit) {
        if(connection == null) return;
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR estableciendo el auto-commit", e);
        }
    }

    public static void commit(Connection connection) {
        if(connection == null) return;
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR realizando el commit", e);
        }
    }

    public static void rollback(Connection connection) {
        if(connection == null) return;
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR realizando el rollback", e);
        }
    }
}

