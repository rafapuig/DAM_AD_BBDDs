package dam.ad.model.personas;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.stream.Stream;

public class PersonasPrinter {
    PrintWriter writer;

    public PersonasPrinter(PrintStream out) {
        writer = new PrintWriter(out, true);
    }

    public PersonasPrinter() {
        this(System.out);
    }

    public void printPersonasHeader() {
        writer.println(Personas.getPersonasHeader(true));
    }

    public void printPersonaRow(Persona persona) {
        writer.println(Personas.getPersonaAsRow(persona));
    }

    public void printPersonas(Stream<Persona> stream) {
        printPersonasHeader();

        stream
                .map(Personas::getPersonaAsRow)
                .forEach(writer::println);

        writer.println();
    }

    public static final PersonasPrinter TO_CONSOLE = new PersonasPrinter();
}
