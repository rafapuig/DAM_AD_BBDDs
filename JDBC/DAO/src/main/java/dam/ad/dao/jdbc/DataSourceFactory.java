package dam.ad.dao.jdbc;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceFactory {

    static {
        try {
            System.out.println("Registering data source...");
            //registerDataSource();
            registerDerbyDataSource();
            registerHSQLDBDataSource();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static DataSource createDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:hsqldb:C:/BBDDs/hsqldb/futbol"); //create=true"); // ;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");

        // No tiene Connection Pool !!!!
        return JDBCDataSourceFactory.createDataSource(properties);
    }

    static void registerDataSource() throws Exception {

        //Properties props = new Properties();
        //props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.impl.SerialInitContextFactory");
        //Context initialContext = new InitialContext(props);

        Context initialContext = new InitialContext();
        JDBCDataSource dataSource = new JDBCDataSource();
        //dataSource.setDatabaseName();

        dataSource.setURL("C:/BBDDs/hsqldb/futbol");
        dataSource.setUser("SA");
        dataSource.setPassword("");

        initialContext.bind("jdbc/futbol", dataSource);
    }

    static void registerHSQLDBDataSource() throws Exception {
        System.out.println("Registrando JDBC BD Futbol en HSQLDB...");

        Context initialContext = new InitialContext();
        JDBCDataSource dataSource = new JDBCDataSource();

        dataSource.setURL("C:/BBDDs/hsqldb/JDBCfutbol");
        dataSource.setUser("SA");
        dataSource.setPassword("");

        initialContext.bind("jdbc/futbol", dataSource);
    }

    static void registerDerbyDataSource() throws Exception {
        System.out.println("Registrando JDBC BD Futbol en Apache Derby...");

        Context initialContext = new InitialContext();
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("C:/BBDDs/derby/futbol");

        initialContext.bind("jdbc/derby/futbol", dataSource);
    }


    public DataSource getDataSource(String name) throws NamingException {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup(name);
        return dataSource;
    }

    private static DataSourceFactory factory;
    public static DataSourceFactory getInstance() {
        if(factory == null) {
            factory = new DataSourceFactory();
        }
        return factory;
    }

}
