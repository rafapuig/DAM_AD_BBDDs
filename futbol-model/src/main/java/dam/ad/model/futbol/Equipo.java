package dam.ad.model.futbol;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Equipo {
    private int equipoId;
    private String nombre;
    private String pais;

    List<Jugador> jugadores;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Equipo(int equipoId, String nombre, String pais) {
        this.equipoId = equipoId;
        this.nombre = nombre;
        this.pais = pais;
    }

    public int getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(int equipoId) {
        this.equipoId = equipoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo equipo)) return false;
        return equipoId == equipo.equipoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipoId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Equipo.class.getSimpleName() + "[", "]")
                .add("equipoId=" + equipoId)
                .add("nombre='" + nombre + "'")
                .add("pais='" + pais + "'")
                .toString();
    }
}
