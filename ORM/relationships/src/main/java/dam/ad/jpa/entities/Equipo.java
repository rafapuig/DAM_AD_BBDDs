package dam.ad.jpa.entities;

import dam.ad.dto.annotations.RowField;
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
    @RowField(columnLength = 3, label = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @RowField(columnLength = 30, label = "NOMBRE")
    private String nombre;

    @RowField(columnLength = 15, label = "PAIS", expression = "nombre")
    @ManyToOne
    @JoinColumn(name = "pais_iso3")
    private Pais pais;


    @RowField(columnLength = 20, label = "ENTRENADOR", expression = "nombre")
    @OneToOne
    @JoinColumn(name = "entrenador_id") //, nullable = false)
    private Entrenador entrenador;

    @ToString.Exclude
    @OneToMany(mappedBy = "equipo")
    private List<Jugador> jugadores;
}