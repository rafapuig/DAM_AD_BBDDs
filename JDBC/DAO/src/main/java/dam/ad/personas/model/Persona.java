package dam.ad.personas.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Persona {
    private int personaId;
    private String nombre;
    private String apellidos;
    private Sexo sexo;
    private LocalDate nacimiento;
    private double ingresos;

    public Persona(int personaId, String nombre, String apellidos, Sexo sexo, LocalDate nacimiento, double ingresos) {
        //System.out.println("Creando persona...");
        this.personaId = personaId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.nacimiento = nacimiento;
        this.ingresos = ingresos;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
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

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    public double getIngresos() {
        return ingresos;
    }

    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Persona.class.getSimpleName() + "[", "]")
                .add("personaId=" + personaId)
                .add("nombre='" + nombre + "'")
                .add("apellidos='" + apellidos + "'")
                .add("sexo=" + sexo)
                .add("nacimiento=" + nacimiento)
                .add("ingresos=" + ingresos)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona persona)) return false;
        return personaId == persona.personaId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personaId);
    }


    public static Builder builder(Persona persona) {
        return new Builder(persona);
    }

    public static final class Builder {
        private int personaId;
        private String nombre;
        private String apellidos;
        Sexo sexo;
        private LocalDate nacimiento;
        private double ingresos;

        public Builder(Persona persona) {
            this.personaId = persona.personaId;
            this.nombre = persona.nombre;
            this.apellidos = persona.apellidos;
            this.sexo = persona.sexo;
            this.nacimiento = persona.nacimiento;
            this.ingresos = persona.ingresos;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder apellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public Builder sexo(Sexo sexo) {
            this.sexo = sexo;
            return this;
        }

        public Builder nacimiento(LocalDate nacimiento) {
            this.nacimiento = nacimiento;
            return this;
        }

        public Builder ingresos(double ingresos) {
            this.ingresos = ingresos;
            return this;
        }

        public Persona build() {
            return new Persona(personaId, nombre, apellidos, sexo, nacimiento, ingresos);
        }

    }

    public Builder with() {
        return new Builder(this);
    }
}