package es.rafapuig.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PrintJDBCDrivers {

    public static void main(String[] args) {
        print();
    }

    public static void print() {

        //Asi lo cargaria 2 veces
        //Driver driver = new org.sqlite.JDBC();
        //DriverManager.registerDriver(driver);

        //Class.forName("org.sqlite.JDBC");

        System.out.println("Lista de drivers JDBC cargados:");

        DriverManager.drivers()
                .forEach(PrintJDBCDrivers::print);
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
    }
}