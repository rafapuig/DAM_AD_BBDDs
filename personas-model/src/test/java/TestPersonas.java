import dam.ad.model.personas.Persona;
import dam.ad.model.personas.Personas;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestPersonas {
    @Test
    void testPrintPersonas() {

        List<Persona> personas = Personas.generateSamplePersonas();

        System.out.println(Personas.getPersonasHeader(true));

        personas.stream()
                .map(Personas::getPersonaAsRow)
                .forEach(System.out::println);
    }
}
