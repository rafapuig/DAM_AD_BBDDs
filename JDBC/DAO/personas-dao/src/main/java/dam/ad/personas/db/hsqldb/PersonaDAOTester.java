package dam.ad.personas.db.hsqldb;

import dam.ad.dao.DAO;
import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Personas;
import dam.ad.model.personas.PersonasPrinter;
import dam.ad.model.personas.Sexo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class PersonaDAOTester {
    DAO<Persona> personaDAO;

    public PersonaDAOTester(DAO<Persona> personaDAO) {
        this.personaDAO = personaDAO;
    }

    static Consumer<Persona> printAddingInfo = persona ->
            System.out.println("Añadiendo a " +
                               persona.getNombre() + " " + persona.getApellidos());

    public void addSamplePersonas() {
        System.out.println("Añadiendo personas...");

        Personas.generateSamplePersonas()
                .stream()
                .peek(printAddingInfo)
                .forEach(personaDAO::add);
    }

    public void printPersonas() {
        PersonasPrinter
                .TO_CONSOLE
                .printPersonas(personaDAO.getAll());
    }

    public void getPersonaByIDTest(int id) {
        System.out.println("Recuperado persona con ID " + id + "...");

        Optional<Persona> personaById = personaDAO.getById(id);

        PersonasPrinter.TO_CONSOLE
                .printPersonas(personaById.stream());

        personaById
                .map(Personas::getPersonaAsRow)
                .ifPresent(System.out::println);

        personaById.ifPresent(System.out::println);

        System.out.println();
    }

    public Persona addNewPersona() {

        Persona persona = new Persona(0, "Perico", "Palotes",
                Sexo.HOMBRE, LocalDate.parse("1977-02-10"), 2300.0f);

        printAddingInfo.accept(persona);

        personaDAO.add(persona);

        System.out.println(persona);

        System.out.println(Personas.getPersonaAsRow(persona));

        printPersonas();

        return persona;
    }

    private static Consumer<Persona> printOperationInfo(String prefix) {
        Consumer<Persona> printPrefix =
                persona -> System.out.print(prefix);

        return printPrefix.andThen(printPersonaInfo);
    }

    private static Consumer<Persona> printPersonaInfo =
            persona -> System.out.println(
                    persona.getNombre() + " " +
                    persona.getApellidos());


    public void updatePersona(Persona persona) {
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

        printPersonas();
    }

    public void obtenerNacidosAntes2000() {
        System.out.println("Obteniendo nacidas antes del 2000...");

        PersonasPrinter.TO_CONSOLE.printPersonasHeader();

        personaDAO.getAll()
                .filter(p -> p.getNacimiento().getYear() < 2000)
                .forEach(PersonasPrinter.TO_CONSOLE::printPersonaRow);
    }

    public void incrementarIngresos() {
        System.out.println("Incrementando ingresos un 5% ...");
        personaDAO.getAll()
                .peek(printOperationInfo("Incrementando ingresos de "))
                .peek(p -> p.setIngresos(p.getIngresos() * 1.05f))
                .forEach(personaDAO::update);

        printPersonas();
    }

    public void borrarHombres() {
        System.out.println("Filtrando personas, borrando hombres...");

        personaDAO.getAll()
                .filter(p -> p.getSexo() == Sexo.HOMBRE)
                .peek(printOperationInfo("Borrando persona: "))
                .forEach(personaDAO::delete);

        printPersonas();
    }

    public void borrarPersonas() {
        System.out.println("Borrando personas 2, 3...");
        personaDAO.getById(2).ifPresent(personaDAO::delete);
        personaDAO.getById(3).stream().forEach(personaDAO::delete);

        System.out.println("Borrando personas 6 y 9...");
        IntStream.of(6, 9).forEach(
                id -> personaDAO.getById(id).ifPresent(personaDAO::delete)
        );

        System.out.println("Borrando personas 5 y 10...");
        IntStream.of(5, 10)
                .mapToObj(personaDAO::getById)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .peek(printOperationInfo("Borrando persona "))
                .forEach(personaDAO::delete);

        System.out.println("Borrando personas 4 y 12...");
        IntStream.of(4, 12)
                .mapToObj(personaDAO::getById)
                .flatMap(Optional::stream)
                .peek(printOperationInfo("Borrando persona "))
                .forEach(personaDAO::delete);

        System.out.println("Borrando persona 11...");
        //Otra forma de llamar a eliminar
        personaDAO.getById(11).stream()
                .peek(printOperationInfo("Borrando persona "))
                .forEach(personaDAO::delete);

        printPersonas();
    }
}
