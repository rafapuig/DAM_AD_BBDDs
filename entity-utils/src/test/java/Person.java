import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;

@RowConvertible
public class Person {

    @RowField(columnLength = 10)
    private String nombre;

    @RowField(columnLength = 15)
    private String apellidos;

    @RowField(columnLength = 10) //, numericFormat = "#,##0.000")
    private Float altura;

    @RowField(columnLength = 30, expression = "city")
    private Address address = new Address("Colon 64","Valencia","España");

    public Person(String nombre, String apellidos) {
        this.nombre = null;
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return "desde el getter de nombre";
    }

    public String getApellidos() {
        return apellidos;
    }

    public Address getAddress() {
        return address;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }
}
