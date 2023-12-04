package es.rafapuig.dam.ad;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PrintDatabaseMetaData {
    static final String SQLITE_URL = "jdbc:sqlite:c:/BBDDs/sqlite/futbol.db";
    static final String DERBY_URL = "jdbc:derby:c:/BBDDs/derby/futbol";
    static final String HSQLDB_URL = "jdbc:hsqldb:file:c:/BBDDs/hsqldb/futbol";

    public static void main(String[] args) {
        printMetadata(SQLITE_URL);
        printMetadata(DERBY_URL);
        printMetadata(HSQLDB_URL);
    }

    static void printMetadata(String connectionURL) {

        try(Connection conn = DriverManager.getConnection(connectionURL)) {

            DatabaseMetaData dbmd = conn.getMetaData();

            String dbName = dbmd.getDatabaseProductName();
            String dbVersion = dbmd.getDatabaseProductVersion();
            String dbURL = dbmd.getURL();
            String dbUserName  = dbmd.getUserName();

            System.out.println("Acerca de la base de datos...");
            System.out.println("Nombre: " + dbName);
            System.out.println("Version: " + dbVersion);
            System.out.println("URL: " + dbURL);
            System.out.println("Usuario: " + dbUserName);

            String driverName = dbmd.getDriverName();
            String diverVersion = dbmd.getDriverVersion();

            System.out.println("\nAcerca del driver JDBC...");
            System.out.println("Nombre: " + driverName);
            System.out.println("Versión: " + diverVersion);

            boolean ansi92Entry = dbmd.supportsANSI92EntryLevelSQL();
            boolean ansi92Intermediate = dbmd.supportsANSI92IntermediateSQL();
            boolean ansi92Full = dbmd.supportsANSI92FullSQL();
            boolean supportsBatchUpdates = dbmd.supportsBatchUpdates();

            System.out.println("\nAcerca de las características que soporta el driver JDBC....");
            System.out.println("ansi92Entry = " + ansi92Entry);
            System.out.println("ansi92Intermediate = " + ansi92Intermediate);
            System.out.println("ansi92Full = " + ansi92Full);
            System.out.println("supportsBatchUpdates = " + supportsBatchUpdates);
            System.out.println();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}