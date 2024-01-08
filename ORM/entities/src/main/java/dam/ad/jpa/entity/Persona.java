package dam.ad.jpa.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
public class Persona {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PERSONAID")
    private int personaid;
    @Basic
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Basic
    @Column(name = "SEXO")
    private String sexo;
    @Basic
    @Column(name = "NACIMIENTO")
    private Date nacimiento;
    @Basic
    @Column(name = "INGRESOS")
    private Double ingresos;

    public int getPersonaid() {
        return personaid;
    }

    public void setPersonaid(int personaid) {
        this.personaid = personaid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Double getIngresos() {
        return ingresos;
    }

    public void setIngresos(Double ingresos) {
        this.ingresos = ingresos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return personaid == persona.personaid && Objects.equals(nombre, persona.nombre) && Objects.equals(apellidos, persona.apellidos) && Objects.equals(sexo, persona.sexo) && Objects.equals(nacimiento, persona.nacimiento) && Objects.equals(ingresos, persona.ingresos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personaid, nombre, apellidos, sexo, nacimiento, ingresos);
    }
}
