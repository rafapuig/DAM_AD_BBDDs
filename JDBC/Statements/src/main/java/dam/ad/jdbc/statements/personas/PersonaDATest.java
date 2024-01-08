package dam.ad.jdbc.statements.personas;

import java.sql.*;
import java.time.LocalDate;

public class PersonaDATest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection(URL);

        PersonaDA personaDA = new PersonaDA(connection);

        createTablePersona(personaDA);

        testInsertPersonas(personaDA);

        printPersonas(personaDA);

        testGetPersona(personaDA);

        testUpdatePersona(personaDA);

        printPersonasPorSexo(personaDA);

        printPersonasNacidasAfter(personaDA);

        testDeletePersonas(personaDA);

        connection.close();
    }


    public static void createTablePersona(PersonaDA personaDA) {
        System.out.println("Eliminando la tabla persona si existe...");
        personaDA.execute(SQLs.DROP_TABLE_PERSONA);

        System.out.println("Creando la tabla persona...");
        personaDA.createTablePersona();
    }

    public static void testInsertPersonas(PersonaDA personaDA) {

        insertPersona(personaDA, "Armando", "Bronca Segura", "H",
                LocalDate.of(1970, 8, 3), 2500.0f);

        insertPersona(personaDA, "Belen", "Tilla", "M",
                LocalDate.of(1970, 8, 3), 2500.0f);

        insertPersona(personaDA, "Esther", "Malgin", "M",
                LocalDate.of(1983, 12, 6), 2100.0f);

        insertPersona(personaDA, "Amador", "Denador", "H",
                LocalDate.of(1994, 12, 24), 1200.0f);

        insertPersona(personaDA, "Aitor", "Tilla", "H",
                LocalDate.of(2001, 1, 7), 1300.0f);

        insertPersona(personaDA, "Sandra", "Matica", "M",
                LocalDate.of(1977, 2, 19), 1500.0f);

       /* insertPersona(personaDA, 'Victor', 'Nado', 'H', '1998-06-30', 2400);
        insertPersona(personaDA, 'Pedro', 'Gado', 'H', '2002-04-23', 1100);
        insertPersona(personaDA, 'Vanesa', 'Tanica', 'M', '2000-01-06', 1200);
        insertPersona(personaDA, 'Marta', 'Baco', 'M', '1970-08-03', 1700);
        */

        insertPersona(personaDA, "Consuelo", "Tería", "M",
                LocalDate.of(1992, 7, 8), 1900.0f);

        insertPersona(personaDA, "Mercedes", "Pacio", "M",
                LocalDate.of(1970, 8, 3), 2400.0f);

        insertPersona(personaDA, "Salvador", "Mido", "H",
                LocalDate.of(2003, 12, 23), 2500.0f);

        insertPersona(personaDA, "Lorenzo", "Penco", "H",
                LocalDate.of(1968, 3, 12), 1900.0f);

        insertPersona(personaDA, "Perica", "Palotes", "M",
                LocalDate.of(1958, 4, 6), 2100.0f);
    }

    static void insertPersona(PersonaDA personaDA, String nombre, String apellidos,
                              String sexo, LocalDate nacimiento, float ingresos) {

        System.out.println("Insertando persona: " + nombre + " " + apellidos);

        if (personaDA.insertPersona(nombre, apellidos, sexo, nacimiento, ingresos)) {
            System.out.println("Persona insertada con éxito");
        } else {
            System.out.println("No se ha podido insertar la persona");
        }
    }


    static void printPersonas(PersonaDA personaDA) {
        PersonasPrinter.printPersonas(
                personaDA.getAllPersonas());
    }

    static void testGetPersona(PersonaDA personaDA) {
        System.out.println("Obteniendo datos de la persona con ID = 1");
        PersonasPrinter.printPersonas(personaDA.getPersona(1));
    }

    /**
     * Para actualizar los datos de una persona, de no ser que los queramos cambiar
     * completamente, deberíamos recuperar primero los datos actuales
     * y asignar los nuevos valores en los campos que queremos cambiar y volver a dejar
     * los valores anteriores en las columnas que queremos que mantengan su valor actual
     */
    static void testUpdatePersona(PersonaDA personaDA) throws SQLException {
        System.out.println("Actualizando en nombre y sueldo de la persona...");

        ResultSet rs = personaDA.getPersona(1);

        rs.next();

        int personaId = rs.getInt(1);
        String oldNombre = rs.getString(2);
        String apellidos = rs.getString(3);
        String sexo = rs.getString(4);
        LocalDate nacimiento = rs.getDate(5).toLocalDate();
        float oldIngresos = rs.getFloat(6);

        //En este caso vamos a cambiar solamente el nombre y los ingresos
        // apellidos, sexo y nacimiento se quedarán con los valores actuales
        personaDA.updatePersona(personaId, "Jorge " + oldNombre, apellidos,
                sexo, nacimiento, oldIngresos + 500);

        rs.close();

        PersonasPrinter.printPersonas(personaDA.getPersona(personaId));
    }

    public static void printPersonasPorSexo(PersonaDA personaDA) {
        System.out.println("Imprimiendo Hombres....");
        PersonasPrinter.printPersonas(personaDA.getPersonasBySexo("H"));

        System.out.println("Imprimiendo Mujeres....");
        PersonasPrinter.printPersonas(personaDA.getPersonasBySexo("M"));
    }

    public static void printPersonasNacidasAfter(PersonaDA personaDA) {

        LocalDate date = LocalDate.of(2000, 1, 1);

        System.out.println("Imprimiendo personas nacidas despues de " + date);
        PersonasPrinter.printPersonas(
                personaDA.getPersonasNacidasAfter(date));
    }


    public static void testDeletePersonas(PersonaDA personaDA) {
        System.out.println("Eliminado personas...");
        deletePersona(personaDA, 1);
        deletePersona(personaDA, 2);
        deletePersona(personaDA, 3);
        deletePersona(personaDA, 4);

        PersonasPrinter.printPersonas(personaDA.getAllPersonas());
    }

    static void deletePersona(PersonaDA personaDA, int personaId) {
        System.out.println("Eliminando persona con ID = " + personaId);

        if (personaDA.deletePersona(personaId)) {
            System.out.println("Persona eliminada correctamente.");
        } else {
            System.out.println("No se ha podido eliminar la persona");
        }
    }

}
