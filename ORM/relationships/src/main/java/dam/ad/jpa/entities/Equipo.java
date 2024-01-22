package dam.ad.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pais_iso3")
    private Pais pais;

    @OneToOne
    @JoinColumn(name = "entrenador_id") //, nullable = false)
    private Entrenador entrenador;

    @ToString.Exclude
    @OneToMany(mappedBy = "equipo")
    private List<Jugador> jugadores;
}