package dam.ad.jpa.entities;

import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RowConvertible
@Getter
@Setter
@ToString
@Entity
public class Jugador {
    @RowField(columnLength = 3)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @RowField(columnLength = 30)
    private String nombre;

    @RowField(columnLength = 11)
    private LocalDate nacimiento;

    @RowField(columnLength = 15)
    private Demarcacion demarcacion;

    //@RowField(columnLength = 9)
    @ManyToMany()
    @JoinTable(name = "nacionalidades",
            joinColumns = @JoinColumn(name = "jugador_id"), //Es el que le pondría por defecto
            inverseJoinColumns = @JoinColumn(name = "pais_iso3"))
    @ToString.Exclude
    private Set<Pais> nacionalidades;   //Hace que sea PK compuesta en la tabla

    @RowField(columnLength = 25, label = "equipo", expression = "nombre")
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @RowField(columnLength = 6)
    private int dorsal;
}