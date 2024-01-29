package dam.ad.jpa.entities;

import dam.ad.dto.annotations.RowConvertible;
import dam.ad.dto.annotations.RowField;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

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
}
