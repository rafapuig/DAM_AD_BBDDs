package dam.ad.personas.db.hsqldb;

import dam.ad.dao.jdbc.DAOManager;
import dam.ad.dao.jdbc.BasicDTOMapper;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.PersonasPrinter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * El DAOManager es capaz de realizar consultas SQL
 */
public class PersonasDAOManagerTester {
    public static void testQueryHombres(DAOManager manager) {
        Stream<Persona> hombres = manager.query(
                "SELECT * FROM persona WHERE sexo = ?",
                PersonaDTOMapper.getInstance(),
                "H");

        PersonasPrinter.TO_CONSOLE.printPersonas(hombres);
    }

    public static void testQueryHombresRicos(DAOManager manager) {
        Stream<Persona> hombresRicos = manager.query(
                "SELECT * FROM persona WHERE sexo = ? AND ingresos > ?",
                manager.getDTOMapper(Persona.class),
                "H", 2000);

        PersonasPrinter.TO_CONSOLE.printPersonas(hombresRicos);
    }

    public static void testQueryMujeresAntesDe2000(DAOManager manager) {
        Stream<Persona> mujeres = manager.query(
                "SELECT * FROM persona WHERE sexo = ? AND nacimiento < ?",
                Persona.class,
                "M", LocalDate.of(2000,1,1));

        PersonasPrinter.TO_CONSOLE.printPersonas(mujeres);
    }

    public static void testQueryNombres(DAOManager manager) {
        Stream<String> nombres = manager.query(
                "SELECT nombre FROM persona WHERE sexo = ? AND nacimiento < ?",
                resultSet -> resultSet.getString(1), //Un DTOMapper a partir de una lambda
                "M", LocalDate.of(2000, 1, 1));

        nombres.forEach(System.out::println);
    }

    public static void testQuerySueldoMedio(DAOManager manager) {
        Stream<Float> media = manager.query(
                "SELECT AVG(ingresos) FROM persona",
                resultSet -> resultSet.getFloat(1)
        );

        Optional<Float> sueldoMedio = media.findFirst();

        sueldoMedio.ifPresent(valor -> System.out.println("El sueldo medio es: " + valor));
    }

    public static void testQueryNombresCompletos(DAOManager manager) {
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



    public static void testQueryGenericMapper(DAOManager manager) {
        Stream<List<Object>> list = manager.query(
                "SELECT * FROM persona",
                new BasicDTOMapper());

        list.forEach(System.out::println);
    }

    public static void testQueryProyeccion(DAOManager manager) {
        Stream<?> list = manager.query(
                "SELECT nombre, apellidos, YEAR(nacimiento) FROM persona WHERE nombre LIKE ?",
                "A%");

        list.forEach(System.out::println);
    }

    public static void testQuerySueldoMedioPorSexos(DAOManager manager) {
        Stream<?> list = manager.query(
                "SELECT sexo, AVG(ingresos) FROM persona GROUP BY sexo");

        list.forEach(System.out::println);
    }

    public static void testQueryScalarNumeroHombres(DAOManager manager) {
        Optional<Long> numHombres = manager.queryScalar(
                "SELECT COUNT(*) FROM persona WHERE sexo='H'",
                Long.class);

        System.out.print("Número de hombres: ");
        numHombres.ifPresent(System.out::println);
    }
}
