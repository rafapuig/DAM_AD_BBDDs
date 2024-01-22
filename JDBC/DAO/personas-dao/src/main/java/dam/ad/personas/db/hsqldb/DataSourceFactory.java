package dam.ad.personas.db.hsqldb;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.hsqldb.jdbc.JDBCDriver;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class DataSourceFactory {
    public static DataSource createDataSource(String url) throws Exception {

        Driver driver = getDriver(url);

        switch (driver) {
            case EmbeddedDriver embeddedDriver -> {
                return createDerbyEmbeddedDataSource(url);
            }
            case JDBCDriver jdbcDriver -> {
                return createHSQLDBDataSource(url);
            }
            case null -> {
                return null;
            }
            default -> throw new IllegalStateException("Unexpected value: " + driver);
        }
    }

    private static Driver getDriver(String url) throws SQLException {
        return DriverManager.getDriver(url);
    }

    private static Driver getDriverAlternativo(String url) {

        Optional<Driver> driver = DriverManager.drivers()
                .filter(d -> {
                    try {
                        return d.acceptsURL(url);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).findAny();
        return driver.orElse(null);
    }


    private static DataSource createHSQLDBDataSource(String url) throws Exception {

        if (JDBCDriver.driverInstance.acceptsURL(url)) {

            Properties properties = new Properties();

            properties.setProperty("url", url); //create=true"); // ;shutdown=true");
            properties.setProperty("user", "SA");
            properties.setProperty("password", "");
            properties.setProperty("shutdown", "true");

            // No tiene Connection Pool !!!!
            return JDBCDataSourceFactory.createDataSource(properties);
        }
        return null;
    }

    private static DataSource createDerbyEmbeddedDataSource(String url) {
        EmbeddedDataSource embeddedDataSource = new EmbeddedDataSource();
        embeddedDataSource.setDatabaseName(url.split(":")[2]);
        return embeddedDataSource;
    }
}
