package dam.ad.jdbc.statements.exercises;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;

public class StatementConnected {
    static final String HSQLDB_URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas20"; //create=true";

    //static Connection connection;
    public static void main(String[] args) {

        Connection conn = getConnection(HSQLDB_URL, "SA", "");

        //connection = conn;

        createDataBase(conn);

        String sqlInsert = """
                INSERT INTO persona
                VALUES(DEFAULT, 'Belen2', 'Tilla', 'M', '2000-1-1', 2000)
                """;

        //update(conn, sqlInsert);
        insertPersona(conn, "Armando", "Bronca Segura", "H",
                LocalDate.of(2003,12,23), 2500.0);
        insertPersona(conn, "Consuelo", "Teria", "M",
                LocalDate.of(1958,4,6), 2100.0);
        closeConnection(conn);
    }

    static Connection getConnection(String url, String userId, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, userId, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    static void closeConnection(Connection connection) {
        Objects.requireNonNull(connection);
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void update(Connection connection, String sql) {
        if (connection == null) return;

        try (Statement stmt = connection.createStatement()) {
            int result = stmt.executeUpdate(sql);
            connection.commit();
            System.out.println("Filas actualizadas: " + result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean execute(Connection connection, String sql) {
        Objects.requireNonNull(connection);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            rollback(connection);
        }
        return false;
    }


    static void rollback(Connection connection) {
        Objects.requireNonNull(connection);
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void createDataBase(Connection connection) {
        createTablePersona(connection);
    }

    static void createTablePersona(Connection connection) {
        Objects.requireNonNull(connection);

        String sqlCreateTablePerson ="""
            CREATE TABLE IF NOT EXISTS persona(
                personaID INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY,
                nombre VARCHAR(20) NOT NULL,
                apellidos VARCHAR(30) NOT NULL,
                sexo CHAR(1) NOT NULL,
                nacimiento DATE,
                ingresos REAL)
            """;
        System.out.println("Creando la tabla persona ...");

        boolean ok = execute(connection, sqlCreateTablePerson);
        if(ok) {
            System.out.println("Tabla persona creada ...");
        }
    }

    static void insertPersona(Connection conn, String nombre, String apellidos, String sexo, LocalDate nacimiento, Double ingresos) {
        String sql = "INSERT INTO persona VALUES(DEFAULT, " +
                     "'" + nombre + "', " +
                     "'" + apellidos + "', " +
                     "'" + sexo + "', " +
                     "{d '" + nacimiento.toString() + "'}, " +
                     ingresos + ")";

        System.out.println(sql);

        update(conn, sql);
    }


}
