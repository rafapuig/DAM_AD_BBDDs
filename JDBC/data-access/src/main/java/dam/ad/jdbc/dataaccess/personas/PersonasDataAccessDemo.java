package dam.ad.jdbc.dataaccess.personas;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Sexo;

import java.time.LocalDate;

public class PersonasDataAccessDemo {

    // URL de conexión a la base de datos futbol HSQLDB
    static final String HSQLDB_PERSONAS_URL =
            "jdbc:hsqldb:C:/BBDDs/hsqldb/personas";

    public static void main(String[] args) {
        try (PersonasDataAccess personasDA = new PersonasDataAccess(HSQLDB_PERSONAS_URL)) {

            //testGetAllPersonas(personasDA);

            //testFindPersona(personasDA);

            //testUpdatePersona(personasDA);

            //testDeletePersona(personasDA);

            testAddPersona(personasDA);

            //personasDA.commit();
        }
    }

    private static void testAddPersona(PersonasDataAccess personasDA) {

        personasDA.setAutoCommit(false);

        Persona persona = new Persona(0, "Rafa", "Puig", Sexo.HOMBRE,
                null,null);

        personasDA.addPersona(persona);

        Persona p2 = personasDA.getAllPersonas()
                .filter(p -> p.getNombre().equalsIgnoreCase("Rafa"))
                .filter(p-> p.getApellidos().equalsIgnoreCase("Puig"))
                .findFirst().orElse(null);

        System.out.println(p2);

        personasDA.getAllPersonas().forEach(System.out::println);

        personasDA.rollback();
        personasDA.setAutoCommit(true);
    }

    private static void testDeletePersona(PersonasDataAccess personasDA) {
        personasDA.setAutoCommit(false);

        Persona persona = personasDA.findPersona(1);

        System.out.println(persona);

        personasDA.deletePersona(persona);

        System.out.println(personasDA.findPersona(1));

        personasDA.rollback();
        personasDA.setAutoCommit(true);
    }

    private static void testUpdatePersona(PersonasDataAccess personasDA) {
        personasDA.setAutoCommit(false);
        Persona persona = personasDA.findPersona(1);
        System.out.println(persona);

        persona.setNombre("Juanito");
        persona.setApellidos("Escarcha");
        persona.setIngresos(persona.getIngresos() - 1000);
        personasDA.updatePersona(persona);

        Persona p2 = personasDA.findPersona(1);
        System.out.println(p2);
        personasDA.rollback();
        personasDA.setAutoCommit(true);
    }

    private static void testFindPersona(PersonasDataAccess personasDA) {
        Persona persona = personasDA.findPersona(1);
        System.out.println(persona);
    }

    static void testGetAllPersonas(PersonasDataAccess personasDA) {
        personasDA.getAllPersonas()
                .forEach(System.out::println);
    }

}
