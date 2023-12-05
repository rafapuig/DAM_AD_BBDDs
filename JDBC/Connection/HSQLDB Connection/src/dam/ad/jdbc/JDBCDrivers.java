package dam.ad.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
La aplicación usa la dependencia hsqldb.jar
El META-INF.services informa que el driver que debe autocargar es:
java.sql.Driver -> org.hsqldb.jdbc.JDBCDriver

De modo que no tenemos que hacer nada para el driver este cargado
cuando vamos a obtener una Conexion a una BD HSQLDB
 */

public class JDBCDrivers {
    public static void main(String[] args) {
        loadDrivers();
        print();
    }

    public static void loadDrivers() {
        loadDriverByRegister();
        loadDriverByLoadingDriverClass();
    }

    public static void loadDriversBySystemProperty() {
        String drivers = "org.hsqldb.jdbc.JDBCDriver";
        System.setProperty("jdbc.drivers", drivers);

        System.out.println(System.getProperty("jdbc.drivers"));
    }

    //Carga del driver por registro
    public static void loadDriverByRegister() {
        Driver driver = new org.hsqldb.jdbc.JDBCDriver();
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Carga el driver cargando la clase del driver en la JVM (solamente si no usamos modulos)
    public static void loadDriverByLoadingDriverClass() {

        //Carga el driver cargando la clase del driver (solamente JDK8 y sin modulos)
        //new org.hsqldb.jdbc.JDBCDriver(); // forma primera: crear un objeto
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver"); //forma segunda
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        /*
        No es necesario mantener la referencia al objeto driver porque el objetivo
        cargar la clase del driver en la JVM.
        Cuando la clase se carga en la JVM, el inicializador estático de la clase
        se ejecuta y en ese código el driver se registra a sí mismo con el DriverManager
        */
    }

    public static void print() {

        System.out.println("Lista de drivers JDBC cargados:");

        DriverManager.drivers()
                .forEach(JDBCDrivers::print);
    }

    public static void print(Driver driver) {
        String className = driver.getClass().getName();
        String moduleName = driver.getClass().getModule().getName();
        int majorVersion = driver.getMajorVersion();
        int minorVersion = driver.getMinorVersion();
        boolean jdbcCompliant = driver.jdbcCompliant();

        System.out.println("Driver Class Name: " + className);
        System.out.println("Driver Module Name: " + moduleName);
        System.out.println("Driver Major Version: " + majorVersion);
        System.out.println("Driver Minor Version: " + minorVersion);
        System.out.println("Driver JDBC Compliant: " + jdbcCompliant);
        System.out.println();
    }

    public static void unloadDriver(Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unloadDrivers() {
        DriverManager.drivers()
                .forEach(JDBCDrivers::unloadDriver);
    }

}
