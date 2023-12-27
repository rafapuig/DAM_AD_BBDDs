package dam.ad.jdbc.statements.personas.consumers;

import dam.ad.jdbc.statements.personas.PersonaDATest;
import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class PersonaDAConsumersTest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL);

            PersonaDAConsumers personaDA = new PersonaDAConsumers(connection);

            PersonaDATest.createTablePersona(personaDA);
            PersonaDATest.insertSamplePersonas(personaDA);

            testGetPersonasByParamSetter(personaDA);

            PersonaDATest.printPersonasPorSexo(personaDA);
            PersonaDATest.printPersonasNacidas(personaDA);

            testConsumeResultSet(personaDA);
            testQueryPersonas(personaDA);


            PersonaDATest.testDeletePersonas(personaDA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void testConsumeResultSet(PersonaDAConsumers personaDA) {
        System.out.println("Imprimiendo todas las personas...");

        new PersonasPrinter().accept(personaDA.getAllPersonas());

    }


    static void testGetPersonasByParamSetter(PersonaDAConsumers personaDA) {

        System.out.println("Imprimiendo Hombres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumers.sexoHombreParamSetter));

        System.out.println("Imprimiendo Mujeres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumers.sexoMujerParamSetter));
    }

    static void testQueryPersonas(PersonaDAConsumers personaDA) {

        LocalDate date = LocalDate.of(1970, 1, 1);

        Consumer<PreparedStatement> paramSetter = stmt -> {
            try {
                stmt.setDate(1, Date.valueOf(date));
            } catch (SQLException e) {
                throw new RuntimeException("ERROR al asignar el parámetro nacimiento", e);
            }
        };

        Consumer<ResultSet> printResults = PersonasPrinter::printPersonas;

        System.out.println("Imprimiendo personas nacidas después de " + date);

        personaDA.queryPersonas(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                paramSetter,
                printResults);
    }



}
