package dam.ad.model.futbol;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Jugador {
    private int jugadorId;
    private String nombre;
    private String pais;
    LocalDate nacimiento;
    double estatura;
    int peso;
    int dorsal;
    int equipoId;
    boolean capitan;
    Equipo equipo;

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Jugador(int jugadorId, String nombre, String pais, LocalDate nacimiento, double estatura, int peso, int dorsal, int equipoId, boolean capitan) {
        this.jugadorId = jugadorId;
        this.nombre = nombre;
        this.pais = pais;
        this.nacimiento = nacimiento;
        this.estatura = estatura;
        this.peso = peso;
        this.dorsal = dorsal;
        this.equipoId = equipoId;
        this.capitan = capitan;
    }

    public int getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(int jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public int getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(int equipoId) {
        this.equipoId = equipoId;
    }

    public boolean isCapitan() {
        return capitan;
    }

    public void setCapitan(boolean capitan) {
        this.capitan = capitan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador jugador)) return false;
        return jugadorId == jugador.jugadorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jugadorId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Jugador.class.getSimpleName() + "[", "]")
                .add("jugadorId=" + jugadorId)
                .add("nombre='" + nombre + "'")
                .add("pais='" + pais + "'")
                .add("nacimiento=" + nacimiento)
                .add("estatura=" + estatura)
                .add("peso=" + peso)
                .add("dorsal=" + dorsal)
                .add("equipoId=" + equipoId)
                .add("capitan=" + capitan)
                .toString();
    }
}
