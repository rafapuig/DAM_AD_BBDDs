package dam.ad.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionDemo {
    public static void main(String[] args) {
        String dbURL = "jdbc:sqlite:c:/BBDDs/SQLite/futbol.db";

        try(Connection conn = DriverManager.getConnection(dbURL)){
            System.out.println("Conectado a la base de datos SQLite futbol");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
