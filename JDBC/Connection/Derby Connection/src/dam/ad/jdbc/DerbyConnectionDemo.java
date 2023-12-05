package dam.ad.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Para conectar a la base de datos mediante JDBC nesitamos proporcionar
la url de conexión a derby
como argumento en la llamada a DriverManager.getConnection

https://db.apache.org/derby/docs/10.17/devguide/cdevdvlp34964.html

El directorio home de las BBDDs de derby se lo tenemos que especificar en
en la configuracion de ejecucion
Edit... -> Modify Options -> Add VM options y ponerle
-Dderby.system.home=C:\BBDDs\derby
 */

public class DerbyConnectionDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        String dbURL = "jdbc:derby:futbol;create=false;user=app;pass_word=app";

        JDBCDrivers.print();
        //JDBCDrivers.unloadDrivers();
        //JDBCDrivers.print();

        //JDBCDrivers.loadDriverByRegister();
        //JDBCDrivers.loadDriversBySystemProperty();
        //JDBCDrivers.print();

        try {
            Connection conn = DriverManager.getConnection(dbURL,"app","app");
            System.out.println("Conectado a la base de datos.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
