import dam.ad.jdbc.statements.personas.PersonasPrinter;
import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAConsumer;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAThrowingConsumers;
import dam.ad.jdbc.stream.ThrowingConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class PersonaDAThrowingConsumersTest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";
    static Connection connection;
    static PersonaDAThrowingConsumers personaDA;

    @BeforeAll
    static void prepareTest() throws SQLException {
        connection = DriverManager.getConnection(URL);
        personaDA = new PersonaDAThrowingConsumers(connection);
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testGetPersonasByParamSetter() {

        System.out.println("Imprimiendo Hombres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAThrowingConsumers.sexoHombreParamSetterNoEx));

        System.out.println("Imprimiendo Mujeres....");
        PersonasPrinter.printPersonas(
                personaDA.getPersonas(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAThrowingConsumers.sexoMujerParamSetterNoExLegacy));
    }


    @ParameterizedTest
    @ValueSource(strings = {"H", "M"})
    void testGetPersonasBySexo(String sexo) {
        String etiqueta = switch (sexo) {
            case "H" -> "Hombres";
            case "M" -> "Mujeres";
            default -> throw new IllegalStateException("Unexpected value: " + sexo);
        };

        System.out.println("Imprimiendo " + etiqueta + "...");

        PersonasPrinter.printPersonas(personaDA.getPersonasBySexo(sexo));
    }


    static Stream<LocalDate> testGetPersonasNacidasAfter() {
        return Stream.of(LocalDate.of(1970, 1, 1),
                LocalDate.of(2000, 1, 1));
    }

    @ParameterizedTest
    @MethodSource
    void testGetPersonasNacidasAfter(LocalDate date) {

        System.out.println("Imprimiendo personas nacidas después de " + date);
        PersonasPrinter.printPersonas(
                personaDA.getPersonasNacidasAfter(date));
    }

    @Test
    void testQueryPersonas() {

        LocalDate date = LocalDate.of(1970, 1, 1);

        ThrowingConsumer<PreparedStatement,SQLException> paramSetter =
                stmt -> stmt.setDate(1, Date.valueOf(date));

        Consumer<ResultSet> printResults = PersonasPrinter::printPersonas;

        System.out.println("Imprimiendo personas nacidas después de " + date);

        personaDA.queryPersonas(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                paramSetter,
                printResults);
    }

    @Test
    void testConsumeResultSet() {
        System.out.println("Imprimiendo todas las personas...");

        new PersonasPrinter().accept(personaDA.getAllPersonas());
    }

    String getEtiquetaSexo(String sexo) {
        return switch (sexo) {
            case "H" -> "Hombres";
            case "M" -> "Mujeres";
            default -> throw new IllegalStateException("Unexpected value: " + sexo);
        };
    }

    @ParameterizedTest
    @ValueSource(strings = {"H","M"})
    void testQueryPersonasBySexo(String sexo) {

        String etiqueta = getEtiquetaSexo(sexo);

        System.out.println("Imprimiendo " + etiqueta + "...");

        //PersonasPrinter.printPersonas(personaDA.getPersonasBySexo(sexo));
        personaDA.queryPersonasBySexo(sexo);
        personaDA.queryPersonasBySexo(sexo, new PersonasPrinter());
    }

    @ParameterizedTest
    @MethodSource("testGetPersonasNacidasAfter")
    void testQueryPersonasNacidasAfter(LocalDate date) {
        System.out.println("Imprimiendo personas nacidas después de " + date);

        //PersonasPrinter.printPersonas(personaDA.getPersonasNacidasAfter(date));
        personaDA.queryPersonasNacidasAfter(date);
    }

}
