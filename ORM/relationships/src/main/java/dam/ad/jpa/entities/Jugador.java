package dam.ad.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate nacimiento;

    private Demarcacion demarcacion;

    @ManyToMany()
    @JoinTable(name = "nacionalidades", inverseJoinColumns = @JoinColumn(name = "pais_iso3"))
    @ToString.Exclude
    private Set<Pais> nacionalidades;   //Hace que sea PK compuesta en la tabla

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    private int dorsal;
}