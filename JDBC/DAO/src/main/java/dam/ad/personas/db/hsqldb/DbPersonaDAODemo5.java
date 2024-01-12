package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.model.personas.Persona;
import static dam.ad.personas.db.hsqldb.PersonasDAOManagerTester.*;

public class DbPersonaDAODemo5 {

    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas50"; //shutdown=true";
    public static void main(String[] args) throws Exception {

        PersonasDAOManager manager = new PersonasDAOManager(URL);

        testWithManager(manager);
    }

    public static void testWithManager(PersonasDAOManager manager) throws Exception {
        try {
            System.out.println("Borrando datos...");
            manager.dropSchema();

            System.out.println("Creando el esquema de la base de datos...");
            manager.generateSchema();

            System.out.println("Creando el DAO de personas....");
            DAO<Persona> personaDAO = manager.createDAO(Persona.class);

            System.out.println("Creando el Tester del DAO de personas....");
            PersonaDAOTester daoTester = new PersonaDAOTester(personaDAO);

            manager.beginTransaction();

            daoTester.addSamplePersonas();

            testQueryHombres(manager);

            testQueryHombresRicos(manager);

            testQueryMujeresAntesDe2000(manager);

            testQueryNombres(manager);

            testQueryNombresCompletos(manager);

            testQuerySueldoMedio(manager);

            testQueryGenericMapper(manager);
            testQueryProyeccion(manager);
            testQuerySueldoMedioPorSexos(manager);

            testQueryScalarNumeroHombres(manager);

            manager.commit();

            personaDAO.close();
            manager.close();

        } finally {
            System.out.println("Cerrando la base de datos...");
            manager.shutdown();   //Muy importante en HSQLDB

            System.out.println("Fin de la demo");
        }

    }

}
