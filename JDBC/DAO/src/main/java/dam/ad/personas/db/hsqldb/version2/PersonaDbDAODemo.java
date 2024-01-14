package dam.ad.personas.db.hsqldb.version2;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DatabaseSchema;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Personas;
import dam.ad.model.personas.Sexo;
import dam.ad.personas.db.hsqldb.PersonasDatabaseSchema;
import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonaDbDAODemo {

    /**
     * Si añadimos la property shutdown=true y ejecutamos la demo veremos como se ralentiza
     * la ejecución del programa debido si añadimos esta propiedad a la configuración de
     * la conexión, entonces cuando se cierra la última conexión abierta con la base de datos
     * se apaga (shutdown) el servicio de base de datos.
     * Por eso, cada vez que se ejecuta un comando con su propia conexión además se está iniciando
     * y después cerrando el propio motor de la base de datos
     */
    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas10;shutdown=true";

    public static void main(String[] args) throws Exception {

        System.out.println("Creando el DataSource...");
        DataSource dataSource = createDataSource();

        System.out.println("Borrando datos...");
        cleanUp(dataSource);

        System.out.println("Creando el esquema de la base de datos...");
        generateSchema(dataSource);

        System.out.println("Creando el DAO de personas....");
        DAO<Persona> personaDAO = new PersonaDbDAO(dataSource); //, false);

        System.out.println("Añadiendo personas...");
        addSamplePersonas(personaDAO);

        System.out.println("Recuperando todas las personas...");
        printPersonas(personaDAO);

        System.out.println("Recuperando todas las personas...");
        personaDAO.getAll().forEach(System.out::println);

        printMujeresIngresosSuperior1500(personaDAO);

        System.out.println("Recuperando personas con sueldo superior a 2000 euros por sexo...");
        printPersonasSueldoSuperior2000GroupBySexo(personaDAO);

        printPersonaByIDTest(personaDAO);

        Persona persona = addNewPersona(personaDAO);

        updatePersona(personaDAO, persona);

        printNacidosAntes2000(personaDAO);

        borrarHombres(personaDAO);

        borrarPersonas(personaDAO);

        incrementarIngresos(personaDAO);

        printPersonas(personaDAO.getAll());

        System.out.println("Cerrando la base de datos...");
        shutdown(dataSource);   //Muy importante en HSQLDB

        System.out.println("Fin de la demo");
    }

    static DataSource createDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", URL); //create=true"); // ;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        properties.setProperty("shutdown", "true"); //No parece hacerle caso

        // Este DataSourceFactory no tiene Connection Pool !!!!
        return JDBCDataSourceFactory.createDataSource(properties);
    }

    static void generateSchema(DataSource dataSource) {

        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(databaseSchema.getCreateSchema());

            System.out.println("Base de datos generada.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    static void cleanUp(DataSource dataSource) {

        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(databaseSchema.getDropSchema());
            System.out.println("Base de datos eliminada.");

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    static void shutdown(DataSource dataSource) {

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute("SHUTDOWN");
            System.out.println("Motor de la base de datos apagado.");

        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    //************** PRINT PERSONA ****************************************

    static void printPersonaRow(Persona persona) {
        System.out.printf("%2s %-20s %-30s %4s %-10s %9s\n",
                persona.getPersonaId(),
                persona.getNombre(),
                persona.getApellidos(),
                persona.getSexo().getInicial(),
                persona.getNacimiento().toString(),
                NumberFormat.getNumberInstance().format(persona.getIngresos()));
    }

    static void printPersonasHeader() {
        System.out.printf("%-2s %-20s %-30s %-4s %-10s %9s\n", "ID", "NOMBRE", "APELLIDOS", "SEXO", "NACIMIENTO", "INGRESOS");
        System.out.println("--------------------------------------------------------------------------------");
    }


    static void printPersonas(DAO<Persona> personaDAO) {
        printPersonasHeader();
        personaDAO.getAll().forEach(PersonaDbDAODemo::printPersonaRow);
        System.out.println();
    }

    static void printPersonas(List<Persona> personas) {
        printPersonasHeader();
        personas.forEach(PersonaDbDAODemo::printPersonaRow);
        System.out.println();
    }

    static void printPersonas(Stream<Persona> personas) {
        printPersonasHeader();
        personas.forEach(PersonaDbDAODemo::printPersonaRow);
        System.out.println();
    }


    //*************** TESTS ***********************************************
    static void addSamplePersonas(DAO<Persona> personaDAO) {
        Personas.generateSamplePersonas()
                .stream()
                .peek(persona ->
                        System.out.println("Añadiendo a " +
                                           persona.getNombre() + " " +
                                           persona.getApellidos() + "..."))
                .forEach(personaDAO::add);
    }


    private static void printPersonasSueldoSuperior2000GroupBySexo(DAO<Persona> personaDAO) {
        var map = personaDAO.getAll()
                .collect(Collectors.groupingBy(
                        Persona::getSexo,
                        Collectors.filtering(
                                p-> p.getIngresos() > 2000,
                                Collectors.toList())));

        map.forEach((sexo, personas) -> {
            System.out.println(sexo);
            printPersonasHeader();
            personas.stream()
                    //.filter(persona -> persona.getIngresos() > 2000) ya no es necesario aquí
                    .forEach(PersonaDbDAODemo::printPersonaRow);
        });
    }


    private static Stream<Persona> getMujeresIngresosSuperior1500(DAO<Persona> personaDAO) {
        return personaDAO.getAll()
                .filter(persona -> persona.getIngresos() > 1500)
                .filter(persona -> persona.getSexo().equals(Sexo.MUJER));
    }

    private static void printMujeresIngresosSuperior1500(DAO<Persona> personaDAO) {
        System.out.println("Obtener mujeres con ingresos superiores a 1500");
        printPersonas(getMujeresIngresosSuperior1500(personaDAO));
    }

    private static void printPersonaByIDTest(DAO<Persona> personaDAO) {
        System.out.println("Recuperado persona con ID 11");
        Optional<Persona> personaById = personaDAO.getById(11);

        personaById.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No existe una persona con ese ID")
        );
    }

    private static Persona addNewPersona(DAO<Persona> personaDAO) {

        Persona persona = new Persona(0, "Perico", "Palotes",
                Sexo.HOMBRE, LocalDate.parse("1977-02-10"), 2300.0f);

        System.out.println("Añadiendo persona " + persona.getNombre());

        personaDAO.add(persona);

        printPersonas(personaDAO);

        return persona;
    }

    private static void updatePersona(DAO<Persona> personaDAO, Persona persona) {
        //Persona modificada = persona.with().apellidos("XXX").ingresos(4000).build();
        persona.setNombre("Pepita");
        persona.setApellidos("Grillo");
        persona.setSexo(Sexo.MUJER);
        persona.setIngresos(2600.0f);

        System.out.println("Actualizando persona " +
                           persona.getNombre() + " " + persona.getApellidos() + "...");

        personaDAO.update(persona);

        printPersonas(personaDAO);
    }

    private static void borrarHombres(DAO<Persona> personaDAO) {
        System.out.println("Filtrando personas, borrando hombres...");
        personaDAO.getAll()
                .filter(p -> p.getSexo() == Sexo.HOMBRE)
                .peek(p -> System.out.println("Borrando a " +
                                              p.getNombre() + " " + p.getApellidos() + "..."))
                .forEach(personaDAO::delete);

        System.out.println("Resultado del borrado todos los hombres:");
        printPersonas(personaDAO);
    }

    private static void borrarPersonas(DAO<Persona> personaDAO) {
        System.out.println("Borrando personas...1,2,7,10");
        personaDAO.getById(1).ifPresent(personaDAO::delete);
        personaDAO.getById(2).ifPresent(personaDAO::delete);
        personaDAO.getById(7).ifPresent(personaDAO::delete);
        personaDAO.getById(10).ifPresent(personaDAO::delete);

        System.out.println("Resultado del borrado:");
        printPersonas(personaDAO);
    }

    private static void incrementarIngresos(DAO<Persona> personaDAO) {
        System.out.println("Incrementando ingresos...");
        personaDAO.getAll()
                .peek(p -> p.setIngresos(p.getIngresos() * 1.05f))
                .peek(p-> System.out.println("Incrementando ingresos de " +
                                             p.getNombre() + " " + p.getApellidos() +
                                             "a " + p.getIngresos() + "€"))
                .forEach(personaDAO::update);

        printPersonas(personaDAO);
    }

    private static void printNacidosAntes2000(DAO<Persona> personaDAO) {
        System.out.println("Obteniendo personas nacidas antes del 2000...");

        printPersonasHeader();
        personaDAO.getAll()
                .filter(p -> p.getNacimiento().getYear() < 2000)
                .forEach(PersonaDbDAODemo::printPersonaRow);

        System.out.println();
    }
}
