package dam.ad.model.personas;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class Personas {
    public static List<Persona> generateSamplePersonas() {

        System.out.println("Generando un sample de personas...");
        return Stream.of(
                new Persona(1, "Armando", "Bronca Segura",
                        Sexo.HOMBRE,
                        LocalDate.of(1970, Month.AUGUST, 3),
                        2500.0f),
                new Persona(2, "Belen", "Tilla",
                        Sexo.MUJER,
                        LocalDate.of(1983, Month.DECEMBER, 6),
                        2100.0f),
                new Persona(3, "Esther", "Malgin",
                        Sexo.MUJER,
                        LocalDate.of(1988, Month.JULY, 4),
                        1800.0f),
                new Persona(4, "Amador", "Denador",
                        Sexo.HOMBRE,
                        LocalDate.of(1994, Month.DECEMBER, 24),
                        1600.0f),
                new Persona(5, "Aitor", "Tilla",
                        Sexo.HOMBRE,
                        LocalDate.of(2001, Month.JANUARY, 7),
                        1300.0f),
                new Persona(6, "Sandra", "Matica",
                        Sexo.MUJER,
                        LocalDate.of(1977, Month.FEBRUARY, 19),
                        1500.0f),
                new Persona(7, "Victor", "Nado",
                        Sexo.HOMBRE,
                        LocalDate.of(1998, Month.JUNE, 30),
                        2400.0f),
                new Persona(8, "Pedro", "Gado",
                        Sexo.HOMBRE,
                        LocalDate.of(2002, Month.APRIL, 23),
                        1100.0f),
                new Persona(9, "Vanesa", "Tánica",
                        Sexo.MUJER,
                        LocalDate.of(2000, Month.JANUARY, 6),
                        1200.0f),
                new Persona(10, "Marta", "Baco",
                        Sexo.MUJER,
                        LocalDate.of(1982, Month.JULY, 8),
                        1700.0f),
                new Persona(11, "Consuelo", "Tería",
                        Sexo.MUJER,
                        LocalDate.of(1967, Month.APRIL, 6),
                        1900.0f)

        ).toList();
    }

    public static String getPersonaAsRow(Persona persona) {
        return String.format("%2s %-20s %-30s %4s %-10s %9s",
                persona.getPersonaId(),
                persona.getNombre(),
                persona.getApellidos(),
                persona.getSexo().getInicial(),
                persona.getNacimiento().toString(),
                NumberFormat.getNumberInstance().format(persona.getIngresos()));
    }

    static final String[] FIELDS =
            {"ID", "NOMBRE", "APELLIDOS", "SEXO", "NACIMIENTO", "INGRESOS"};

    public static String getPersonasHeader(boolean withLine) {
        String format = "%-2s %-20s %-30s %-4s %-10s %9s";
        String header = String.format(format, (Object[]) FIELDS);

        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
        stringJoiner.add(header);
        if(withLine){
            String line = "-".repeat(header.length());
            stringJoiner.add(line);
        }
        return stringJoiner.toString();
    }

}
