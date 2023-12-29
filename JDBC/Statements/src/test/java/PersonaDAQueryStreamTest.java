import dam.ad.jdbc.query.ResultSetStream;
import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAConsumer;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAThrowingConsumers;
import dam.ad.jdbc.statements.personas.query.stream.PersonaDAStream;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

public class PersonaDAQueryStreamTest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";
    static Connection connection;
    static PersonaDAStream personaDA;

    @BeforeAll
    static void prepareTest() throws SQLException {
        connection = DriverManager.getConnection(URL);
        personaDA = new PersonaDAStream(connection);
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void testGetPersonasAsStream() {

        personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS)
                .filter(persona -> persona.getSexo() == Sexo.MUJER)
                .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                //.sorted(Comparator.comparing(Persona::getIngresos))
                .forEach(System.out::println);
    }

    /**
     * Si vamos a obtener un Stream que mantiene recursos que hay que cerrar
     * deberíamos usar un bloque try o un try-with-resources
     * Aqui vemos un try
     */
    @Test
    void testGetPersonasAsStreamTry() {
        Stream<Persona> personas = null;
        try {
            personas = personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS);
            personas
                    .filter(persona -> persona.getSexo() == Sexo.MUJER)
                    .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                    .forEach(System.out::println);
        } finally {
            assert personas != null;
            personas.close();
        }
    }

    /**
     * Si vamos a obtener un Stream que mantiene recursos que hay que cerrar
     * deberíamos usar un bloque try o un try-with-resources
     * Aquí vemos un try-with-resources
     */
    @Test
    void testGetPersonasAsStreamTryWithResources() {

        //Hacemos uso de un try-with-resources para obtener el stream
        try (Stream<Persona> personas = personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS)) {
            personas
                    .filter(persona -> persona.getSexo() == Sexo.MUJER)
                    .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                    //.sorted(Comparator.comparing(Persona::getIngresos))
                    .forEach(System.out::println);
        } // LLamada automática al close
    }

    @Test
    void testGetPersonasAsStreamBySexo() {

        Stream<Persona> personas =
                personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAThrowingConsumers.sexoHombreParamSetterNoEx::accept);

        personas.forEach(System.out::println);  //Este foreach es de la clase ResultStream y cierra el resultset
    }

    @Test
    void testGetPersonasAsStreamBySexo1() {

        Stream<Persona> personas =
                personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumer.sexoHombreParamSetter::accept);

        personas.forEach(System.out::println);  //Este foreach es de la clase ResultStream y cierra el resultset

        System.out.println("Llamando a personas.close()...");
        personas.close();
    }

    @Test
    void testGetPersonasAsStreamBySexo2() {

        ResultSetStream<Persona> personas = (ResultSetStream<Persona>) personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS);
        personas
                .filter(persona -> persona.getSexo() == Sexo.HOMBRE)
                .skip(2)
                .limit(3)
                .forEach(System.out::println);  //Este foreach ya no es de ResultSetStream y no cierra

        personas.close();
    }

    @Test
    void testGetPersonasAsStream1() {

        try (Stream<Persona> personas = personaDA.getPersonasAsStream(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                stmt -> stmt.setDate(1, Date.valueOf(
                        LocalDate.of(1960, 1, 1))))) {
            personas.forEach(System.out::println);
        }
    }

    @Test
    void testGetPersonasAsStream2() {

        personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                        stmt -> stmt.setDate(1, Date.valueOf(
                                LocalDate.of(1900, 1, 1))))
                //.filter(persona -> persona.getIngresos()>3000)
                //.limit(2)
                .forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    void testGetPersonasNombreStartsWith(String starts) {
        personaDA.getPersonasAsStream(
                        "SELECT * FROM persona WHERE nombre LIKE ?",
                        stmt -> stmt.setString(1, starts + "%"))
                .forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(floats = {1000, 2000})
    void testGetPersonasSalarioSupera(float salario) {
        System.out.println("Personas con ingresos superiores a " + salario + "...");
        personaDA.getPersonasAsStream(
                        "SELECT * FROM persona WHERE ingresos > ?",
                        stmt -> stmt.setFloat(1, salario))
                .sorted(Comparator.comparing(Persona::getIngresos))
                .forEach(System.out::println);
    }

}
