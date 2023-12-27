package dam.ad.jdbc.statements.personas;

import java.sql.*;
import java.time.LocalDate;

public class PersonaDATest {

    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL);

            PersonaDA personaDA = new PersonaDA(connection);

            createTablePersona(personaDA);

            insertSamplePersonas(personaDA);

            printPersonas(personaDA);

            printPersonasPorSexo(personaDA);

            printPersonasNacidas(personaDA);

            testDeletePersonas(personaDA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTablePersona(PersonaDA personaDA) {
        System.out.println("Creando la tabla persona...");
        personaDA.createTablePersona();
    }

    public static void insertSamplePersonas(PersonaDA personaDA) {

        personaDA.insertPersona("Armando", "Bronca Segura", "H",
                LocalDate.of(2003, 12, 23), 2500.0f);

        personaDA.insertPersona("Consuelo", "Teria", "M",
                LocalDate.of(1958, 4, 6), 2100.0f);

        personaDA.insertPersona("Belen", "Tilla", "M",
                LocalDate.of(1978, 8, 14), 3600.0f);
    }

    static void printPersonas(PersonaDA personaDA) {
        PersonasPrinter.printPersonas(
                personaDA.getAllPersonas());
    }

    public static void printPersonasPorSexo(PersonaDA personaDA) {
        System.out.println("Imprimiendo Hombres....");
        PersonasPrinter.printPersonas(personaDA.getPersonasBySexo("H"));

        System.out.println("Imprimiendo Mujeres....");
        PersonasPrinter.printPersonas(personaDA.getPersonasBySexo("M"));
    }

    public static void printPersonasNacidas(PersonaDA personaDA) {

        LocalDate date = LocalDate.of(2000, 1, 1);

        System.out.println("Imprimiendo personas nacidas despues de " + date);
        PersonasPrinter.printPersonas(
                personaDA.getPersonasNacidasAfter(date));
    }


    public static void testDeletePersonas(PersonaDA personaDA) {
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
