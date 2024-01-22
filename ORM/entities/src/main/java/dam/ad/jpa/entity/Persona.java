package dam.ad.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@ToString
@Getter
@Setter
@Entity
@SecondaryTable(name = Direccion.DIRECCION,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "persona_id"))
public class Persona {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String nombre;

    private String apellidos;

    @Column(columnDefinition = "CHAR(1)")
    private Sexo sexo;

    private LocalDate nacimiento;

    //@Column(scale = 2, precision = 7)
    @Column(columnDefinition = "NUMERIC(7,2)")
    private Double ingresos;

//    @Column(precision = 7, scale = 2)
//    private Double ingresos;

//    static final String DIRECCION = "persona_DIRECCION";
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "via",
//                    column = @Column(table = DIRECCION))})
    Direccion direccion;
}
