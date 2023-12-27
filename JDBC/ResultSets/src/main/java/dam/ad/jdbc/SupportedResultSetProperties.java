package dam.ad.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static java.sql.ResultSet.*;

public class SupportedResultSetProperties {

    static String HSQLDB_URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas";
    static String DERBY_URL = "jdbc:derby:C:/BBDDs/derby/futbol";

    public static void main(String[] args) {

        Connection connection = null;
        try {
            connection = JDBCUtil.getConnection(DERBY_URL);
            DatabaseMetaData dbmd = connection.getMetaData();

            System.out.println("Características de SCROLLABILIDAD soportadas por ResultSet");
            printScrollabilityInfo(dbmd);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            JDBCUtil.close(connection);
        }
    }

    static void printScrollabilityInfo(DatabaseMetaData dbmd) {
        try {
            boolean forwardOnly = dbmd.supportsResultSetType(TYPE_FORWARD_ONLY);
            boolean scrollSensitive = dbmd.supportsResultSetType(TYPE_SCROLL_SENSITIVE);
            boolean scrollInsensitive = dbmd.supportsResultSetType(TYPE_SCROLL_INSENSITIVE);

            System.out.println("Forward-Only: " + forwardOnly);
            System.out.println("Scroll-Sensitive: " + scrollSensitive);
            System.out.println("Scroll-Insensitive: " + scrollInsensitive);

        } catch (SQLException e) {
            System.out.println("No se pudo recuperar información sobre Scrollabilidad");
        }
    }

}
