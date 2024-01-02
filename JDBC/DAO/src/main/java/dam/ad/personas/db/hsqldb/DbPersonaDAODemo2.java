package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.dao.jdbc.DatabaseSchema;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Personas;
import dam.ad.model.personas.Sexo;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public class DbPersonaDAODemo2 {

    static final String URL = "jdbc:hsqldb:C:/BBDDs/hsqldb/personas20";

    public static void main(String[] args) throws Exception {

        System.out.println("Creando el DataSource...");
        DataSource dataSource = createDataSource();

        DatabaseSchema schema = new PersonasDatabaseSchema();

        System.out.println("Borrando datos...");
        cleanUp(dataSource, schema);

        System.out.println("Creando el esquema de la base de datos...");
        createSchema(dataSource, schema);

        System.out.println("Creando el DAO de personas....");
        DAO<Persona> personaDAO = new DbPersonaDAO(dataSource);

        System.out.println("Añadiendo personas...");
        addSamplePersonas(personaDAO);

        System.out.println("Recuperando todas las personas...");
        printPersonas(personaDAO);

        getPersonaByIDTest(personaDAO);

        Persona persona = addNewPersona(personaDAO);

        updatePersona(personaDAO, persona);

        obtenerNacidosAntes2000(personaDAO);

        borrarHombres(personaDAO);

        borrarPersonas(personaDAO);

        incrementarIngresos(personaDAO);

    }

    static DataSource createDataSource() throws Exception {
        return PersonasDataSourceFactory.createDataSource(URL);
    }

    static void createSchema(DataSource dataSource, DatabaseSchema dbSchema) {
        dbSchema.createSchema(dataSource);
    }

    static void cleanUp(DataSource dataSource, DatabaseSchema dbSchema) {
        dbSchema.dropSchema(dataSource);
    }

    static Consumer<Persona> printAddingInfo = persona ->
            System.out.println("Añadiendo a " +
                               persona.getNombre() + " " + persona.getApellidos());

    static void addSamplePersonas(DAO<Persona> personaDAO) {

        Personas.generateSamplePersonas()
                .stream()
                .peek(printAddingInfo)
                .forEach(personaDAO::add);
    }

    static void printPersonas(DAO<Persona> personaDAO) {
        printPersonasHeader();

        personaDAO.getAll()
                .map(Personas::getPersonaAsRow)
                .forEach(System.out::println);

        System.out.println();
    }

    private static void getPersonaByIDTest(DAO<Persona> personaDAO) {
        System.out.println("Recuperado persona con ID 11...");

        Optional<Persona> personaById = personaDAO.getById(11);

        printPersonasHeader();
        personaById
                .map(Personas::getPersonaAsRow)
                .ifPresent(System.out::println);

        personaById.ifPresent(System.out::println);

        System.out.println();
    }

    static Persona addNewPersona(DAO<Persona> personaDAO) {

        Persona persona = new Persona(0, "Perico", "Palotes",
                Sexo.HOMBRE, LocalDate.parse("1977-02-10"), 2300.0f);

        printAddingInfo.accept(persona);

        personaDAO.add(persona);

        printPersonas(personaDAO);
        return persona;
    }

    static Consumer<Persona> printOperationInfo(String prefix) {
        Consumer<Persona> printPrefix =
                persona -> System.out.print(prefix);

        return printPrefix.andThen(printInfo);
    }

    static Consumer<Persona> printInfo =
            persona -> System.out.println(
                    persona.getNombre() + " " +
                    persona.getApellidos());


    static void updatePersona(DAO<Persona> personaDAO, Persona persona) {
        //Persona modificada = persona.with().apellidos("XXX").ingresos(4000).build();

        Consumer<Persona> printUpdatingInfo =
                printOperationInfo("Actualizando persona ");

        Consumer<Persona> printUpdatedInfo =
                printOperationInfo("A persona ");

        printUpdatingInfo.accept(persona);

        persona.setNombre("Pepita");
        persona.setApellidos("Grillo");
        persona.setSexo(Sexo.MUJER);
        persona.setIngresos(2600.0f);

        printUpdatedInfo.accept(persona);

        personaDAO.update(persona);

        printPersonas(personaDAO);
    }

    static void borrarHombres(DAO<Persona> personaDAO) {
        System.out.println("Filtrando personas, borrando hombres...");
        personaDAO.getAll()
                .filter(p -> p.getSexo() == Sexo.HOMBRE)
                .forEach(personaDAO::delete);

        printPersonas(personaDAO);
    }

    static void borrarPersonas(DAO<Persona> personaDAO) {
        System.out.println("Borrando personas 1, 2, 7 y 10...");
        personaDAO.getById(1).ifPresent(personaDAO::delete);
        personaDAO.getById(2).ifPresent(personaDAO::delete);
        personaDAO.getById(7).ifPresent(personaDAO::delete);
        personaDAO.getById(10).ifPresent(personaDAO::delete);

        printPersonas(personaDAO);
    }

    private static void incrementarIngresos(DAO<Persona> personaDAO) {
        System.out.println("Incrementando ingresos un 5% ...");
        personaDAO.getAll()
                .peek(p -> p.setIngresos(p.getIngresos() * 1.05f))
                .forEach(personaDAO::update);

        printPersonas(personaDAO);
    }

    static void obtenerNacidosAntes2000(DAO<Persona> personaDAO) {
        System.out.println("Obteniendo nacidas antes del 2000...");
        printPersonasHeader();
        personaDAO.getAll()
                .filter(p -> p.getNacimiento().getYear() < 2000)
                .map(Personas::getPersonaAsRow)
                .forEach(System.out::println);
        System.out.println();
    }

    private static void printPersonasHeader() {
        System.out.println(Personas.getPersonasHeader(true));
    }

}
