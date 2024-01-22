package dam.ad.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.StringJoiner;

@Getter
@Setter
@ToString
@Entity
public class Entrenador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pais_iso3", nullable = false)
    private Pais pais;

    @ToString.Exclude
    @OneToOne(mappedBy = "entrenador")
    private Equipo equipo;

    /*@Override
    public String toString() {
        return new StringJoiner(", ", Entrenador.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("nombre='" + nombre + "'")
                .add("pais=" + pais)
                .add("equipo=" + (equipo != null ? equipo.getNombre() : null))
                .toString();
    }*/
}