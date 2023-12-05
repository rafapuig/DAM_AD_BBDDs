package dam.ad.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HSQLDBConnectionDemo {
    public static void main(String[] args) {
        String dbURL = "jdbc:hsqldb:file:${hsqldb.home}/futbol";
        try (Connection conn = DriverManager.getConnection(dbURL)) {
            System.out.println("Conectado a la base de datos");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
