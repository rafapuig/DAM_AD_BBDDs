package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.model.personas.Persona;

public class DbPersonaDAODemo3 {

    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas30;shutdown=true";
    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAOManager(URL);

        System.out.println("Borrando datos...");
        manager.dropSchema();

        System.out.println("Creando el esquema de la base de datos...");
        manager.generateSchema();

        System.out.println("Creando el DAO de personas....");
        DAO<Persona> personaDAO = manager.createDAO(Persona.class);

        PersonaDAOTester tester = new PersonaDAOTester(personaDAO);

        tester.addSamplePersonas();
        System.out.println("Recuperando todas las personas...");
        tester.printPersonas();

        tester.getPersonaByIDTest(11);
        tester.getPersonaByIDTest(2);

        Persona persona = tester.addNewPersona();

        tester.updatePersona(persona);

        tester.obtenerNacidosAntes2000();

        tester.incrementarIngresos();

        tester.borrarPersonas();

        tester.borrarHombres();


        System.out.println("Cerrando la base de datos...");
        manager.shutdown();   //Muy importante en HSQLDB

        personaDAO.close();

        System.out.println("Fin de la demo");
    }

}
