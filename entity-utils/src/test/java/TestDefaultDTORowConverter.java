import dam.ad.converters.Converters;
import org.junit.jupiter.api.Test;

public class TestDefaultDTORowConverter {

    @Test
    void testInheritance() {

        Empleado empleado = new Empleado("Armando","Bronca",1000);

        empleado.setAltura(1.754535434443f);

        String text = Converters.getAsRow(empleado);
        System.out.println(text);

    }
}
