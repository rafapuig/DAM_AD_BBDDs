package dam.ad.jdbc.statements.personas;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.function.Consumer;

public class PersonasPrinter implements Consumer<ResultSet> {
    static PrintStream printer = System.out;

    public static void printPersonas(ResultSet rs) {

        String format = "%-2s %-20s %-30s %-4s %-10s %s\n";

        printer.printf(format, "ID", "NOMBRE", "APELLIDOS", "SEXO", "NACIMIENTO", "INGRESOS");

        try {
            while (rs.next()) {
                printPersona(rs);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("ERROR al recorrer el ResultSet", e);
        }
    }

    static void printPersona(ResultSet rs) throws SQLException {

        String format = "%2s %-20s %-30s %4s %10s %.2f\n";

        int personaId = rs.getInt("personaID");
        String nombre = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String sexo = rs.getString("sexo");
        LocalDate nacimiento = rs.getDate("nacimiento").toLocalDate();
        float ingresos = rs.getFloat("ingresos");

        printer.printf(format, personaId, nombre, apellidos, sexo, nacimiento, ingresos);
    }

    @Override
    public void accept(ResultSet resultSet) {
        printPersonas(resultSet);
    }
}
