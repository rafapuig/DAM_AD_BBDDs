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

            PersonaDAConsumer personaDA = new PersonaDAConsumer(connection);

            PersonaDATest.createTablePersona(personaDA);
            PersonaDATest.testInsertPersonas(personaDA);

            testGetPersonasByParamSetter(personaDA);

            PersonaDATest.printPersonasPorSexo(personaDA);
            PersonaDATest.printPersonasNacidasAfter(personaDA);

            testConsumeResultSet(personaDA);
            testQueryPersonas(personaDA);


            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static void testGetPersonasByParamSetter(PersonaDAConsumer personaDA) {

        System.out.println("Imprimiendo Hombres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumer.sexoHombreParamSetter));

        System.out.println("Imprimiendo Mujeres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumer.sexoMujerParamSetter));
    }

    static void testQueryPersonas(PersonaDAConsumer personaDA) {

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

    static void testConsumeResultSet(PersonaDAConsumer personaDA) {
        System.out.println("Imprimiendo todas las personas...");

        new PersonasPrinter().accept(personaDA.getAllPersonas());

    }


}
