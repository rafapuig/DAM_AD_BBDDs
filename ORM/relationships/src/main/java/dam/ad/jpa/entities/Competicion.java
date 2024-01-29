package dam.ad.jpa.entities;

import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@RowConvertible
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Competicion {

    @RowField(columnLength = 6)

    @Id
    @Column(length = 5)
    String codigo;


    @RowField(columnLength = 30)

    @Basic(optional = false)
    @Column(unique = true)
    String denominacion;


    @RowField(columnLength = 10)

    @Column(length = 10)
    String organizador;

    @RowField(columnLength = 30, expression = "nombre")
    @ManyToMany()
    @JoinTable(name = "competir",
            joinColumns = @JoinColumn(name = "competicion"),
            inverseJoinColumns = @JoinColumn(name="equipo"))
    private Collection<Equipo> equipos;
}
