package dam.ad.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SecondaryTable(name = Empresa.DIRECCION,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "empresa_id"))
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String razonSocial;

    static final String DIRECCION = "empresa_direccion";

    @Embedded
    @AttributeOverride(name = "via", column = @Column(table = DIRECCION))
    @AttributeOverride(name = "localidad", column = @Column(table = DIRECCION))
    Direccion direccion;
}
