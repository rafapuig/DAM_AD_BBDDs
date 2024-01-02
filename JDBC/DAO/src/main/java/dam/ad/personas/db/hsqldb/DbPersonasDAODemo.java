package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DatabaseSchema;

import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Personas;
import dam.ad.model.personas.Sexo;
import org.hsqldb.jdbc.JDBCDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DbPersonasDAODemo {

    public static void mainxxx(String[] args) {
        System.out.printf("%-2s %-20s %-30s %-4s %-10s %-8s\n", "ID", "NOMBRE", "APELLIDOS", "SEXO", "NACIMIENTO", "INGRESOS");
        System.out.printf("%2s %-20s %-30s %4s %-10s %8s", 1, "Rafa", "Puig", "H", "2000-01-01", 3450.0);

    }

    public static void main(String[] args) throws Exception {

        System.out.println("Creando el DataSource...");
        DataSource dataSource = createDataSource();

        System.out.println("Creando el esquema de la base de datos...");
        createSchema(dataSource);

        System.out.println("Creando el DAO de personas....");
        DAO<Persona> personaDAO = new DbPersonaDAO(dataSource); //, false);

        //DAO<Persona> personaDAO = new DbPersonaDAO(dataSource);

        System.out.println("Añadiendo personas...");
        addPersonas(personaDAO);

        //personaDAO.add(generateSamplePersonas());

        System.out.println("Recuperando todas las personas...");
        printPersonas(personaDAO);

        printMujeresIngresosSuperior1500(personaDAO);

        getPersonasSueldoSuperior2000GroupBySexo(personaDAO);

        //System.out.println("Recuperando todas las personas...");
        //personaDAO.getAll().forEach(System.out::println);

        getPersonaByIDTest(personaDAO);

        Persona persona = addNewPersona(personaDAO);

        updatePersona(personaDAO, persona);

        borrarHombres(personaDAO);

        borrarPersonas(personaDAO);

        incrementarIngresos(personaDAO);

        obtenerNacidosAntes2000(personaDAO);

        printPersonas(personaDAO.getAll());


        System.out.println("Borrando datos...");
        cleanUp(dataSource);

        System.out.println("Cerrando la base de datos...");
        shutdown(dataSource);   //Muy importante en HSQLDB

        System.out.println("Fin de la demo");
    }

    private static void getPersonasSueldoSuperior2000GroupBySexo(DAO<Persona> personaDAO) {
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
                    //.filter(persona -> persona.getIngresos() > 2000)
                    .forEach(DbPersonasDAODemo::printPersonaRow);
        });
    }


    static DataSource createDataSource() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:hsqldb:C:/BBDDs/hsqldb/personas12"); //create=true"); // ;shutdown=true");
        properties.setProperty("user", "SA");
        properties.setProperty("password", "");
        properties.setProperty("shutdown", "true");

        // No tiene Connection Pool !!!!
        return JDBCDataSourceFactory.createDataSource(properties);
    }

    static void createSchema(DataSource dataSource) throws SQLException {

        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            //System.out.println(databaseSchema.getCreateSchema());
            stmt.execute(databaseSchema.getCreateSchema());
            System.out.println("Base de datos generada.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    static void addPersonas(DAO<Persona> personaDAO) {
        Personas.generateSamplePersonas()
                .stream()
                .peek(persona -> System.out.println("Añadiendo a " + persona.getNombre() + " " + persona.getApellidos()))
                .forEach(personaDAO::add);
    }

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
        personaDAO.getAll().forEach(DbPersonasDAODemo::printPersonaRow);
        System.out.println();
    }

    static void printPersonas(List<Persona> personas) {
        printPersonasHeader();
        personas.forEach(DbPersonasDAODemo::printPersonaRow);
        System.out.println();
    }

    static void printPersonas(Stream<Persona> personas) {
        printPersonasHeader();
        personas.forEach(DbPersonasDAODemo::printPersonaRow);
        System.out.println();
    }

    static void cleanUp(DataSource dataSource) throws SQLException {

        DatabaseSchema databaseSchema = new PersonasDatabaseSchema();

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            //System.out.println(databaseSchema.getDropSchema());
            stmt.execute(databaseSchema.getDropSchema());
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    static void shutdown(DataSource dataSource) throws SQLException {

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            //System.out.println(databaseSchema.getDropSchema());
            stmt.execute("SHUTDOWN");
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
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

    private static void getPersonaByIDTest(DAO<Persona> personaDAO) {
        System.out.println("Recuperado persona con ID 11");
        Optional<Persona> personaById = personaDAO.getById(11);

        personaById.ifPresent(System.out::println);
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

        System.out.println("Actualizando persona " + persona.getNombre());
        personaDAO.update(persona);

        printPersonas(personaDAO);
    }

    private static void borrarHombres(DAO<Persona> personaDAO) {
        System.out.println("Filtrando personas, borrando hombres...");
        personaDAO.getAll()
                .filter(p -> p.getSexo() == Sexo.HOMBRE)
                .forEach(personaDAO::delete);

        printPersonas(personaDAO);
    }

    private static void borrarPersonas(DAO<Persona> personaDAO) {
        System.out.println("Borrando personas...1,2,7,10");
        personaDAO.getById(1).ifPresent(personaDAO::delete);
        personaDAO.getById(2).ifPresent(personaDAO::delete);
        personaDAO.getById(7).ifPresent(personaDAO::delete);
        personaDAO.getById(10).ifPresent(personaDAO::delete);

        printPersonas(personaDAO);
    }

    private static void incrementarIngresos(DAO<Persona> personaDAO) {
        System.out.println("Incrementando ingresos...");
        personaDAO.getAll()
                .peek(p -> p.setIngresos(p.getIngresos() * 1.05f))
                .forEach(personaDAO::update);

        printPersonas(personaDAO);
    }

    private static void obtenerNacidosAntes2000(DAO<Persona> personaDAO) {
        System.out.println("Obteniendo nacidas antes del 2000...");
        printPersonasHeader();
        personaDAO.getAll()
                .filter(p -> p.getNacimiento().getYear() < 2000)
                .forEach(DbPersonasDAODemo::printPersonaRow);
        System.out.println();
    }
}
