package dam.ad.jdbc.statements.personas.query.stream;

import dam.ad.jdbc.query.ResultSetStream;

import dam.ad.jdbc.statements.personas.SQLs;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAConsumer;
import dam.ad.jdbc.statements.personas.consumers.PersonaDAThrowingConsumers;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;


import java.sql.*;
import java.time.LocalDate;
import java.util.stream.Stream;

public class QueryStreamPersonaDATest {
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL);

            PersonaDAStream personaDA = new PersonaDAStream(connection);

            //PersonaDATest.createTablePersona(personaDA);
            //PersonaDATest.insertSamplePersonas(personaDA);

            //testGetPersonasAsStream(personaDA);
            testGetPersonasAsStreamTry(personaDA);

            //testGetPersonasAsStreamBySexo(personaDA);
            //testGetPersonasAsStreamBySexo2(personaDA);

            //testGetPersonasAsStream(personaDA);

            //testGetPersonasBy2(personaDA);

            //PersonaDATest.testDeletePersonas(personaDA);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testGetPersonasAsStream(PersonaDAStream personaDA) {

        personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS)
                .filter(persona -> persona.getSexo() == Sexo.MUJER)
                .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                //.sorted(Comparator.comparing(Persona::getIngresos))
                .forEach(System.out::println);
    }

    /**
     * Si vamos a obtener un Stream que mantiene recursos que hay que cerrar
     * deberiamos usar un bloque try o un try-with-resources
     * Aqui vemos un try
     */
    static void testGetPersonasAsStreamTry(PersonaDAStream personaDA) {
        Stream<Persona> personas = null;
        try {
            personas = personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS);
            personas
                    .filter(persona -> persona.getSexo() == Sexo.MUJER)
                    .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                    .forEach(System.out::println);
        } finally {
            personas.close();
        }
    }

    /**
     * Si vamos a obtener un Stream que mantiene recursos que hay que cerrar
     * deberíamos usar un bloque try o un try-with-resources
     * Aquí vemos un try-with-resources
     */
    static void testGetPersonasAsStreamTryWithResources(PersonaDAStream personaDA) {

        //Hacemos uso de un try-with-resources para obtener el stream
        try (Stream<Persona> personas = personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS)) {
            personas
                    .filter(persona -> persona.getSexo() == Sexo.MUJER)
                    .filter(persona -> persona.getNacimiento().isAfter(LocalDate.of(1970, 1, 1)))
                    //.sorted(Comparator.comparing(Persona::getIngresos))
                    .forEach(System.out::println);
        } // LLamada automática al close
    }

    static void testGetPersonasAsStreamBySexo(PersonaDAStream personaDA) {

        Stream<Persona> personas =
                personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAThrowingConsumers.sexoHombreParamSetterNoEx::accept);

        personas.forEach(System.out::println);  //Este foreach es de la clase ResultStream y cierra el resultset


    }

    static void testGetPersonasAsStreamBySexo1(PersonaDAStream personaDA) {

        Stream<Persona> personas =
                personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDAConsumer.sexoHombreParamSetter::accept);

        personas.forEach(System.out::println);  //Este foreach es de la clase ResultStream y cierra el resultset

        System.out.println("Llamando a personas.close()...");
        personas.close();
    }

    static void testGetPersonasAsStreamBySexo2(PersonaDAStream personaDA) {

        ResultSetStream<Persona> personas = (ResultSetStream<Persona>) personaDA.getPersonasAsStream(SQLs.SELECT_ALL_PERSONAS);
        personas
                .filter(persona -> persona.getSexo() == Sexo.HOMBRE)
                .skip(2)
                .limit(3)
                .forEach(System.out::println);  //Este foreach ya no es de ResultSetStream y no cierra

        personas.close();
    }

    static void testGetPersonasAsStream1(PersonaDAStream personaDA) {
        /*Stream<Persona> personas =
                personaDA.getPersonasBy(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDA.personasHombres);

        personas.forEach(System.out::println);

        personas.close();*/

        try (Stream<Persona> personas = personaDA.getPersonasAsStream(
                SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                stmt -> stmt.setDate(1, Date.valueOf(
                        LocalDate.of(1960, 1, 1))))) {
            personas.forEach(System.out::println);
        }
    }

    static void testGetPersonasBy2(PersonaDAStream personaDA) {
        /*Stream<Persona> personas =
                personaDA.getPersonasBy(
                        SQLs.SELECT_PERSONAS_BY_SEXO,
                        PersonaDA.personasHombres);

        personas.forEach(System.out::println);

        personas.close();*/

        personaDA.getPersonasAsStream(
                        SQLs.SELECT_PERSONAS_BY_NACIMIENTO,
                        stmt -> stmt.setDate(1, Date.valueOf(
                                LocalDate.of(1900, 1, 1))))
                //.filter(persona -> persona.getIngresos()>3000)
                //.limit(2)
                .forEach(System.out::println);

    }


}
