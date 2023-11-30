package es.rafapuig.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionDemo {

    public static void main(String[] args) {
        String dbURL = "jdbc:sqlite:c:/BBDDs/SQLite/futbol.db";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL);
            System.out.println("Conexión a la base de datos con éxito");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn!=null) {
                try {
                    //Cerrar la conexion
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
