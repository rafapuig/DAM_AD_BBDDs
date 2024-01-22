package dam.ad.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Embeddable
//@Table(name = Direccion.DIRECCION)
public class Direccion {

    public static final String DIRECCION = "Direccion";

    @Column(table = DIRECCION)
    String via;

    @Column(table = DIRECCION)
    String localidad;
//
//    @Column(table = DIRECCION)
//    String numero;
//
//    @Column(table = DIRECCION)
//    String escalera;
//
//    @Column(table = DIRECCION)
//    String piso;
//
//    @Column(table = DIRECCION)
//    int puerta;
//
//    @Column(table = DIRECCION)
//    String municipio;
//
//    @Column(table = DIRECCION)
//    String codigoPostal;
//
//    @Column(table = DIRECCION)
//    String provincia;
}
