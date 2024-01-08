package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.GenericDTOMapper;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.PersonasPrinter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DbPersonaDAODemo5 {

    static final String URL = "jdbc:hsqldb:file:C:/BBDDs/hsqldb/personas50"; //shutdown=true";
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

            manager.beginTransaction();

            tester.addSamplePersonas();

            testQueryHombres(manager);

            testQueryHombresRicos(manager);

            testQueryMujeresAntesDe2000(manager);

            testQueryNombres(manager);

            testQueryNombresCompletos(manager);

            testQuerySueldoMedio(manager);

            testQueryGenericMapper(manager);
            testQueryProyeccion(manager);
            testQuerySueldoMedioPorSexos(manager);

            manager.commit();

            personaDAO.close();
            manager.close();

        } finally {
            System.out.println("Cerrando la base de datos...");
            manager.shutdown();   //Muy importante en HSQLDB

            System.out.println("Fin de la demo");
        }

    }

    private static void testQueryHombres(PersonasDAOManager manager) {
        Stream<Persona> hombres = manager.query(
                "SELECT * FROM persona WHERE sexo = ?",
                PersonaDTOMapper.getInstance(),
                "H");

        PersonasPrinter.TO_CONSOLE.printPersonas(hombres);
    }

    private static void testQueryHombresRicos(PersonasDAOManager manager) {
        Stream<Persona> hombresRicos = manager.query(
                "SELECT * FROM persona WHERE sexo = ? AND ingresos > ?",
                manager.getDTOMapper(Persona.class),
                "H", 2000);

        PersonasPrinter.TO_CONSOLE.printPersonas(hombresRicos);
    }

    private static void testQueryMujeresAntesDe2000(PersonasDAOManager manager) {
        Stream<Persona> mujeres = manager.query(
                "SELECT * FROM persona WHERE sexo = ? AND nacimiento < ?",
                Persona.class,
                "M", LocalDate.of(2000,1,1));

        PersonasPrinter.TO_CONSOLE.printPersonas(mujeres);
    }

    private static void testQueryNombres(PersonasDAOManager manager) {
        Stream<String> nombres = manager.query(
                "SELECT nombre FROM persona WHERE sexo = ? AND nacimiento < ?",
                resultSet -> resultSet.getString(1),
                "M", LocalDate.of(2000, 1, 1));

        nombres.forEach(System.out::println);
    }

    private static void testQueryNombresCompletos(PersonasDAOManager manager) {
        record NombreCompletoDTO(String nombre, String apellidos) {}

        Stream<NombreCompletoDTO> nombresCompletos = manager.query(
                "SELECT nombre, apellidos FROM persona WHERE sexo = ? AND nacimiento < ?",
                resultSet -> new NombreCompletoDTO(
                        resultSet.getString(1),
                        resultSet.getString(2)
                ),
                "M", LocalDate.of(2000, 1, 1));

        nombresCompletos.forEach(System.out::println);
    }

    private static void testQuerySueldoMedio(PersonasDAOManager manager) {
        Stream<Float> media = manager.query(
                "SELECT AVG(ingresos) FROM persona",
                resultSet -> resultSet.getFloat(1)
        );

        Optional<Float> sueldoMedio = media.findFirst();

        sueldoMedio.ifPresent(valor -> System.out.println("El sueldo medio es: " + valor));
    }

    private static void testQueryGenericMapper(PersonasDAOManager manager) {
        Stream<List<Object>> list = manager.query(
                "SELECT * FROM persona",
                new GenericDTOMapper());

        list.forEach(System.out::println);
    }

    private static void testQueryProyeccion(PersonasDAOManager manager) {
        Stream<?> list = manager.query(
                "SELECT nombre, apellidos, YEAR(nacimiento) FROM persona WHERE nombre LIKE ?",
                "A%");

        list.forEach(System.out::println);
    }

    private static void testQuerySueldoMedioPorSexos(PersonasDAOManager manager) {
        Stream<?> list = manager.query(
                "SELECT sexo, AVG(ingresos) FROM persona GROUP BY sexo");

        list.forEach(System.out::println);
    }

}
