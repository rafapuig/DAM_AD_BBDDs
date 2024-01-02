package dam.ad.personas.db.hsqldb;

import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class PersonasDataSourceFactory {
    public static DataSource createDataSource(String url) throws Exception {

        Properties properties = new Properties();

        properties.setProperty("url", url); //create=true"); // ;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        properties.setProperty("shutdown", "true");

        // No tiene Connection Pool !!!!
        return JDBCDataSourceFactory.createDataSource(properties);
    }
}
