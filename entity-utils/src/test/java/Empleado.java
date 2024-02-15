import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;

//@RowConvertible
public class Empleado extends Person {

    //@RowField(columnLength = 7)
    private int salary;

    public Empleado(String nombre, String apellidos, int salary) {
        super(nombre, apellidos);
        this.salary = salary;
    }

}
