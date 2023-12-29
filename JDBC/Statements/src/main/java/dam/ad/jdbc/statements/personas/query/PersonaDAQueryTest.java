package dam.ad.jdbc.statements.personas.query;

import dam.ad.jdbc.statements.personas.PersonaDATest;

import java.sql.*;

public class PersonaDAQueryTest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL);

            PersonaDAQuery personaDA = new PersonaDAQuery(connection);

            PersonaDATest.createTablePersona(personaDA);
            PersonaDATest.testInsertPersonas(personaDA);

            testQueryPersonasBySexo(personaDA);

            PersonaDATest.testDeletePersonas(personaDA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static void testQueryPersonasBySexo(PersonaDAQuery da) {
        System.out.println("Imprimiendo Hombres....");
        da.queryPersonasBySexo("H");

        System.out.println("Imprimiendo Mujeres....");
        da.queryPersonasBySexo("M");
    }


}
