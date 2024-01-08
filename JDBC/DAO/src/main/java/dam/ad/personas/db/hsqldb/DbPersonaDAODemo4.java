package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.PersonasPrinter;

import java.util.stream.Stream;

public class DbPersonaDAODemo4 {
    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas40"; //shutdown=true";
    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAOManager(URL);

        try {

            System.out.println("Borrando datos...");
            manager.dropSchema();

            System.out.println("Creando el esquema de la base de datos...");
            manager.generateSchema();

            System.out.println("Creando el DAO de personas....");
            DAO<Persona> personaDAO = manager.createDAO(Persona.class);

            System.out.println("Creando el Tester del DAO de personas....");
            PersonaDAOTester tester = new PersonaDAOTester(personaDAO);

            /////////manager.beginTransaction();
            //manager.beginTransaction(); provocaria excepcion

            tester.addSamplePersonas();

            manager.beginTransaction();

            System.out.println("Recuperando todas las personas...");
            tester.printPersonas();

            tester.getPersonaByIDTest(11);
            tester.getPersonaByIDTest(2);

            //Persona persona = tester.addNewPersona();

            //tester.updatePersona(persona);

            tester.obtenerNacidosAntes2000();

            //tester.incrementarIngresos();

            //tester.borrarPersonas();

            //tester.borrarHombres();

            manager.commit();

            tester.obtenerNacidosAntes2000();

            Stream<Persona> hombres = manager.query(
                    "SELECT * FROM persona WHERE sexo = ?",
                    PersonaDTOMapper.getInstance(),
                    "H");

            PersonasPrinter.TO_CONSOLE.printPersonas(hombres);

            //manager.commit(); //provoca excepcion

            personaDAO.close();
            manager.close();

        } finally {
            System.out.println("Cerrando la base de datos...");
            manager.shutdown();   //Muy importante en HSQLDB

            System.out.println("Fin de la demo");
        }

    }

}
